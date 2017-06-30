package com.example.android.inventoryapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by amogh on 28/6/17.
 */

public class CategoryModel {
    @SerializedName("category")
    String category;

    @SerializedName("items")
    List<ItemModel> itemList;

    public void setCategory(String category){
        this.category = category;
    }

    public void setItemList(List<ItemModel> itemList){
        this.itemList = itemList;
    }

    public String getCategory(){
        return this.category;
    }

    public List<ItemModel> getItemList(){
        return this.itemList;
    }

    public CategoryModel(){

    }
}
