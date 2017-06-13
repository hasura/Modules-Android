package com.example.android.signuphasura;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.hasura.sdk.auth.HasuraUser;

/**
 * Created by amogh on 25/5/17.
 */

public class SocialAuthSignup extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        HasuraUser user = new HasuraUser();

        //user.socialLogin(GOOGLE,,new );
    }
}
