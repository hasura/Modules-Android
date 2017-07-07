package com.example.android.chatmodule;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amogh on 6/7/17.
 */

public class Contact {
    @SerializedName("name")
    String name;

    @SerializedName("mobile")
    String mobile;

    public void setName(String name){
        this.name = name;
    }

    public void setMobile(String mobile){
        this.mobile = mobile;
    }

    public String getName(){
        return name;
    }

    public String getMobile(){
        return mobile;
    }
}
