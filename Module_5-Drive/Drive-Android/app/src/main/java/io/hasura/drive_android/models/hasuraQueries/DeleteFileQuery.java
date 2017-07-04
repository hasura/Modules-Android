package io.hasura.drive_android.models.hasuraQueries;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jaison on 23/01/17.
 */

public class DeleteFileQuery {

    @SerializedName("type")
    String type = "delete";

    @SerializedName("args")
    Args args;

    public DeleteFileQuery(String fileId, Integer userId) {
        args = new Args();
        args.where = new Where();
        args.where.id = fileId;
        args.where.userId = userId;
    }

    class Args {

        @SerializedName("table")
        String table = "file";

        @SerializedName("where")
        Where where;
    }

    class Where {
        @SerializedName("user_id")
        Integer userId;

        @SerializedName("id")
        String id;
    }
}
