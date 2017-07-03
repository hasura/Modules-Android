package io.hasura.drive_android.hasura;

import java.util.List;

import io.hasura.drive_android.models.HasuraFile;
import io.hasura.drive_android.models.HasuraFolder;
import io.hasura.drive_android.models.hasuraQueries.DeleteFileQuery;
import io.hasura.drive_android.models.hasuraQueries.FileReturningResponse;
import io.hasura.drive_android.models.hasuraQueries.InsertFileQuery;
import io.hasura.drive_android.models.hasuraQueries.SelectFileQuery;
import io.hasura.drive_android.models.hasuraQueries.SelectFolderQuery;
import io.hasura.drive_android.models.hasuraQueries.UpdateFileQuery;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by jaison on 23/01/17.
 */

public interface HasuraDBInterface {

    @POST(Endpoint.QUERY)
    Call<List<HasuraFile>> getFiles(@Body SelectFileQuery query);

    @POST(Endpoint.QUERY)
    Call<FileReturningResponse> addFile(@Body InsertFileQuery query);

    @POST(Endpoint.QUERY)
    Call<FileReturningResponse> updateFile(@Body UpdateFileQuery query);

    @POST(Endpoint.QUERY)
    Call<FileReturningResponse> deleteFile(@Body DeleteFileQuery query);

    @POST(Endpoint.QUERY)
    Call<List<HasuraFolder>> getFolders(@Body SelectFolderQuery query);
}
