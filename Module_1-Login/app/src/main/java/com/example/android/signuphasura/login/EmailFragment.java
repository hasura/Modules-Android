package com.example.android.signuphasura.login;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.signuphasura.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseListener.AuthResponseListener;
import io.hasura.sdk.responseListener.SignUpResponseListener;

/**
 * Created by amogh on 25/5/17.
 */

public class EmailFragment extends BaseFragment {

    public static final String TITLE = "Email Login";
    public static final String TAG = TITLE;

    @Bind(R.id.email)
    TextInputEditText email;
    @Bind(R.id.til_email)
    TextInputLayout tilEmail;
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
        View view = inflater.inflate(R.layout.fragment_email, container, false);
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
        user.setPassword(password.getText().toString());
        user.setEmail(email.getText().toString());

        //Set username as the email - since it is going to be unique
        user.setUsername(email.getText().toString());

        switch (view.getId()) {
            case R.id.signUp_button:
                showProgressDialog("Signing Up");
                user.signUp(new SignUpResponseListener() {
                    @Override
                    public void onSuccessAwaitingVerification(HasuraUser hasuraUser) {
                        /*If email verification is enabled on the console, then this method will get called and the user will
                         then have to verify his email id by clicking on the link sent to his email address. After verification is complete,
                         the user can then login.
                        */
                        hideProgressDialog();
                        Toast.makeText(getContext(), "Email verification sent. Please verify and then login", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(HasuraUser hasuraUser) {
                        hideProgressDialog();
                        Toast.makeText(getContext(), "SignUp Successful", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        hideProgressDialog();
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                    }

                });
                break;
            case R.id.login_button:
                /*
                If email verification is enabled then Login will only work if the email has been verified.
                * */
                showProgressDialog("Logging in");
                user.login(new AuthResponseListener() {
                    @Override
                    public void onSuccess(String s) {
                        hideProgressDialog();
                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        hideProgressDialog();
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
}
