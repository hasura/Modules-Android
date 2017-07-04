package io.hasura.drive_android;

import android.app.Application;

import io.hasura.drive_android.utils.SharedPrefManager;
import io.hasura.drive_android.utils.UriPathProvider;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.ProjectConfig;

/**
 * Created by jaison on 30/03/17.
 */

public class DriveApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPrefManager.initialize(this);
        UriPathProvider.getInstance().setContext(this);
        Hasura.setProjectConfig(new ProjectConfig.Builder()
                .setProjectName("hello70")
                .build())
                .enableLogs()
                .initialise(this);
    }
}
