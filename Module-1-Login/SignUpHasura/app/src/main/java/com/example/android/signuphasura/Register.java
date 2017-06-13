package com.example.android.signuphasura;

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

public class Register extends AppCompatActivity {

    EditText username;
    EditText password;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        final HasuraUser user = new HasuraUser();

        username = (EditText) findViewById(R.id.register_username);
        password = (EditText) findViewById(R.id.register_password);
        button = (Button) findViewById(R.id.register_button);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());
                user.signUp(new AuthResponseListener() {
                    @Override
                    public void onSuccess(HasuraUser hasuraUser) {
                        Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        Toast.makeText(Register.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
