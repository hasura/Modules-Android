package io.hasura.drive_android.models.hasuraQueries;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jaison on 23/01/17.
 */

public class SelectFileQuery {

    @SerializedName("type")
    String type = "select";

    @SerializedName("args")
    Args args;

    public SelectFileQuery(Integer userId) {
        args = new Args();
        args.where = new Where();
        args.where.userId = userId;
    }

    class Args {

        @SerializedName("table")
        String table = "file";

        @SerializedName("columns")
        String[] columns = {
                "id","name","created","last_modified","file_number","file_expiry","folder_id"
        };

        @SerializedName("where")
        Where where;

    }

    class Where {
        @SerializedName("user_id")
        Integer userId;
    }

}
