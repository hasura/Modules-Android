package com.example.android.signuphasura;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.hasura.sdk.auth.HasuraUser;
import io.hasura.sdk.auth.responseListener.AuthResponseListener;
import io.hasura.sdk.auth.responseListener.MobileConfirmationResponseListener;
import io.hasura.sdk.core.HasuraException;

/**
 * Created by amogh on 25/5/17.
 */

public class MobileSignup extends AppCompatActivity{

    EditText username;
    EditText mobile;
    EditText password;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_signup_activity);

        username = (EditText) findViewById(R.id.mobile_username);
        mobile = (EditText) findViewById(R.id.mobile_mobile);
        password = (EditText) findViewById(R.id.mobile_password);
        button = (Button) findViewById(R.id.mobile_button);

        final HasuraUser user = new HasuraUser();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());
                user.setMobile(mobile.getText().toString());

                user.signUp(new AuthResponseListener() {
                    @Override
                    public void onSuccess(HasuraUser hasuraUser) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                        final EditText otp = new EditText(v.getContext());
                        alert.setMessage("Enter the otp you received");
                        alert.setView(otp);
                        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                user.confirmMobile(otp.getText().toString(), new MobileConfirmationResponseListener() {
                                    @Override
                                    public void onSuccess() {
                                        Intent i = new Intent(v.getContext(),AuthenticationActivity.class);
                                        startActivity(i);
                                        finish();
                                        Toast.makeText(v.getContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(HasuraException e) {
                                        Toast.makeText(v.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        alert.show();
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        Toast.makeText(v.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}
