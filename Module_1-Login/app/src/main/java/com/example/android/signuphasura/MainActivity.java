package com.example.android.signuphasura;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.example.android.signuphasura.login.EmailFragment;
import com.example.android.signuphasura.login.MobileFragment;
import com.example.android.signuphasura.login.UsernameFragment;
import com.example.android.signuphasura.socialLogin.FacebookLoginFragment;
import com.example.android.signuphasura.socialLogin.GithubLoginFragment;
import com.example.android.signuphasura.socialLogin.GoogleLoginFragment;
import com.example.android.signuphasura.socialLogin.LinkedInLoginFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by amogh on 14/6/17.
 */

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        //Set up toolbar
        setSupportActionBar(toolbar);

        setUpNavigationView();

        //Show home fragment by default
        if (savedInstanceState == null) {
            loadFragment(HasuraHomePage.TITLE, new HasuraHomePage(), HasuraHomePage.TAG, 0);
        }
    }

    private void loadFragment(String title, final Fragment fragment, final String fragmentTag, int currentSelectedItemId) {
        //deselect all nav items - this will uncheck the previously selected one as well
        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            SubMenu subMenu = navigationView.getMenu().getItem(i).getSubMenu();
            if (subMenu != null) {
                for (int j = 0; j < subMenu.size(); j++) {
                    subMenu.getItem(j).setChecked(false);
                }
            } else
                navigationView.getMenu().getItem(i).setChecked(false);
        }

        //select currently selected item

        navigationView.getMenu().findItem(currentSelectedItemId).setChecked(true);

        //set toolbar title
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);

        //If open selecting again closes nav drawerLayout.
        if (getSupportFragmentManager().findFragmentByTag(fragmentTag) != null) {
            drawerLayout.closeDrawers();
            return;
        }

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, fragment, fragmentTag)
                        .commitAllowingStateLoss();
            }
        });

        //closer drawer
        drawerLayout.closeDrawers();

        invalidateOptionsMenu();
    }

    private NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    loadFragment(HasuraHomePage.TITLE, new HasuraHomePage(), HasuraHomePage.TAG, R.id.home);
                    return true;
                case R.id.username:
                    loadFragment(UsernameFragment.TITLE, new UsernameFragment(), UsernameFragment.TAG, R.id.username);
                    return true;
                case R.id.email:
                    loadFragment(EmailFragment.TITLE, new EmailFragment(), EmailFragment.TAG, R.id.email);
                    return true;
                case R.id.mobile:
                    loadFragment(MobileFragment.TITLE, new MobileFragment(), MobileFragment.TAG, R.id.mobile);
                    return true;
                case R.id.facebook:
                    loadFragment(FacebookLoginFragment.TITLE, new FacebookLoginFragment(), FacebookLoginFragment.TAG, R.id.facebook);
                    return true;
                case R.id.google:
                    loadFragment(GoogleLoginFragment.TITLE, new GoogleLoginFragment(), GoogleLoginFragment.TAG, R.id.google);
                    return true;
                case R.id.linkedin:
                    loadFragment(LinkedInLoginFragment.TITLE, new LinkedInLoginFragment(), LinkedInLoginFragment.TAG, R.id.linkedin);
                    return true;
                case R.id.github:
                    loadFragment(GithubLoginFragment.TITLE, new GithubLoginFragment(), GithubLoginFragment.TAG, R.id.github);
                    return true;
                default:
                    loadFragment(HasuraHomePage.TITLE, new HasuraHomePage(), HasuraHomePage.TAG, R.id.home);
                    return true;
            }
        }
    };

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

}

