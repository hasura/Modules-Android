package io.hasura.drive_android.models.hasuraQueries;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amogh on 3/7/17.
 */

public class InsertFolderQuery {
    @SerializedName("type")
    String type = "insert";

    @SerializedName("args")
    Args args;

    class Args{
        @SerializedName("table")
        String table = "folder";

        @SerializedName("objects")
        List<Entry> entry;

        @SerializedName("returning")
        String[] returning = {"id","name","user_id"};
    }

    class Entry{
        @SerializedName("name")
        String name;

        @SerializedName("user_id")
        int userId;
    }


    public InsertFolderQuery(String name,int userId) {
        args = new Args();
        args.entry = new ArrayList<>();
        Entry folder = new Entry();
        folder.name = name;
        folder.userId = userId;
        args.entry.add(folder);
    }
}
