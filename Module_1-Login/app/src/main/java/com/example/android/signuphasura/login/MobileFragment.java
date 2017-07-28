package com.example.android.signuphasura.login;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.android.signuphasura.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hasura.sdk.HasuraErrorCode;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseListener.AuthResponseListener;
import io.hasura.sdk.responseListener.OtpStatusListener;
import io.hasura.sdk.responseListener.SignUpResponseListener;

/**
 * Created by amogh on 13/6/17.
 */

public class MobileFragment extends BaseFragment {

    public static final String TITLE = "Mobile OTP Login";
    public static final String TAG = TITLE;

    @Bind(R.id.mobile)
    TextInputEditText mobile;
    @Bind(R.id.til_mobile)
    TextInputLayout tilMobile;
    @Bind(R.id.next_button)
    Button nextButton;
    @Bind(R.id.mobileLayout)
    LinearLayout mobileLayout;
    @Bind(R.id.otp)
    TextInputEditText otp;
    @Bind(R.id.til_Otp)
    TextInputLayout tilOtp;
    @Bind(R.id.submit_button)
    Button submitButton;
    @Bind(R.id.otpLayout)
    LinearLayout otpLayout;
    @Bind(R.id.cancel_button)
    Button cancelButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mobile, container, false);
        ButterKnife.bind(this, view);
        showMobileLayout();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    //Submitting Mobile number
    @OnClick(R.id.next_button)
    public void onNextButtonClicked() {
        showProgressDialog("Sending Otp");
        user.setMobile(mobile.getText().toString());
        //Set username as the mobile number since it is unique
        user.setUsername(mobile.getText().toString());

        //First, check if user exists
        user.otpSignUp(new SignUpResponseListener() {
            @Override
            public void onSuccessAwaitingVerification(HasuraUser hasuraUser) {
                hideProgressDialog();
                showOtpLayout();
                showLongToast("Otp sent to mobile");
            }

            @Override
            public void onSuccess(HasuraUser hasuraUser) {
                hideProgressDialog();

            }

            @Override
            public void onFailure(HasuraException e) {
                //If user already exists then send OTP to mobile
                if (e.getCode() == HasuraErrorCode.USER_ALREADY_EXISTS) {
                    user.sendOtpToMobile(new OtpStatusListener() {
                        @Override
                        public void onSuccess(String s) {
                            hideProgressDialog();
                            showOtpLayout();
                            showLongToast(s);
                        }

                        @Override
                        public void onFailure(HasuraException e) {
                            hideProgressDialog();
                            showLongToast(e.toString());
                        }
                    });

                } else {
                    hideProgressDialog();
                    showLongToast(e.toString());
                }
            }
        });

    }

    private void showOtpLayout() {
        //hide mobile layout
        mobileLayout.setVisibility(View.GONE);
        //Show otp layout
        otpLayout.setVisibility(View.VISIBLE);
    }

    private void showMobileLayout() {
        //hide mobile layout
        mobileLayout.setVisibility(View.VISIBLE);
        //Show otp layout
        otpLayout.setVisibility(View.GONE);
    }

    //Submitting OTP
    @OnClick(R.id.submit_button)
    public void onSubmitButtonClicked() {
        showProgressDialog("Logging In");
        user.otpLogin(otp.getText().toString(), new AuthResponseListener() {
            @Override
            public void onSuccess(String s) {
                hideProgressDialog();
                showLongToast(s);
            }

            @Override
            public void onFailure(HasuraException e) {
                hideProgressDialog();
                showLongToast(e.toString());
            }
        });
    }

    @OnClick(R.id.cancel_button)
    public void onViewClicked() {
        otp.setText("");
        mobile.setText("");
        showMobileLayout();
    }
}
