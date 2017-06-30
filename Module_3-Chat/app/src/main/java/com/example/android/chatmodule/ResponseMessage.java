package com.example.android.chatmodule;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amogh on 16/6/17.
 */

public class ResponseMessage {
    @SerializedName("affected_rows")
    Integer rowsAffected;

    public Integer getRowsAffected(){
        return rowsAffected;
    }
}
