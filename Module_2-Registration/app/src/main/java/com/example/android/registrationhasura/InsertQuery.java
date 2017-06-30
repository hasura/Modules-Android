package com.example.android.registrationhasura;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amogh on 19/6/17.
 */

public class InsertQuery {
    @SerializedName("type")
    String type = "insert";

    @SerializedName("args")
    Args args;

    class Args{
        @SerializedName("table")
        String table = "user_details";

        @SerializedName("objects")
        List<UserDetails> objects;
    }

    public InsertQuery(UserDetails userDetails){
        args = new Args();
        args.objects = new ArrayList<>();
        args.objects.add(userDetails);
    }
}
