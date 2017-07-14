package com.example.android.registrationhasura;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amogh on 5/6/17.
 */
public class UserDetails {
    @SerializedName("name")
    String name;

    @SerializedName("status")
    String status;

    @SerializedName("user_id")
    int user_id;

    @SerializedName("file_id")
    String fileId;

    public void setName(String name){
        this.name = name;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public void setId(int id){
        this.user_id = id;
    }

    public void setFileId(String fileId){
        this.fileId = fileId;
    }

    public String getName(){
        return name;
    }

    public String getStatus(){
        return status;
    }

    public int getId(){
        return user_id;
    }

    public String getFileId(){
        return fileId;
    }

    public UserDetails(){

    }
}
