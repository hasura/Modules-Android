package com.example.android.signuphasura.socialLogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.signuphasura.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseListener.AuthResponseListener;

import static io.hasura.sdk.HasuraSocialLoginType.FACEBOOK;

/**
 * Created by amogh on 12/6/17.
 */

public class FacebookLoginFragment extends Fragment {

    public static final String TITLE = "Facebook Login";
    public static final String TAG = TITLE;

    LoginButton loginButton;
    CallbackManager callbackmanager;
    View parentViewHolder;

    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data){
        super.onActivityResult(requestcode,resultcode,data);
        callbackmanager.onActivityResult(requestcode,resultcode,data);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        parentViewHolder = inflater.inflate(R.layout.facebook_login,container,false);

        final HasuraUser user = Hasura.getClient().getUser();

        callbackmanager = CallbackManager.Factory.create();
        loginButton = (LoginButton) parentViewHolder.findViewById(R.id.facebook_login_button);

        loginButton.setReadPermissions("email");
        loginButton.setFragment(this);

        LoginManager.getInstance().registerCallback(callbackmanager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                /*
                    Once you've used Facebook to login, he will give you an access token.
                    You should use this access token to Hasura Auth to log you in.
                    If the user is using Facebook login for the first time, Hasura Auth will
                    create a new user for him and then log him in, else there will be a normal login.
                 */
                user.socialLogin(FACEBOOK, loginResult.getAccessToken().getToken(), new AuthResponseListener() {
                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }


                });
                Toast.makeText(getActivity().getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity().getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        return parentViewHolder;
    }
}
