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

import io.hasura.sdk.HasuraException;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.responseListener.AuthResponseListener;

/**
 * Created by amogh on 13/6/17.
 */

public class RegularLogin extends Fragment {

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
        parentViewHolder = inflater.inflate(R.layout.login_regular,container,false);

        username = (EditText) parentViewHolder.findViewById(R.id.login_username);
        password = (EditText) parentViewHolder.findViewById(R.id.login_password);
        button = (Button) parentViewHolder.findViewById(R.id.regular_login_button);

        final HasuraUser user = new HasuraUser();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());

                user.login(new AuthResponseListener() {
                    @Override
                    public void onSuccess(HasuraUser hasuraUser) {
                        Intent i = new Intent(getActivity().getApplicationContext(),SampleActivity.class);
                        startActivity(i);
                        //getActivity().finish();
                        Toast.makeText(getActivity().getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
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
