package io.hasura.drive_android.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jaison on 23/01/17.
 */

public class AuthRequest {

    @SerializedName("username")
    String username;

    @SerializedName("mobile")
    String mobile;

    @SerializedName("otp")
    String otp;

    public AuthRequest(String mobile) {
        this.mobile = mobile;
        this.username = mobile;
    }

    public  AuthRequest(String mobile, String otp) {
        this.username = mobile;
        this.mobile = mobile;
        this.otp = otp;
    }
}
