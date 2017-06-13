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
 * Created by amogh on 13/6/17.
 */

public class RegularLogin extends AppCompatActivity {

    EditText username;
    EditText password;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_regular);

        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);
        button = (Button) findViewById(R.id.regular_login_button);

        final HasuraUser user = new HasuraUser();

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
                        Toast.makeText(RegularLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        Toast.makeText(RegularLogin.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
