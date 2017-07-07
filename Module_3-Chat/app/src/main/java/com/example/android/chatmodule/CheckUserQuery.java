package com.example.android.chatmodule;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amogh on 16/6/17.
 */

public class CheckUserQuery {
    @SerializedName("type")
    String type = "select";

    @SerializedName("args")
    Args args;

    class Args{
        @SerializedName("table")
        String table = "user_details";

        @SerializedName("columns")
        String[] columns = {"name","status","user_id","file_id","mobile"};

        @SerializedName("where")
        Where where;
    }

    class Where{
        @SerializedName("mobile")
        String mobile;
    }

    public CheckUserQuery(String mobile){
        args = new Args();
        args.where = new Where();
        args.where.mobile = mobile;
    }
}
