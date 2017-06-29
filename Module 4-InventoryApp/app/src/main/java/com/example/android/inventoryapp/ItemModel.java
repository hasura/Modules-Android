package com.example.android.inventoryapp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amogh on 28/6/17.
 */

public class ItemModel {
    @SerializedName("name")
    String name;

    @SerializedName("price")
    int price;

    @SerializedName("description")
    String description;

    @SerializedName("category")
    String category;

    @SerializedName("id")
    int id;

    @SerializedName("file_id")
    String fileId;

    public void setName(String name){
        this.name = name;
    }

    public  void setPrice(int price){
        this.price = price;
    }

    public void setFileId(String fileId){
        this.fileId = fileId;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public int getPrice(){
        return this.price;
    }

    public String getFileId(){
        return this.fileId;
    }

    public String getCategory(){
        return this.category;
    }

    public int getId(){
        return this.id;
    }

    public ItemModel(){

    }
}
