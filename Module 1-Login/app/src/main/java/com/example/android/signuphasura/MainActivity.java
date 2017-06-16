package com.example.android.signuphasura;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by amogh on 14/6/17.
 */

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private Toolbar toolbar;

    public static int navItemIndex = 0;

    private String[] activityTitles;

    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler handler;

    private static final String TAG_HOME = "Home";
    private static final String TAG_REGULAR_SIGNUP = "SignUp";
    private static final String TAG_EMAIL_SIGNUP = "Email-SignUp";
    private static final String TAG_MOBILE_SIGNUP = "Mobile-SignUp";
    private static final String TAG_REGULAR_LOGIN = "Login";
    private static final String TAG_MOBILE_OTP_LOGIN = "Mobile-OTP-Login";
    private static final String TAG_FACEBOOK_LOGIN = "Facebook-Login";
    private static final String TAG_GOOGLE_LOGIN = "Google-Login";
    private static final String TAG_LINKEDIN_LOGIN = "LinkedIn-Login";
    private static final String TAG_GITHUB_LOGIN = "Github-Login";

    public static String CURRENT_TAG = TAG_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navHeader = navigationView.getHeaderView(0);

        activityTitles = new String[]{"Home", "Register", "Mobile-SignUp","Email-SignUp","Login","OTP-Login","Facebook-Login","Google-Login","LinkedIn-Login","Github-Login"};

        loadNavHeader();

        setUpNavigationView();

        if(savedInstanceState == null){
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    private void loadNavHeader(){

    }

    private void loadHomeFragment(){

        selectNavMenu();

        setToolbarTitle();

        //If open selecting again closes nav drawer.
        if(getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null){
            drawer.closeDrawers();
            return;
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame,fragment,CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if(runnable != null){
            handler.post(runnable);
        }

        drawer.closeDrawers();

        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment(){
        switch (navItemIndex){
            case 0:
                return new HasuraHomePage();
            case 1:
                return new Register();
            case 2:
                return new MobileSignup();
            case 3:
                return new EmailSignup();
            case 4:
                return new RegularLogin();
            case 5:
                return new OtpLogin();
            case 6:
                return new FacebookLogin();
            case 7:
                return new GoogleLogin();
            case 8:
                return new LinkedInLogin();
            case 9:
                return new GithubLogin();
            default:
                return new HasuraHomePage();
        }
    }

    private void setUpNavigationView(){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_1:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_2:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_REGULAR_SIGNUP;
                        break;
                    case R.id.nav_3:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_MOBILE_SIGNUP;
                        break;
                    case R.id.nav_4:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_EMAIL_SIGNUP;
                        break;
                    case R.id.nav_5:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_REGULAR_LOGIN;
                        break;
                    case R.id.nav_6:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_MOBILE_OTP_LOGIN;
                        break;
                    case R.id.nav_7:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_FACEBOOK_LOGIN;
                        break;
                    case R.id.nav_8:
                        navItemIndex = 7;
                        CURRENT_TAG = TAG_GOOGLE_LOGIN;
                        break;
                    case R.id.nav_9:
                        navItemIndex = 8;
                        CURRENT_TAG = TAG_LINKEDIN_LOGIN;
                        break;
                    case R.id.nav_10:
                        navItemIndex = 9;
                        CURRENT_TAG = TAG_GITHUB_LOGIN;
                        break;
                    default:
                        navItemIndex = 0;
                }


                loadHomeFragment();
                return true;
            }
        });
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void setToolbarTitle(){
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu(){
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawers();
            return;
        }

        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }


}

