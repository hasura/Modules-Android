package com.example.android.signuphasura;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseListener.SignUpResponseListener;

/**
 * Created by amogh on 25/5/17.
 */

public class EmailSignup extends Fragment {

    EditText username;
    EditText email;
    EditText password;
    Button button;
    View parentViewHolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        parentViewHolder = inflater.inflate(R.layout.email_signup_activity,container,false);

        username = (EditText) parentViewHolder.findViewById(R.id.email_username);
        email = (EditText) parentViewHolder.findViewById(R.id.email_email);
        password = (EditText) parentViewHolder.findViewById(R.id.email_password);
        button = (Button) parentViewHolder.findViewById(R.id.email_button);


        final HasuraUser user = Hasura.getClient().getUser();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());
                user.setEmail(email.getText().toString());


                /*
                    Using email verification , Hasura Auth will send an email to the given email-id.
                    The email will contain a link with a token which the user has to click to verify his email.
                    Once done, he can then login.
                 */

                user.signUp(new SignUpResponseListener() {
                    @Override
                    public void onSuccessAwaitingVerification(HasuraUser hasuraUser) {

                    }

                    @Override
                    public void onSuccess(HasuraUser hasuraUser) {
                        Toast.makeText(v.getContext(), "Email sent. Please verify and then login", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(v.getContext(),MainActivity.class);
                        startActivity(i);
                        getActivity().finish();
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
