
package io.hasura.drive_android.models.hasuraQueries;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.hasura.drive_android.models.HasuraFile;

/**
 * Created by jaison on 23/01/17.
 */

public class FileReturningResponse {

    @SerializedName("affected_rows")
    Integer affectedRows;

    @SerializedName("returning")
    List<HasuraFile> files;

    public Integer getAffectedRows() {
        return affectedRows;
    }

    public List<HasuraFile> getFiles() {
        return files;
    }
}
