package io.hasura.drive_android.stores;

import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.hasura.drive_android.enums.FileEditState;
import io.hasura.drive_android.enums.FolderListState;
import io.hasura.drive_android.models.FileUploadResponse;
import io.hasura.drive_android.models.FolderResponse;
import io.hasura.drive_android.models.HasuraFile;
import io.hasura.drive_android.models.HasuraFolder;
import io.hasura.drive_android.models.ServerError;
import io.hasura.drive_android.models.UploadingFile;
import io.hasura.drive_android.models.hasuraQueries.DeleteFileQuery;
import io.hasura.drive_android.models.hasuraQueries.FileReturningResponse;
import io.hasura.drive_android.models.hasuraQueries.InsertFileQuery;
import io.hasura.drive_android.models.hasuraQueries.SelectFileQuery;
import io.hasura.drive_android.models.hasuraQueries.SelectFolderQuery;
import io.hasura.drive_android.models.hasuraQueries.UpdateFileQuery;
import io.hasura.drive_android.models.hasuraQueries.InsertFolderQuery;
import io.hasura.drive_android.utils.UriPathProvider;
import io.hasura.sdk.Callback;
import io.hasura.sdk.HasuraClient;
import io.hasura.sdk.HasuraErrorCode;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseListener.FileUploadResponseListener;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

import static io.hasura.drive_android.R.drawable.error;

/**
 * Created by jaison on 29/03/17.
 */

public class FileDataStore {

    HasuraClient client = io.hasura.sdk.Hasura.getClient();
    HasuraUser user = client.getUser();


    public interface Listener {
        void currentDataSet(List<HasuraFile> availableFiles, List<UploadingFile> uploadingFileList, List<UploadingFile> uploadFailedFiles);

        void onDataFetchStarted();

        void onDataFetchComplete(List<HasuraFile> fileList);

        void onDataFetchError(ServerError error);

        void onFileUploadStarted(UploadingFile file);

        void onFileUploadCancelled(UploadingFile file);

        void onFileUploadComplete(UploadingFile uploadingFile, HasuraFile file);

        void onFileUploadFailed(UploadingFile file, ServerError error);

        void onFileDeleteStarted(HasuraFile file);

        void onFileDeleteCompleted(HasuraFile file);

        void onFileDeleteFailed(HasuraFile file, ServerError error);

        void onSessionExpired();
    }

    public interface FileEditListener {
        void onFileEditStateChanged(FileEditState editState, Object object);
    }

    public interface FolderListListener {
        void onFolderListStateChanged(FolderListState folderListState, Object object);
    }

    private static FileDataStore instance;
    private Listener listener;
    private int folderId;
    private List<HasuraFile> data = new ArrayList<>();
    private List<HasuraFolder> folders = new ArrayList<>();
    private boolean isFetchingData = false;

    private List<UploadingFile> failedUploadList = new ArrayList<>();
    private Map<UploadingFile, Call<FileUploadResponse>> uploadingFileCallMap = new HashMap<>();
    private Map<UploadingFile, Call<FileReturningResponse>> insertingFileCallMap = new HashMap<>();

    public static FileDataStore getInstance() {
        if (instance == null) {
            instance = new FileDataStore();
        }
        return instance;
    }

    public void unregisterListener() {
        this.listener = null;
        this.folderId = -1;
    }

    public void registerListener(Listener listener, int folderId) {
        this.listener = listener;
        this.folderId = folderId;
        broadcastCurrentData();
        if (data.size() == 0) {
            syncStore();
        }
    }

    public HasuraFile getFileDetailsForId(String id) {
        for (HasuraFile file : data) {
            if (file.getId().equals(id))
                return file;
        }

        return null;
    }

    private void broadcastCurrentData() {
        if (listener != null && folderId != -1) {
            listener.currentDataSet(getSavedFilesForFolder(folderId), getUploadingFilesForFolder(folderId), getFailedUploadList(folderId));
        }
    }

    public void syncStore() {
        onDataFetchStarted();
        if (isFetchingData) {
            return;
        }
        isFetchingData = true;

        client.useDataService()
                .setRequestBody(new SelectFileQuery(user.getId()))
                .expectResponseTypeArrayOf(HasuraFile.class)
                .enqueue(new Callback<List<HasuraFile>, HasuraException>() {
                    @Override
                    public void onSuccess(List<HasuraFile> hasuraFiles) {
                        isFetchingData = false;
                        data = hasuraFiles;
                        onDataFetchComplete();
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        isFetchingData = false;
                    }
                });
    }

    public void retryFileUpload(UploadingFile file) {
        removeFromFailedFileList(file);
        makeFileUploadAPICall(file);
    }

    public void removeFailedUpload(UploadingFile file) {
        removeFromFailedFileList(file);
    }

    public void uploadFile(Uri imageUri, int folderId) {
        String imageName = "IMG_" + user.getId() + new Date().getTime();
        final UploadingFile uploadingFile = new UploadingFile(imageName, imageUri, folderId);
        makeFileUploadAPICall(uploadingFile);
    }

    private void makeFileUploadAPICall(final UploadingFile uploadingFile) {
        File file;
        try {
            file = new File(UriPathProvider.getInstance().getPath(uploadingFile.getImageUri()));
        } catch (NullPointerException e) {
            file = new File(uploadingFile.getImageUri().getPath());
        }
        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
        onFileUploadStarted(uploadingFile);

        client.useFileStoreService()
                .uploadFile(uploadingFile.getName(), file, "image/*", new FileUploadResponseListener() {
                    @Override
                    public void onUploadComplete(io.hasura.sdk.model.response.FileUploadResponse fileUploadResponse) {
                        uploadingFileCallMap.remove(uploadingFile);
                        addFileToTable(uploadingFile, fileUploadResponse);
                    }

                    @Override
                    public void onUploadFailed(HasuraException e) {
                        uploadingFileCallMap.remove(uploadingFile);
                        if (e.getCode() == HasuraErrorCode.INVALID_USER) {
                            onSessionExpired();
                        }
                        addToFailedFilesList(uploadingFile);
                    }
                });
    }

    private void addFileToTable(final UploadingFile uploadingFile, io.hasura.sdk.model.response.FileUploadResponse response) {
        //Call<FileReturningResponse> call = Hasura.db.addFile(new InsertFileQuery(response, uploadingFile.getFolderId()));
        //insertingFileCallMap.put(uploadingFile, call);

        client.useDataService()
                .setRequestBody(new InsertFileQuery(response, uploadingFile.getFolderId()))
                .expectResponseType(FileReturningResponse.class)
                .enqueue(new Callback<FileReturningResponse, HasuraException>() {
                    @Override
                    public void onSuccess(FileReturningResponse fileReturningResponse) {
                        removeFromFailedFileList(uploadingFile);
                        //insertingFileCallMap.remove(uploadingFile);
                        onFileUploadComplete(uploadingFile, fileReturningResponse.getFiles().get(0));
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        //insertingFileCallMap.remove(uploadingFile);
                        if (e.getCode() == HasuraErrorCode.INVALID_USER) {
                            ;
                        }// else onFileUploadFailed(uploadingFile, error);
                        addToFailedFilesList(uploadingFile);
                    }
                });
    }

    private void addToFailedFilesList(UploadingFile file) {
        if (!failedUploadList.contains(file)) {
            failedUploadList.add(file);
        }
    }

    private void removeFromFailedFileList(UploadingFile file) {
        failedUploadList.remove(file);
    }

    public void cancelFileUpload(UploadingFile file) {
        Call<FileUploadResponse> uploadingFileCall = uploadingFileCallMap.get(file);
        if (uploadingFileCall != null) {
            uploadingFileCall.cancel();
            uploadingFileCallMap.remove(uploadingFileCall);
        } else {
            Call<FileReturningResponse> insertingFileCall = insertingFileCallMap.get(file);
            if (insertingFileCall != null) {
                insertingFileCall.cancel();
                insertingFileCallMap.remove(insertingFileCall);
            }
        }
        onFileUploadCancelled(file);
    }

    public void deleteFile(final HasuraFile file) {
        data.remove(file);
        onFileDeleteStarted(file);

        client.useDataService()
                .setRequestBody(new DeleteFileQuery(file.getId(), client.getUser().getId()))
                .expectResponseType(FileReturningResponse.class)
                .enqueue(new Callback<FileReturningResponse, HasuraException>() {
                    @Override
                    public void onSuccess(FileReturningResponse fileReturningResponse) {
                        data.remove(file);
                        onFileDeleteCompleted(file);
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        data.add(file);
                        //onFileDeleteFailed(file, e);
                    }
                });
    }

    public void editFile(final HasuraFile file, final FileEditListener editListener) {
        editListener.onFileEditStateChanged(FileEditState.EDIT_STARTED, file);

        client.useDataService()
                .setRequestBody(new UpdateFileQuery(file, client.getUser().getId()))
                .expectResponseType(FileReturningResponse.class)
                .enqueue(new Callback<FileReturningResponse, HasuraException>() {
                    @Override
                    public void onSuccess(FileReturningResponse fileReturningResponse) {
                        for (int i = 0; i < data.size(); i++) {
                            HasuraFile hasuraFile = data.get(i);
                            if (hasuraFile.getId().equals(file.getId())) {
                                data.set(i, fileReturningResponse.getFiles().get(0));
                                break;
                            }
                        }
                        editListener.onFileEditStateChanged(FileEditState.EDIT_COMPLETED, fileReturningResponse.getFiles().get(0));
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        editListener.onFileEditStateChanged(FileEditState.EDIT_FAILED, error);
                    }
                });
    }

    public int addFolder(String name,int userId){
        final int[] folderId = {0};

        client.useDataService()
                .setRequestBody(new InsertFolderQuery(name,userId))
                .expectResponseType(FolderResponse.class)
                .enqueue(new Callback<FolderResponse, HasuraException>() {
                    @Override
                        public void onSuccess(FolderResponse folderResponse) {
                            folderId[0] = folderResponse.getFolder().getId();
                        }

                    @Override
                    public void onFailure(HasuraException e) {

                    }
                });

        return folderId[0];

    }

    public void getFolderList(final FolderListListener listListener) {
        listListener.onFolderListStateChanged(FolderListState.SYNC_STARTED, null);

        client.useDataService()
                .setRequestBody(new SelectFolderQuery())
                .expectResponseTypeArrayOf(HasuraFolder.class)
                .enqueue(new Callback<List<HasuraFolder>, HasuraException>() {
                    @Override
                    public void onSuccess(List<HasuraFolder> hasuraFolders) {
                        folders = hasuraFolders;
                        listListener.onFolderListStateChanged(FolderListState.SYNC_COMPLETE, folders);
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        listListener.onFolderListStateChanged(FolderListState.SYNC_FAILED, error);
                    }
                });
    }

    /**
     * STATUS
     **/

    private void onDataFetchStarted() {
        if (listener != null) {
            listener.onDataFetchStarted();
        }
    }

    private void onDataFetchComplete() {
        if (listener != null) {
            listener.onDataFetchComplete(getSavedFilesForFolder(folderId));
        }
    }

    private void onDataFetchError(ServerError error) {
        if (listener != null) {
            listener.onDataFetchError(error);
        }
    }

    private void onFileUploadStarted(UploadingFile file) {
        if (listener != null) {
            if (file.getFolderId() == folderId)
                listener.onFileUploadStarted(file);
        }
    }

    private void onFileUploadCancelled(UploadingFile file) {
        if (listener != null) {
            if (file.getFolderId() == folderId)
                listener.onFileUploadCancelled(file);
        }
    }

    private void onFileUploadComplete(UploadingFile uploadingFile, HasuraFile file) {
        if (listener != null) {
            if (uploadingFile.getFolderId() == folderId)
                listener.onFileUploadComplete(uploadingFile, file);
        }
    }

    private void onFileUploadFailed(UploadingFile file, ServerError error) {
        if (listener != null) {
            if (file.getFolderId() == folderId)
                listener.onFileUploadFailed(file, error);
        }
    }

    private void onFileDeleteStarted(HasuraFile file) {
        if (listener != null) {
            if (file.getFolder_id() == folderId)
                listener.onFileDeleteStarted(file);
        }
    }

    private void onFileDeleteCompleted(HasuraFile file) {
        if (listener != null) {
            if (file.getFolder_id() == folderId)
                listener.onFileDeleteCompleted(file);
        }
    }

    private void onFileDeleteFailed(HasuraFile file, ServerError error) {
        if (listener != null) {
            if (file.getFolder_id() == folderId)
                listener.onFileDeleteFailed(file, error);
        }
    }

    private void onSessionExpired() {
        if (listener != null) {
            listener.onSessionExpired();
        }
    }

    /**
     * REDUCERS
     **/

    private List<UploadingFile> getUploadingFilesForFolder(int folderId) {
        List<UploadingFile> uploadingFiles = new ArrayList<>();
        for (Map.Entry<UploadingFile, Call<FileUploadResponse>> entry : uploadingFileCallMap.entrySet()) {
            if (entry.getKey().getFolderId() == folderId)
                uploadingFiles.add(entry.getKey());
        }
        for (Map.Entry<UploadingFile, Call<FileReturningResponse>> entry : insertingFileCallMap.entrySet()) {
            if (entry.getKey().getFolderId() == folderId)
                uploadingFiles.add(entry.getKey());
        }
        return uploadingFiles;
    }

    private List<HasuraFile> getSavedFilesForFolder(int folderId) {
        List<HasuraFile> fileList = new ArrayList<>();
        for (HasuraFile file : data) {
            if (file.getFolder_id() == folderId)
                fileList.add(file);
        }
        return fileList;
    }

    private List<UploadingFile> getFailedUploadList(int folderId) {
        List<UploadingFile> uploadingFiles = new ArrayList<>();
        for (UploadingFile uploadingFile : failedUploadList)
            if (uploadingFile.getFolderId() == folderId)
                uploadingFiles.add(uploadingFile);
        return uploadingFiles;
    }

}
