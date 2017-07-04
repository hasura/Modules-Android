package com.example.android.chatmodule;

/**
 * Created by jaison on 04/04/17.
 */

public class ServerError {
    private ServerErrorType type;
    private String errorMessage;

    public ServerError(ErrorResponse response) {
        this.type = ServerErrorType.lookUpByCode(response.getCode());
        this.errorMessage = response.getMessage();
    }

    public ServerError(ServerErrorType type, String errorMessage) {
        this.type = type;
        this.errorMessage = errorMessage;
    }

    public ServerErrorType getType() {
        return type;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
