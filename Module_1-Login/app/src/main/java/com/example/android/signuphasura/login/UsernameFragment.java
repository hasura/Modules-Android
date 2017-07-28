package com.example.android.signuphasura.login;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.signuphasura.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseListener.AuthResponseListener;
import io.hasura.sdk.responseListener.SignUpResponseListener;

/**
 * Created by amogh on 13/6/17.
 */

public class UsernameFragment extends BaseFragment {

    public static final String TITLE = "Username Login";
    public static final String TAG = TITLE;

    @Bind(R.id.username)
    TextInputEditText username;
    @Bind(R.id.til_username)
    TextInputLayout tilUsername;
    @Bind(R.id.password)
    TextInputEditText password;
    @Bind(R.id.til_password)
    TextInputLayout tilPassword;
    @Bind(R.id.signUp_button)
    Button signUpButton;
    @Bind(R.id.login_button)
    Button loginButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_username, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.signUp_button, R.id.login_button})
    public void onViewClicked(View view) {
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());

        switch (view.getId()) {
            case R.id.signUp_button:
                showProgressDialog("Signing Up");
                user.signUp(new SignUpResponseListener() {
                    @Override
                    public void onSuccessAwaitingVerification(HasuraUser hasuraUser) {
                        hideProgressDialog();
                        Toast.makeText(getActivity().getApplicationContext(), "SignUp successful - Awaiting Verification", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(HasuraUser hasuraUser) {
                        hideProgressDialog();
                        Toast.makeText(getActivity().getApplicationContext(), "SignUp successful", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        hideProgressDialog();
                        Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.login_button:
                showProgressDialog("Logging In");
                user.login(new AuthResponseListener() {
                    @Override
                    public void onSuccess(String s) {
                        hideProgressDialog();
                        Toast.makeText(getActivity().getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        hideProgressDialog();
                        Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }

                });
                break;
        }
    }
}

