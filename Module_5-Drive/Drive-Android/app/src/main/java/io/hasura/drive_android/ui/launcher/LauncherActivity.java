package io.hasura.drive_android.ui.launcher;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hasura.drive_android.R;
import io.hasura.drive_android.ui.authentication.AuthenticationActivity;
import io.hasura.drive_android.ui.folderList.FolderListActivity;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.ProjectConfig;

/**
 * This is the starting point for the app. Checks for the users login status and navigates accordingly
 **/
public class LauncherActivity extends AppCompatActivity {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.signInButton)
    Button signInButton;
    @BindView(R.id.firstIndicator)
    ImageView firstIndicator;
    @BindView(R.id.secondIndicator)
    ImageView secondIndicator;
    @BindView(R.id.thirdIndicator)
    ImageView thirdIndicator;
    @BindView(R.id.nextButton)
    Button nextButton;
    @BindView(R.id.parent)
    LinearLayout parent;


    List<ImageView> pageIndicators;
    int currentPage = 0;
    private static final int LAST_PAGE_POSITION = 2;
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private Integer[] colors = null;
    private static final int ANIM_ITEM_DURATION = 500;

    public static void startActivity(Activity startingActivity) {
        Intent intent = new Intent(startingActivity, LauncherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        ButterKnife.bind(this);



        //Hiding the status bar
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        pageIndicators = new ArrayList<>();
        pageIndicators.add(firstIndicator);
        pageIndicators.add(secondIndicator);
        pageIndicators.add(thirdIndicator);

        Integer color1 = getResources().getColor(R.color.onBoardingPage1);
        Integer color2 = getResources().getColor(R.color.onBoardingPage2);
        Integer color3 = getResources().getColor(R.color.onBoardingPage3);
        Integer[] colors_temp = {color1, color2, color3};
        colors = colors_temp;

        setPageIndicator(0);

        final OnBoardingViewPagerAdapter adapter = new OnBoardingViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < (adapter.getCount() - 1) && position < (colors.length - 1)) {
                    parent.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, colors[position], colors[position + 1]));
                } else {
                    parent.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                setPageIndicator(position);

                if (currentPage == LAST_PAGE_POSITION) {
                    nextButton.setText("Sign In");
                    signInButton.setVisibility(View.INVISIBLE);
                    ViewCompat.animate(signInButton)
                            .alpha(0)
                            .setDuration(ANIM_ITEM_DURATION)
                            .setInterpolator(new DecelerateInterpolator(1.2f)).start();
                } else {
                    nextButton.setText("Next");
                    signInButton.setVisibility(View.VISIBLE);
                    signInButton.setAlpha(1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //Check if the user is logged in
        //1. Logged in - Navigate to Home Activity
        //2. Not Logged in - Navigate to SignIn Activity
        /*switch (UserAuthStore.getUserStatus()) {
            case AUTHENTICATED:
                FolderListActivity.startActivity(this);
                break;
            case UNAUTHENTICATED:
                break;
        }*/

        if(Hasura.getClient().getUser().getAuthToken() != null)
            FolderListActivity.startActivity(this);

    }

    private void setPageIndicator(int pageNum) {
        for (ImageView indicator : pageIndicators)
            indicator.setColorFilter(Color.parseColor("#33FFFFFF"), PorterDuff.Mode.SRC_IN);
        pageIndicators.get(pageNum).setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
    }

    @OnClick(R.id.signInButton)
    public void onSignInButtonClicked() {
        AuthenticationActivity.startActivity(this);
    }

    @OnClick(R.id.nextButton)
    public void onNextButtonClicked() {
        if (currentPage == LAST_PAGE_POSITION) {
            AuthenticationActivity.startActivity(this);
        } else {
            currentPage++;
            viewPager.setCurrentItem(currentPage);
        }
    }
}
