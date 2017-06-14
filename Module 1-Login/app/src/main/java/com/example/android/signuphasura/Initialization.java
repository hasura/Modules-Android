package com.example.android.signuphasura;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import io.hasura.sdk.Hasura;

/**
 * Created by amogh on 25/5/17.
 */

public class Initialization extends Application {

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
        Hasura.setProjectName("hello70")
                .enableLogs()
                .initialise(this);
    }
}
