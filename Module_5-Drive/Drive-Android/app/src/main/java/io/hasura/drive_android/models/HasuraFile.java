package io.hasura.drive_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import io.hasura.drive_android.hasura.Endpoint;
import io.hasura.drive_android.utils.DateManager;

/**
 * Created by jaison on 29/03/17.
 */

public class HasuraFile implements Parcelable {
    private String id;
    private int user_id;
    private String name;
    private String created;
    private String last_modified;
    private String file_number;
    private String file_expiry;
    private int folder_id;

    public HasuraFile() {

    }

    public HasuraFile(io.hasura.sdk.model.response.FileUploadResponse response, int folder_id) {
        this.id = response.getFile_id();
        this.user_id = response.getUser_id();
        this.name = response.getFile_id();
        this.created = response.getCreated_at();
        this.last_modified = response.getCreated_at();
        this.folder_id = folder_id;
    }

    public int getFolder_id() {
        return folder_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setLast_modified(String last_modified) {
        this.last_modified = last_modified;
    }

    public void setFile_number(String file_number) {
        this.file_number = file_number;
    }

    public void setFile_expiry(String file_expiry) {
        this.file_expiry = file_expiry;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreated() {
        return created;
    }

    public String getLast_modifiedString() {
        return "Modified at " + DateManager.getFormattedModifiedData(last_modified);
    }

    public String getLast_modified() {
        return last_modified;
    }

    public String getFile_number() {
        return file_number;
    }

    public String getFile_expiry() {
        return file_expiry;
    }

    public String getImageString() {
        return Endpoint.GET_FILE + id;
    }


    public Boolean isSame(HasuraFile file) {
        if (!file.id.equals(id))
            return false;
        if (!file.name.equals(name))
            return false;

        String selfPassExp = file_expiry != null ? file_expiry : "";
        String filePassExp = file.file_expiry != null ? file.file_expiry : "";

        if (!selfPassExp.equals(filePassExp))
            return false;

        if (file.file_number == null)
            file.file_number = "";
        if (file_number == null)
            file_number = "";
        if (!file.file_number.equals(file_number))
            return false;

        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.user_id);
        dest.writeString(this.name);
        dest.writeString(this.created);
        dest.writeString(this.last_modified);
        dest.writeString(this.file_number);
        dest.writeString(this.file_expiry);
        dest.writeInt(this.folder_id);
    }

    protected HasuraFile(Parcel in) {
        this.id = in.readString();
        this.user_id = in.readInt();
        this.name = in.readString();
        this.created = in.readString();
        this.last_modified = in.readString();
        this.file_number = in.readString();
        this.file_expiry = in.readString();
        this.folder_id = in.readInt();
    }

    public static final Creator<HasuraFile> CREATOR = new Creator<HasuraFile>() {
        @Override
        public HasuraFile createFromParcel(Parcel source) {
            return new HasuraFile(source);
        }

        @Override
        public HasuraFile[] newArray(int size) {
            return new HasuraFile[size];
        }
    };
}

