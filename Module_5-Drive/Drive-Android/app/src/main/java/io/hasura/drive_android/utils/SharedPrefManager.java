package io.hasura.drive_android.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static SharedPrefManager instance;
    private static Context context;

    public static void initialize(Context context) {
        if (instance == null)
            instance =  new SharedPrefManager(context);
    }

    public SharedPrefManager(Context context) {
        SharedPrefManager.context = context;
    }

    public static SharedPreferences getPref(String prefName) {
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }
}