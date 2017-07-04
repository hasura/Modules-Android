package com.example.android.chatmodule;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jaison on 04/04/17.
 */

public class ErrorResponse {

    @SerializedName("message")
    String message;

    @SerializedName("code")
    String code;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
