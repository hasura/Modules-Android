package io.hasura.drive_android.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by jaison on 10/04/17.
 */

public class UriPathProvider {

    private static UriPathProvider instance;
    private Context context;

    public static UriPathProvider getInstance() {
        if (instance == null) {
            instance = new UriPathProvider();
        }
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }
}
