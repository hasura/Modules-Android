package com.example.android.signuphasura;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import io.hasura.sdk.auth.HasuraUser;
import io.hasura.sdk.auth.responseListener.AuthResponseListener;
import io.hasura.sdk.core.HasuraException;

import static io.hasura.sdk.auth.HasuraSocialLoginType.FACEBOOK;

/**
 * Created by amogh on 12/6/17.
 */

public class FacebookLogin extends AppCompatActivity {

    LoginButton loginButton;
    CallbackManager callbackmanager;

    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data){
        super.onActivityResult(requestcode,resultcode,data);
        callbackmanager.onActivityResult(requestcode,resultcode,data);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facebook_login);

        final HasuraUser user = new HasuraUser();

        callbackmanager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.facebook_login_button);

        loginButton.setReadPermissions("email");

        LoginManager.getInstance().registerCallback(callbackmanager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                user.socialLogin(FACEBOOK, loginResult.getAccessToken().getToken(), new AuthResponseListener() {
                    @Override
                    public void onSuccess(HasuraUser hasuraUser) {
                        Toast.makeText(FacebookLogin.this, "Login successful", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        Toast.makeText(FacebookLogin.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(FacebookLogin.this, "Successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(FacebookLogin.this, "Cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(FacebookLogin.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
