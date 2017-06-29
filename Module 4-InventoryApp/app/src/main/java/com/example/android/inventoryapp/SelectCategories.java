package com.example.android.inventoryapp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amogh on 28/6/17.
 */

public class SelectCategories {
    @SerializedName("type")
    String type = "select";

    @SerializedName("args")
    Args args;

    class Args{
        @SerializedName("table")
        String table = "categories_list";

        @SerializedName("columns")
        String[] columns = {"category_name"};
    }

    public SelectCategories(){
        args = new Args();
    }
}
