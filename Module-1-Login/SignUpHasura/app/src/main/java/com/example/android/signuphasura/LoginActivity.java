package com.example.android.signuphasura;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.hasura.sdk.auth.HasuraUser;
import io.hasura.sdk.auth.responseListener.AuthResponseListener;
import io.hasura.sdk.core.HasuraException;

/**
 * Created by amogh on 25/5/17.
 */

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button button;
    Button facebook;
    Button google;
    Button otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        final HasuraUser user = new HasuraUser();

        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);
        button = (Button) findViewById(R.id.login_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());

                user.login(new AuthResponseListener() {
                    @Override
                    public void onSuccess(HasuraUser hasuraUser) {
                        Intent i = new Intent(v.getContext(),SampleActivity.class);
                        startActivity(i);
                        finish();
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        facebook = (Button) findViewById(R.id.facebook);
        google = (Button) findViewById(R.id.google);
        otp = (Button) findViewById(R.id.login_otp_button);

        otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,OtpLogin.class);
                startActivity(i);
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,FacebookLogin.class);
                startActivity(i);
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,GoogleLogin.class);
                startActivity(i);
            }
        });
    }

}
