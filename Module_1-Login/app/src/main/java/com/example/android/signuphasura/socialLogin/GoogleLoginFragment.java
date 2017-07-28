package com.example.android.signuphasura.socialLogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.signuphasura.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseListener.AuthResponseListener;

import static io.hasura.sdk.HasuraSocialLoginType.GOOGLE;

/**
 * Created by amogh on 12/6/17.
 */

public class GoogleLoginFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    public static final String TITLE = "Google Login";
    public static final String TAG = TITLE;

    SignInButton signInButton;
    GoogleApiClient googleApiClient;
    View parentViewHolder;

    private final static int CODE = 05;
    HasuraUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        parentViewHolder = inflater.inflate(R.layout.google_login,container,false);

        user = Hasura.getClient().getUser();

        signInButton = (SignInButton) parentViewHolder.findViewById(R.id.google_login_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        /*
            You ask Google to send you an IdToken that will be sent to Hasura Auth to verify.
         */

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))  //request IdToken
                .build();

        googleApiClient = new GoogleApiClient.Builder(getActivity().getApplicationContext())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent,CODE);
            }
        });
        return parentViewHolder;
    }

    @Override
    public void onActivityResult(int requestcode,int resultcode,Intent data){
        super.onActivityResult(requestcode,resultcode,data);
        if(requestcode == CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){

            /*
                Retrieve the IdToken and send it to HasuraAuth in the socialLogin request.
             */

            user.socialLogin(GOOGLE, result.getSignInAccount().getIdToken(), new AuthResponseListener() {
                @Override
                public void onSuccess(String s) {
                    Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(HasuraException e) {
                    Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }

            });
        }else{

            Log.i(TAG, "SignIn failed: " + result.getStatus().toString());

            Toast.makeText(getActivity(), result.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
