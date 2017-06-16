package com.example.android.signuphasura;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.hasura.sdk.HasuraException;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.responseListener.AuthResponseListener;
import io.hasura.sdk.responseListener.OtpStatusListener;

/**
 * Created by amogh on 13/6/17.
 */

public class OtpLogin extends Fragment {

    EditText mobile;
    EditText username;
    Button button;
    View parentViewHolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        parentViewHolder = inflater.inflate(R.layout.mobile_login_activity,container,false);

        final HasuraUser user = new HasuraUser();

        username = (EditText) parentViewHolder.findViewById(R.id.otp_username);
        mobile = (EditText) parentViewHolder.findViewById(R.id.otp_mobile);
        button = (Button) parentViewHolder.findViewById(R.id.otp_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                user.setMobile(mobile.getText().toString());
                user.setUsername(username.getText().toString());

                /*
                    To use OTP login, you must first enable OTP login.
                 */
                user.enableMobileOtpLogin();

                /*
                    To login using OTP, you ask HasuraAuth to send an OTP to your mobile.
                 */

                /*
                    Currently there is some issue with the sendOtpToMobile API, the otp gets sent,
                    but the response is a 401.This will be fixed soon.

                    So for the time being, to use OTP Login,write your code in the onFailure() method of sendOtpToMobile()
                 */
                user.sendOtpToMobile(new OtpStatusListener() {
                    @Override
                    public void onSuccess() {

                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity().getApplicationContext());
                        alert.setMessage("Enter the OTP you received");
                        final EditText otp = new EditText(getActivity().getApplicationContext());
                        alert.setView(otp);
                        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                /*
                                    Once you get the OTP, you have to enter that OTP and call otpLogin() to login.
                                 */
                                user.otpLogin(otp.getText().toString(), new AuthResponseListener() {
                                    @Override
                                    public void onSuccess(HasuraUser hasuraUser) {
                                        Intent i = new Intent(getActivity().getApplicationContext(),SampleActivity.class);
                                        startActivity(i);
                                        getActivity().finish();
                                    }

                                    @Override
                                    public void onFailure(HasuraException e) {
                                        Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
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
                                        Intent i = new Intent(getActivity().getApplicationContext(),SampleActivity.class);
                                        startActivity(i);
                                        //getActivity().finish();
                                    }

                                    @Override
                                    public void onFailure(HasuraException e) {
                                        Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        alert.show();
                    }

                });
            }
        });
        return parentViewHolder;
    }
}
