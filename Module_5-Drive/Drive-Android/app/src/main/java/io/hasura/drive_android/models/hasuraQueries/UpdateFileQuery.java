package io.hasura.drive_android.models.hasuraQueries;

import com.google.gson.annotations.SerializedName;

import io.hasura.drive_android.models.HasuraFile;

/**
 * Created by jaison on 23/01/17.
 */

public class UpdateFileQuery {

    @SerializedName("type")
    String type = "update";

    @SerializedName("args")
    Args args;

    public UpdateFileQuery(HasuraFile file, int userId) {
        args = new Args();
        args.where = new Where();
        args.where.id = file.getId();
        args.where.userId = userId;

        args.set = new Set();
        args.set.name = file.getName();
        args.set.lastModified = file.getLast_modified();
        args.set.passportNumber = file.getFile_number();
        args.set.passportExpiry = file.getFile_expiry();
    }

    class Args {

        @SerializedName("table")
        String table = "file";

        @SerializedName("where")
        Where where;

        @SerializedName("$set")
        Set set;

        @SerializedName("returning")
        String[]  returning = {
                "id","name","created","last_modified","file_number","file_expiry"
        };
    }

    class Where {
        @SerializedName("user_id")
        Integer userId;

        @SerializedName("id")
        String id;
    }

    class Set {
        @SerializedName("name")
        String name;

        @SerializedName("last_modified")
        String lastModified;

        @SerializedName("file_number")
        String passportNumber;

        @SerializedName("file_expiry")
        String passportExpiry;
    }
}
