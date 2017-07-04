package io.hasura.drive_android.ui.home;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.jaison.bsimagepicker.BottomSheetImagePicker;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.hasura.drive_android.R;
import io.hasura.drive_android.models.HasuraFile;
import io.hasura.drive_android.models.HasuraFolder;
import io.hasura.drive_android.models.ServerError;
import io.hasura.drive_android.models.UploadingFile;
import io.hasura.drive_android.stores.FileDataStore;
import io.hasura.drive_android.ui.editFile.EditFileActivity;
import io.hasura.drive_android.ui.fileDetail.FileDetailActivity;
import io.hasura.drive_android.ui.launcher.LauncherActivity;
import io.hasura.drive_android.ui.utils.FileOptionBottomSheet;

public class HomeActivity extends AppCompatActivity implements FileOptionBottomSheet.Listener {

    private static final String TAG = "HomeActivity";
    private static final String FOLDER_KEY = "FolderKey";

    enum ViewType {
        GRID, LIST
    }

    HasuraFolder folder;

    FileDataStore dataStore;
    SectionedRecyclerViewAdapter adapter;

    FileSection savedFileSection;
    UploadingFileSection uploadingFileSection;
    EmptySection emptySection;
    UploadFailedFileSection uploadFailedFileSection;

    MenuItem toggelViewItem;
    ViewType currentlyShowingViewType = ViewType.LIST;
    GridLayoutManager gridLayoutManager;
    FileOptionBottomSheet fileOptionBottomSheet;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;
    @BindView(R.id.bottomSheet)
    BottomSheetLayout bottomSheet;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.retry_button)
    Button retryButton;
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;

    //Start activity by clearing the back stack
    public static void startActivity(Activity startingActivity, HasuraFolder folder) {
        Intent intent = new Intent(startingActivity, HomeActivity.class);
        intent.putExtra(FOLDER_KEY, folder);
        startingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        folder = getIntent().getParcelableExtra(FOLDER_KEY);
        dataStore = FileDataStore.getInstance();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(folder.getName());

        showErrorLayout(false);

        //Setup recycler view
        adapter = new SectionedRecyclerViewAdapter();
        savedFileSection = new FileSection(this, new FileSection.Listener() {
            @Override
            public void onItemCountChanged(int count) {
                savedFileSection.setVisible(count > 0);
                checkIsEmptySectionShouldBeShown();
            }

            @Override
            public void onFileSelected(HasuraFile file) {
                FileDetailActivity.startActivity(HomeActivity.this, file);
            }

            @Override
            public void onFileOptionsSelected(HasuraFile file) {
                fileOptionBottomSheet = FileOptionBottomSheet.newInstance(file);
                fileOptionBottomSheet.show(getSupportFragmentManager(), "BottomSheet");
            }
        });

        uploadingFileSection = new UploadingFileSection(this, new UploadingFileSection.InteractionListener() {
            @Override
            public void onStopUploadClicked(UploadingFile file) {
                dataStore.cancelFileUpload(file);
            }

            @Override
            public void onItemCountChanged(int count) {
                uploadingFileSection.setVisible(count > 0);
                checkIsEmptySectionShouldBeShown();
            }
        });

        uploadFailedFileSection = new UploadFailedFileSection(this, new UploadFailedFileSection.InteractionListener() {
            @Override
            public void onRetryUploadClicked(UploadingFile file) {
                dataStore.retryFileUpload(file);
                uploadFailedFileSection.removeData(file);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onDeleteUploadClicked(UploadingFile file) {
                uploadFailedFileSection.removeData(file);
                dataStore.removeFailedUpload(file);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onItemCountChanged(int count) {
                uploadFailedFileSection.setVisible(count > 0);
                checkIsEmptySectionShouldBeShown();
            }
        });

        emptySection = new EmptySection();

        adapter.addSection(uploadFailedFileSection);
        adapter.addSection(uploadingFileSection);
        adapter.addSection(savedFileSection);

        adapter.addSection(emptySection);

        uploadFailedFileSection.setVisible(false);
        uploadingFileSection.setVisible(false);
        savedFileSection.setVisible(false);
        emptySection.setVisible(false);

        gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getSectionItemViewType(position)) {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        return 2;
                    default:
                        return currentlyShowingViewType == ViewType.LIST ? 2 : 1;
                }
            }
        });

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "OnSwipeRefresh");
                dataStore.syncStore();
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetImagePicker.getInstance().showImagePicker(HomeActivity.this, bottomSheet, new BottomSheetImagePicker.Listener() {
                    @Override
                    public void onImageArrived(Uri selectedImageUri) {
                        Log.i(TAG, "Image Arrived");
                        dataStore.uploadFile(selectedImageUri, folder.getId());
                    }
                });
            }
        });

    }

    private void checkIsEmptySectionShouldBeShown() {
        emptySection.setVisible(false);
        if (!swipeRefreshLayout.isRefreshing()) {
            if (savedFileSection.getContentItemsTotal() == 0 && uploadingFileSection.getContentItemsTotal() == 0 && uploadFailedFileSection.getContentItemsTotal() == 0) {
                emptySection.setVisible(true);
            }
        }
    }

    private void showProgressIndicator(Boolean show) {
        if (show)
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
        else {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private FileDataStore.Listener dataStoreListener = new FileDataStore.Listener() {

        @Override
        public void currentDataSet(List<HasuraFile> availableFiles, List<UploadingFile> uploadingFileList, List<UploadingFile> uploadFailedFiles) {
            Log.i(TAG, "Current Data Set");
            if (availableFiles != null)
                savedFileSection.setData(availableFiles);

            if (uploadingFileList != null && uploadingFileList.size() > 0)
                uploadingFileSection.setData(uploadingFileList);

            if (uploadFailedFiles != null && uploadFailedFiles.size() > 0)
                uploadFailedFileSection.setData(uploadFailedFiles);

            adapter.notifyDataSetChanged();
        }

        @Override
        public void onDataFetchStarted() {
            showProgressIndicator(true);
            Log.i(TAG, "OnDataFetchStarted");
        }

        @Override
        public void onDataFetchComplete(List<HasuraFile> fileList) {
            showProgressIndicator(false);
            savedFileSection.setData(fileList);
            savedFileSection.setState(Section.State.LOADED);
            adapter.notifyDataSetChanged();
            Log.i(TAG, "OnDataFetchComplete");
        }

        @Override
        public void onDataFetchError(ServerError error) {
            showProgressIndicator(false);
            showErrorLayout(true);
            Log.i(TAG, "OnDataFetchError");
        }

        @Override
        public void onFileUploadStarted(UploadingFile file) {
            uploadingFileSection.addData(file);
            adapter.notifyDataSetChanged();
            Log.i(TAG, "OnFileUploadStarted");
        }

        @Override
        public void onFileUploadCancelled(UploadingFile file) {
            uploadingFileSection.removeData(file);
            adapter.notifyDataSetChanged();
            Log.i(TAG, "OnFileUploadCancelled");
        }

        @Override
        public void onFileUploadComplete(UploadingFile uploadingFile, HasuraFile file) {
            Log.i(TAG, "OnFileUploadCompleter");
            uploadingFileSection.removeData(uploadingFile);
            savedFileSection.addData(file);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFileUploadFailed(UploadingFile file, ServerError error) {
            uploadingFileSection.removeData(file);
            uploadFailedFileSection.addData(file);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFileDeleteStarted(HasuraFile file) {
            savedFileSection.removeData(file);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFileDeleteCompleted(HasuraFile file) {

        }

        @Override
        public void onFileDeleteFailed(HasuraFile file, ServerError error) {
            savedFileSection.addData(file);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onSessionExpired() {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("Session Expired");
            builder.setMessage("Your session has expired.Please login in again.");
            builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //UserAuthStore.clearSession();
                    LauncherActivity.startActivity(HomeActivity.this);
                }
            });
            builder.show();
        }
    };

    @Override
    public void onEditFileClicked(HasuraFile file) {
        EditFileActivity.startActivity(this, file);
    }

    @Override
    public void onDeleteFileClicked(final HasuraFile file) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete File");
        builder.setMessage(file.getName() + " will be deleted.");
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dataStore.deleteFile(file);
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.RED);
    }

    @OnClick(R.id.retry_button)
    public void onRetryButtonClicked() {
        showErrorLayout(false);
        dataStore.syncStore();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        BottomSheetImagePicker.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BottomSheetImagePicker.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataStore.registerListener(dataStoreListener, folder.getId());
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataStore.unregisterListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        toggelViewItem = menu.findItem(R.id.toggle_view);
        toggelViewItem.setVisible(false);
        toggelViewItem.setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toggle_view:
                toggleView();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleView() {
        currentlyShowingViewType = currentlyShowingViewType == ViewType.LIST ? ViewType.GRID : ViewType.LIST;
        switch (currentlyShowingViewType) {
            case GRID:
                toggelViewItem.setIcon(R.drawable.ic_view_grid);
                break;
            case LIST:
                toggelViewItem.setIcon(R.drawable.ic_view_list);
                break;
        }
        adapter.notifyDataSetChanged();
    }

    private void showErrorLayout(Boolean shouldShow) {
        errorLayout.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
        bottomSheet.setVisibility(!shouldShow ? View.VISIBLE : View.GONE);
    }
}
