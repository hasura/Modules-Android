package com.example.android.registrationhasura;

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

import io.hasura.sdk.auth.HasuraUser;
import io.hasura.sdk.auth.responseListener.AuthResponseListener;
import io.hasura.sdk.auth.responseListener.MobileConfirmationResponseListener;
import io.hasura.sdk.core.HasuraException;


/**
 * Created by amogh on 1/6/17.
 */

public class SignUpFragment extends Fragment {

    EditText username;
    EditText mobile;
    Button signup;
    View parentViewHolder;

    public SignUpFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        parentViewHolder = inflater.inflate(R.layout.fragment_signup,container,false);

        username = (EditText) parentViewHolder.findViewById(R.id.signup_username);
        mobile = (EditText) parentViewHolder.findViewById(R.id.signup_mobile);
        signup = (Button) parentViewHolder.findViewById(R.id.signup_button);
        mobile.setFocusable(true);
        mobile.requestFocus();

        final HasuraUser user = new HasuraUser();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                user.setUsername(username.getText().toString());
                user.setMobile(mobile.getText().toString());

                user.enableMobileOtpLogin();

                user.signUp(new AuthResponseListener() {
                    @Override
                    public void onSuccess(HasuraUser hasuraUser) {
                        final AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                        alert.setMessage("Enter the otp you received");
                        final EditText otp = new EditText(v.getContext());
                        alert.setView(otp);
                        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                user.confirmMobile(otp.getText().toString(), new MobileConfirmationResponseListener() {
                                    @Override
                                    public void onSuccess() {
                                        Intent i = new Intent(getActivity().getApplicationContext(),Profile.class);
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
                        Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return parentViewHolder;
    }
}
