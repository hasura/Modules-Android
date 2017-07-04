package io.hasura.drive_android.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by amogh on 3/7/17.
 */

public class FolderResponse {
    @SerializedName("affected_rows")
    int affectedRows;

    @SerializedName("returning")
    List<HasuraFolder> folder;

    public HasuraFolder getFolder(){
        return folder.get(0);
    }

    public int getAffectedRows(){
        return this.affectedRows;
    }
}
