package com.example.android.chatmodule;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtpFragment extends Fragment {

    @BindView(R.id.otp)
    TextInputEditText otp;
    @BindView(R.id.til_otp)
    TextInputLayout tilOtp;
    @BindView(R.id.submit_button)
    Button submitButton;
    Unbinder unbinder;
    @BindView(R.id.resend_button)
    Button resendButton;

    private InteractionListener interactionListener;

    public OtpFragment() {

    }

    public static OtpFragment getInstance() {
        OtpFragment fragment = new OtpFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_otp, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.submit_button)
    public void onSubmitButtonClicked() {
        tilOtp.setError(null);
        String enteredOtp = otp.getText().toString().trim();
        if (enteredOtp.isEmpty()) {
            tilOtp.setError("Please enter the OTP you received");
            return;
        }
        interactionListener.onSubmitButtonClicked(enteredOtp);
    }

    @OnClick(R.id.resend_button)
    public void onResendButtonClicked() {
        interactionListener.onResendButtonClicked();
        disableResendButton();
    }

    private void disableSubmitButton() {
        submitButton.setEnabled(false);
        submitButton.setText("Please Wait");
    }

    private void enableSubmitButton() {
        submitButton.setEnabled(true);
        submitButton.setText("Submit");
    }

    private void disableResendButton() {
        resendButton.setEnabled(false);
        resendButton.setText("Please Wait");
    }

    private void enableResendButton() {
        resendButton.setEnabled(true);
        resendButton.setText("Resend");
    }

    public void showErrorMessage(String message) {
        tilOtp.setError(message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof InteractionListener) {
            interactionListener = (InteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OtpFragment.InteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
    }

    interface InteractionListener {
        void onSubmitButtonClicked(String otp);

        void onResendButtonClicked();
    }
}
