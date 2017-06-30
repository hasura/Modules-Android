package com.example.android.inventoryapp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amogh on 28/6/17.
 */

public class CategoryResponse {
    @SerializedName("category_name")
    String category;

    public String getCategory(){
        return this.category;
    }
}
