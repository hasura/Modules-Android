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
import io.hasura.sdk.auth.responseListener.OtpStatusListener;
import io.hasura.sdk.core.HasuraException;

/**
 * Created by amogh on 13/6/17.
 */

public class OtpLogin extends AppCompatActivity {

    EditText mobile;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_login_activity);

        final HasuraUser user = new HasuraUser();

        mobile = (EditText) findViewById(R.id.otp_mobile);
        button = (Button) findViewById(R.id.otp_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                user.setMobile(mobile.getText().toString());

                user.enableMobileOtpLogin();

                user.sendOtpToMobile(new OtpStatusListener() {
                    @Override
                    public void onSuccess() {

                        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                        alert.setMessage("Enter the OTP you received");
                        final EditText otp = new EditText(v.getContext());
                        alert.setView(otp);
                        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                user.otpLogin(otp.getText().toString(), new AuthResponseListener() {
                                    @Override
                                    public void onSuccess(HasuraUser hasuraUser) {
                                        Intent i = new Intent(v.getContext(),SampleActivity.class);
                                        startActivity(i);
                                        finish();
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
                        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                        alert.setMessage("Enter the OTP you received");
                        final EditText otp = new EditText(v.getContext());
                        alert.setView(otp);
                        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                user.otpLogin(otp.getText().toString(), new AuthResponseListener() {
                                    @Override
                                    public void onSuccess(HasuraUser hasuraUser) {
                                        Intent i = new Intent(v.getContext(),SampleActivity.class);
                                        startActivity(i);
                                        finish();
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
                });
            }
        });
    }
}
