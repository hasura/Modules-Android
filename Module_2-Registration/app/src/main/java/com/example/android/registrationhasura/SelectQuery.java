package com.example.android.registrationhasura;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amogh on 16/6/17.
 */

public class SelectQuery {
    @SerializedName("type")
    String type = "select";

    @SerializedName("args")
    Args args;

    class Args{
        @SerializedName("table")
        String table = "user_details";

        @SerializedName("columns")
        String[] columns = {"name","status","user_id","file_id"};

        @SerializedName("where")
        Where where;
    }

    class Where{
        @SerializedName("user_id")
        Integer userId;
    }

    public SelectQuery(Integer userId){
        args = new Args();
        args.where = new Where();
        args.where.userId = userId;
    }
}
