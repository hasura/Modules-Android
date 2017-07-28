package com.example.android.signuphasura;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import io.hasura.sdk.Hasura;
import io.hasura.sdk.ProjectConfig;

/**
 * Created by amogh on 25/5/17.
 */

public class InitializationApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /*
            Initializing Facebook SDK for facebook(social) login.
         */
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        /*
            Initializing your Hasura Project.
         */

        Hasura.setProjectConfig(new ProjectConfig.Builder()
                .setProjectName("hello70")
                .build())
                .enableLogs()
                .initialise(this);
    }
}
