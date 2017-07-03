package io.hasura.drive_android.hasura;

import io.hasura.drive_android.models.FileUploadResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by jaison on 05/04/17.
 */

public interface HasuraFileServiceInterface {

    @Headers("content-type: text/plain;charset=UTF-8")
    @POST(Endpoint.FILE + "/{fileName}")
    Call<FileUploadResponse> uploadFile(@Path("fileName") String fileName, @Body RequestBody body);
}
