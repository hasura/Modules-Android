package io.hasura.drive_android.models;

import android.net.Uri;

/**
 * Created by jaison on 29/03/17.
 */

public class UploadingFile {
    private String name;
    private Uri imageUri;
    private int folderId;

    public int getFolderId() {
        return folderId;
    }

    public UploadingFile(String name, Uri imageUri, int folderId) {
        this.name = name;
        this.imageUri = imageUri;
        this.folderId = folderId;

    }

    public String getName() {
        return name;
    }

    public Uri getImageUri() {
        return imageUri;
    }
}
