package com.example.android.signuphasura;

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

public class Register extends Fragment {

    EditText username;
    EditText password;
    Button button;
    View parentViewHolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        parentViewHolder = inflater.inflate(R.layout.register_activity,container,false);

        final HasuraUser user = Hasura.getClient().getUser();

        username = (EditText) parentViewHolder.findViewById(R.id.register_username);
        password = (EditText) parentViewHolder.findViewById(R.id.register_password);
        button = (Button) parentViewHolder.findViewById(R.id.register_button);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());

                user.signUp(new SignUpResponseListener() {

                    @Override
                    public void onSuccessAwaitingVerification(HasuraUser hasuraUser) {

                    }

                    @Override
                    public void onSuccess(HasuraUser hasuraUser) {
                        Toast.makeText(getActivity(), "Registration Successful", Toast.LENGTH_SHORT).show();
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
