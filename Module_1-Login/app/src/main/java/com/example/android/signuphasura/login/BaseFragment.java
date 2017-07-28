package com.example.android.signuphasura.login;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraUser;

/**
 * Created by jaison on 28/07/17.
 */

public class BaseFragment extends Fragment {

    final HasuraUser user = Hasura.getClient().getUser();
    ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(getContext());
    }

    public void showProgressDialog(String message) {
        dialog.setMessage(message);
        dialog.show();
    }

    public void hideProgressDialog() {
        dialog.dismiss();
    }

    public void showLongToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

}
