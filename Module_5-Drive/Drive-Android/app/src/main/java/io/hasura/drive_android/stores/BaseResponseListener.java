package io.hasura.drive_android.stores;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import io.hasura.drive_android.enums.ServerErrorType;
import io.hasura.drive_android.models.ErrorResponse;
import io.hasura.drive_android.models.ServerError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jaison on 04/04/17.
 */

public abstract class BaseResponseListener<T> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        Log.i("ServerResponse","OnResponse");
        if (response.isSuccessful()) {
            onSuccessfulResponse(response.body());
        } else {
            try {
                ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                if (errorResponse.getMessage() != null) {
                    if (errorResponse.getMessage().equals("invalid authorization token")) {
                        onFailureResponse(new ServerError(ServerErrorType.INVALID_AUTH, errorResponse.getMessage()));
                        return;
                    }
                }
                onFailureResponse(new ServerError(errorResponse));
            } catch (JsonSyntaxException | IOException e) {
                e.printStackTrace();
                onFailureResponse(new ServerError(ServerErrorType.UNKNOWN, "Something went wrong"));
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        t.printStackTrace();
        onFailureResponse(new ServerError(ServerErrorType.INTERNET, "Please ensure that you have a working internet connection"));
    }

    public abstract void onSuccessfulResponse(T response);

    public abstract void onFailureResponse(ServerError error);
}
