package com.example.android.signuphasura;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by amogh on 14/6/17.
 */

public class HasuraHomePage extends Fragment {

    View parentViewHolder;

    public static final String TITLE = "Home";
    public static final String TAG = TITLE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentViewHolder = inflater.inflate(R.layout.hasura_home_page, container, false);
        return parentViewHolder;
    }
}
