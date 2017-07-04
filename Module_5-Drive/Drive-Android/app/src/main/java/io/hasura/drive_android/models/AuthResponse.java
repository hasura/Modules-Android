package io.hasura.drive_android.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jaison on 23/01/17.
 */

public class AuthResponse {

    @SerializedName("auth_token")
    String authToken;

    @SerializedName("hasura_id")
    Integer id;

    @SerializedName("hasura_roles")
    List<String> roles;

    public String getAuthToken() {
        return authToken;
    }

    public Integer getId() {
        return id;
    }

    public List<String> getRoles() {
        return roles;
    }
}
