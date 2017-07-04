package io.hasura.drive_android.ui.authentication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hasura.drive_android.R;
import io.hasura.drive_android.enums.AuthStoreState;
import io.hasura.drive_android.models.ServerError;
import io.hasura.drive_android.stores.UserAuthStore;
import io.hasura.drive_android.ui.folderList.FolderListActivity;
import io.hasura.sdk.HasuraUser;

public class AuthenticationActivity extends AppCompatActivity
        implements MobileNumberFragment.InteractionListener, OtpFragment.InteractionListener {

    HasuraUser user;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.progress_layout)
    LinearLayout progressLayout;

    UserAuthStore store;
    MobileNumberFragment mobileNumberFragment;
    OtpFragment otpFragment;

    public static void startActivity(Activity startingActivity) {
        startingActivity.startActivity(new Intent(startingActivity, AuthenticationActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (savedInstanceState == null) {
            addMobileNumberFragment();
        }
        showProgressBar(false);

        /*
                Check here
         */
        store = new UserAuthStore(authStoreStateChangeListener);
    }

    private UserAuthStore.Listener authStoreStateChangeListener = new UserAuthStore.Listener() {
        @Override
        public void onStateChanged(AuthStoreState state, Object object) {
            switch (state) {
                case SENDING_OTP:
                    showProgressBar(true);
                    break;
                case SENDING_OTP_SUCCESSFUL:
                    showProgressBar(false);
                    addOTPFragment();
                    break;
                case SENDING_OTP_FAILED:
                    showProgressBar(false);
                    ServerError error = (ServerError) object;
                    mobileNumberFragment.showErrorMessage(error.getErrorMessage());
                    break;
                case VERIFYING_OTP:
                    showProgressBar(true);
                    break;
                case VERIFYING_OTP_SUCCESSFUL:
                    showProgressBar(false);
                    FolderListActivity.startActivity(AuthenticationActivity.this);
                    break;
                case VERIFYING_OTP_FAILED:
                    showProgressBar(false);
                    ServerError otpError = (ServerError) object;
                    otpFragment.showErrorMessage(otpError.getErrorMessage());
                    break;
            }
        }
    };

    private void addMobileNumberFragment() {
        mobileNumberFragment = MobileNumberFragment.getInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, mobileNumberFragment)
                .commit();
    }

    private void addOTPFragment() {
        otpFragment = OtpFragment.getInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, otpFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onNextButtonClicked(String mobileNumber,String username) {
        /*
                Send Otp
         */



        store.authenticateUser(mobileNumber,username);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getSupportFragmentManager().popBackStack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSubmitButtonClicked(String otp) {
        /*
            verify otp
         */
        store.verifyOTP(otp);
    }

    @Override
    public void onResendButtonClicked() {
        /*
            Re-Send otp
         */
        store.resendOtp();
    }

    private void showProgressBar(boolean show) {
        progressLayout.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }
}
