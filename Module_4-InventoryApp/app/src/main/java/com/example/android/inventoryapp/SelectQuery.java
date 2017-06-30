package com.example.android.inventoryapp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amogh on 28/6/17.
 */

public class SelectQuery {
    @SerializedName("type")
    String type = "select";

    @SerializedName("args")
    Args args;

    class Args{
        @SerializedName("table")
        String table = "inventory_items";

        @SerializedName("columns")
        String[] columns = {"name","price","description","category","id","file_id"};

        @SerializedName("where")
        Where where;
    }

    class Where{
        @SerializedName("category")
        String category;
    }

    public SelectQuery(String category){
        args = new Args();
        args.where = new Where();
        args.where.category = category;
    }
}
