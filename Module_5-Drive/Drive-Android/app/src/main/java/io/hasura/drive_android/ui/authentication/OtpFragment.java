package io.hasura.drive_android.ui.authentication;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.hasura.drive_android.R;
import io.hasura.drive_android.utils.SmsReceiver;

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
        // Required empty public constructor
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

        SmsReceiver.bindListener(new SmsReceiver.SmsListener() {
            @Override
            public void messageReceived(String otpString) {
                Log.d("Text", otpString);
                otp.setText(otpString);
            }
        });

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
            tilOtp.setError(getResources().getString(R.string.otp_error_text));
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
        submitButton.setText(getResources().getString(R.string.please_wait));
    }

    private void enableSubmitButton() {
        submitButton.setEnabled(true);
        submitButton.setText(getResources().getString(R.string.submit_button_text));
    }

    private void disableResendButton() {
        resendButton.setEnabled(false);
        resendButton.setText(getResources().getString(R.string.please_wait));
    }

    private void enableResendButton() {
        resendButton.setEnabled(true);
        resendButton.setText(getResources().getString(R.string.resend_otp_button_text));
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
