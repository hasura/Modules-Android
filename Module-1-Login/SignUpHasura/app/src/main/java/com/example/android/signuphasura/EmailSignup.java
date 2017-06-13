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

public class EmailSignup extends AppCompatActivity {

    EditText username;
    EditText email;
    EditText password;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_signup_activity);

        username = (EditText) findViewById(R.id.email_username);
        email = (EditText) findViewById(R.id.email_email);
        password = (EditText) findViewById(R.id.email_password);
        button = (Button) findViewById(R.id.email_button);


        final HasuraUser user = new HasuraUser();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());
                user.setEmail(email.getText().toString());

                user.setMobile("8888401705");

                user.signUp(new AuthResponseListener() {
                    @Override
                    public void onSuccess(HasuraUser hasuraUser) {
                        Toast.makeText(EmailSignup.this, "Email sent. Please verify and then login", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(EmailSignup.this,AuthenticationActivity.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        Toast.makeText(EmailSignup.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

}
