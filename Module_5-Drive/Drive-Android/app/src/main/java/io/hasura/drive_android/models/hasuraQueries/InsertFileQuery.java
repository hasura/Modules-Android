package io.hasura.drive_android.models.hasuraQueries;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.hasura.drive_android.models.HasuraFile;

/**
 * Created by jaison on 23/01/17.
 */

public class InsertFileQuery {

    @SerializedName("type")
    String type = "insert";

    @SerializedName("args")
    Args args;

    public InsertFileQuery(io.hasura.sdk.model.response.FileUploadResponse response, int folderId) {
        args = new Args();
        args.objects = new ArrayList<>();
        HasuraFile file = new HasuraFile(response,folderId);
        args.objects.add(file);
    }

    class Args {

        @SerializedName("table")
        String table = "file";

        @SerializedName("returning")
        String[] returning = {
                "id","name","created","last_modified","file_number","file_expiry","folder_id"
        };

        @SerializedName("objects")
        List<HasuraFile> objects;
    }

}
