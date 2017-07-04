package com.example.android.chatmodule;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jaison on 29/03/17.
 */

public enum ServerErrorType {
    INVALID_AUTH("invalid-auth"),
    INTERNET("internet"),
    UNKNOWN("Unknown"),
    USER_INVALID("invalid-user"),
    USER_ALREADY_EXISTS("user-already-exists");

    private String code;
    private static final Map<String, ServerErrorType> valuesByCode;

    static {
        valuesByCode = new HashMap<>();
        for (ServerErrorType errorType : ServerErrorType.values()) {
            valuesByCode.put(errorType.code, errorType);
        }
    }

    ServerErrorType(String code) {
        this.code = code;
    }

    public static ServerErrorType lookUpByCode(String code) {
        return valuesByCode.get(code) != null ? valuesByCode.get(code) : UNKNOWN;
    }

    public String getCode() {
        return code;
    }

}
