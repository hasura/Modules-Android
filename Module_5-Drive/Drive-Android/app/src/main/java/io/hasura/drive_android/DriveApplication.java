package io.hasura.drive_android;

import android.app.Application;

import io.hasura.drive_android.hasura.Hasura;
import io.hasura.drive_android.utils.SharedPrefManager;
import io.hasura.drive_android.utils.UriPathProvider;

/**
 * Created by jaison on 30/03/17.
 */

public class DriveApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPrefManager.initialize(this);
        Hasura.initialise();
        UriPathProvider.getInstance().setContext(this);
    }
}
