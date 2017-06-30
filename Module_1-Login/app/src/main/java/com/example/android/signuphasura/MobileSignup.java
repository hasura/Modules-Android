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

import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseListener.MobileConfirmationResponseListener;
import io.hasura.sdk.responseListener.SignUpResponseListener;

/**
 * Created by amogh on 25/5/17.
 */

public class MobileSignup extends Fragment{

    EditText username;
    EditText mobile;
    EditText password;
    Button button;
    View parentViewHolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        parentViewHolder = inflater.inflate(R.layout.mobile_signup_activity,container,false);

        username = (EditText) parentViewHolder.findViewById(R.id.mobile_username);
        mobile = (EditText) parentViewHolder.findViewById(R.id.mobile_mobile);
        password = (EditText) parentViewHolder.findViewById(R.id.mobile_password);
        button = (Button) parentViewHolder.findViewById(R.id.mobile_button);

        final HasuraUser user = Hasura.getClient().getUser();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());
                user.setMobile(mobile.getText().toString());

                /*
                    When using signUp username is essential.

                    When you call signup(), HasuraAuth will send you an OTP to verify your mobile number.
                    The response will contain hasura_roles,hasura_id and auth_token, with auth_token set to null.
                 */

                user.otpSignUp(new SignUpResponseListener() {
                    @Override
                    public void onSuccessAwaitingVerification(HasuraUser hasuraUser) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                        final EditText otp = new EditText(v.getContext());
                        alert.setMessage("Enter the otp you received");
                        alert.setView(otp);
                        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                /*
                                    Once you receive the OTP, you have to send enter the OTP and call confirmMobile()
                                    to verify you number.
                                    Once done, you can now login.
                                 */
                                user.confirmMobile(otp.getText().toString(), new MobileConfirmationResponseListener() {

                                    @Override
                                    public void onSuccess(String s) {
                                        Intent i = new Intent(v.getContext(),MainActivity.class);
                                        startActivity(i);
                                        getActivity().finish();
                                        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(HasuraException e) {
                                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        alert.show();
                    }

                    @Override
                    public void onSuccess(HasuraUser hasuraUser) {

                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });

        return parentViewHolder;
    }
}
