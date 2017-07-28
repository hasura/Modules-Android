package com.example.android.signuphasura.socialLogin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.android.signuphasura.R;
import com.example.android.signuphasura.SampleActivity;
import com.example.android.signuphasura.model.SocialLoginResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraUser;

/**
 * Created by amogh on 15/6/17.
 */

public class LinkedInLoginFragment extends Fragment{

    public static final String TITLE = "LinkedIn Login";
    public static final String TAG = TITLE;

    private static final String API_KEY = "8117x242llbwmy";
    private static final String STATE = "DgkRrHXmyu3KLd0KDdfq";
    private static final String REDIRECT_URI = "https://auth.hello70.hasura-app.io/linkedin/authenticate";

    private static final String AUTHORIZATION_URL = "https://www.linkedin.com/oauth/v2/authorization";
    private static final String RESPONSE_TYPE_PARAM = "response_type";
    private static final String RESPONSE_TYPE_VALUE = "code";
    private static final String CLIENT_ID_PARAM = "client_id";
    private static final String STATE_PARAM = "state";
    private static final String REDIRECT_URI_PARAM = "redirect_uri";
    private static final String SCOPE_PARAM = "scope";
    private static final String SCOPE_VALUE = "r_emailaddress%20r_basicprofile";

    private static final String QUESTION_MARK = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUALS = "=";

    private WebView webView;
    private ProgressDialog pd;

    HasuraUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.linkedin_login,container,false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        webView = (WebView) view.findViewById(R.id.webview_linkedin);
        webView.clearCache(true);
        webView.clearHistory();
        clearCookies(getContext());
        webView.requestFocus(View.FOCUS_DOWN);
        webView.getSettings().setJavaScriptEnabled(true);

        user = Hasura.getClient().getUser();

        webView.setWebChromeClient(new WebChromeClient(){
            public boolean onConsoleMessage(ConsoleMessage consoleMessage){
                Log.i("Console Message",consoleMessage.message());
                try {
                    SocialLoginResponse response = new Gson().fromJson(consoleMessage.message(),SocialLoginResponse.class);
                    handleLinkedInLoginResponse(response);
                }catch (JsonSyntaxException e){
                    e.printStackTrace();
                }
                return true;
            }
        });

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view,String url){
                if(pd != null && pd.isShowing()){
                    pd.dismiss();
                }

                view.loadUrl("javascript:console.log(document.body.getElementsByTagName('pre')[0].innerHTML);");
            }

        });

        String authUrl = getAuthorizationUrl();

        if(isNetworkAvailable(getContext())){
            pd = ProgressDialog.show(getContext(),"","Please Wait",true);
            webView.loadUrl(authUrl);
        }
    }

    private void handleLinkedInLoginResponse(final SocialLoginResponse loginResponse){
        webView.setVisibility(View.INVISIBLE);

        if(loginResponse.getAccessToken() != null) {
            Intent i = new Intent(getActivity().getApplicationContext(),SampleActivity.class);
            startActivity(i);
            //getActivity().finish();
        }
    }

    private static String getAuthorizationUrl() {
        return AUTHORIZATION_URL
                + QUESTION_MARK + RESPONSE_TYPE_PARAM + EQUALS + RESPONSE_TYPE_VALUE
                + AMPERSAND + CLIENT_ID_PARAM + EQUALS + API_KEY
                + AMPERSAND + STATE_PARAM + EQUALS + STATE
                + AMPERSAND + SCOPE_PARAM + EQUALS + SCOPE_VALUE
                + AMPERSAND + REDIRECT_URI_PARAM + EQUALS + REDIRECT_URI;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @SuppressWarnings("deprecation")
    public static void clearCookies(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Log.d("LinkedInWebView", "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            Log.d("LinkedInWebView", "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
