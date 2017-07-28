package com.example.android.signuphasura.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amogh on 15/6/17.
 */
public class SocialLoginResponse {
    @SerializedName("hasura_id")
    int hasuraId;

    @SerializedName("hasura_roles")
    String[] hasuraRoles;

    @SerializedName("new_user")
    boolean newUser;

    @SerializedName("auth_token")
    String auth_token;

    @SerializedName("access_token")
    String access_token;

    public int getHasuraId() {
        return hasuraId;
    }

    public String[] getHasuraRoles() {
        return hasuraRoles;
    }

    public String getSessionId() {
        return auth_token;
    }

    public boolean isNewUser() {
        return newUser;
    }

    public String getAccessToken() {
        return access_token;
    }


}
