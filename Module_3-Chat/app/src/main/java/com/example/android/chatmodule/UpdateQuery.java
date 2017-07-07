package com.example.android.chatmodule;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amogh on 19/6/17.
 */

public class UpdateQuery {
    @SerializedName("type")
    String type = "update";

    @SerializedName("args")
    Args args;

    class Args{
        @SerializedName("table")
        String table = "user_details";

        @SerializedName("$set")
        $Set set;

        @SerializedName("where")
        Where where;
    }

    class $Set{
        @SerializedName("name")
        String name;

        @SerializedName("status")
        String status;

        @SerializedName("file_id")
        String file_id;

    }

    class Where{
        @SerializedName("user_id")
        Integer userId;
    }

    public UpdateQuery(UserDetails userDetails){
        args = new Args();
        args.set = new $Set();
        args.set.name = userDetails.getName();
        args.set.status = userDetails.getStatus();
        args.set.file_id = userDetails.getFileId();
        args.where = new Where();
        args.where.userId = userDetails.getId();
    }
}
