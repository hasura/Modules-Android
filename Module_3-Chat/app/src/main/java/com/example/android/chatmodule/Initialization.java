package com.example.android.chatmodule;

import android.app.Application;

import io.hasura.sdk.Hasura;
import io.hasura.sdk.ProjectConfig;

/**
 * Created by amogh on 4/7/17.
 */

public class Initialization extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Hasura.setProjectConfig(new ProjectConfig.Builder()
                .setProjectName("hello70")
                .build())
                .enableLogs()
                .initialise(this);
    }
}
