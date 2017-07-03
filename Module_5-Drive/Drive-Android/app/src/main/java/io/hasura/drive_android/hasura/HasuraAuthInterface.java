package io.hasura.drive_android.hasura;

import io.hasura.drive_android.models.AuthRequest;
import io.hasura.drive_android.models.AuthResponse;
import io.hasura.drive_android.models.MessageResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;


/**
 * Created by jaison on 23/01/17.
 */

public interface HasuraAuthInterface {

    @POST(Endpoint.LOGIN)
    Call<MessageResponse> verifyMobile(@Body AuthRequest request);

    @POST(Endpoint.REGISTER)
    Call<AuthResponse> registerMobile(@Body AuthRequest request);

    @POST(Endpoint.LOGIN)
    Call<AuthResponse> verifyOtp(@Body AuthRequest request);

    @GET(Endpoint.LOGOUT)
    Call<MessageResponse> logout(@Header("Authorization") String authHeader);

}
