package io.hasura.drive_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jaison on 11/04/17.
 */

public class HasuraFolder implements Parcelable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    public HasuraFolder() {
    }

    protected HasuraFolder(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<HasuraFolder> CREATOR = new Parcelable.Creator<HasuraFolder>() {
        @Override
        public HasuraFolder createFromParcel(Parcel source) {
            return new HasuraFolder(source);
        }

        @Override
        public HasuraFolder[] newArray(int size) {
            return new HasuraFolder[size];
        }
    };
}
