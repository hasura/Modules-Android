package io.hasura.drive_android.models.hasuraQueries;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jaison on 23/01/17.
 */

public class SelectFolderQuery {

    @SerializedName("type")
    String type = "select";

    @SerializedName("args")
    Args args = new Args();

    class Args {

        @SerializedName("table")
        String table = "folder";

        @SerializedName("columns")
        String[] columns = {
                "id","name"
        };
    }

}
