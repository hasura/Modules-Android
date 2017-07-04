package io.hasura.drive_android.ui.authentication;

import android.content.Context;
import android.os.Bundle;
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
import io.hasura.drive_android.R;

public class MobileNumberFragment extends Fragment {

    @BindView(R.id.mobile_number)
    TextInputEditText mobileNumber;
    @BindView(R.id.til_mobile_number)
    TextInputLayout tilMobileNumber;
    @BindView(R.id.til_user_name)
    TextInputLayout tilUsername;
    @BindView(R.id.user_name)
    TextInputEditText username;

    Unbinder unbinder;
    @BindView(R.id.next_button)
    Button nextButton;
    private InteractionListener interactionListener;

    public static MobileNumberFragment getInstance() {
        return new MobileNumberFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mobile_number, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InteractionListener) {
            interactionListener = (InteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MobileNumberFragment.InteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.next_button)
    public void onViewClicked() {
        tilMobileNumber.setError(null);
        tilUsername.setError(null);
        String enteredMobileNumber = mobileNumber.getText().toString().trim();
        String enteredUsername = username.getText().toString().trim();
        if (enteredMobileNumber.isEmpty()) {
            showErrorMessage(getResources().getString(R.string.mobile_number_error_text));
            return;
        }
        interactionListener.onNextButtonClicked(mobileNumber.getText().toString().trim(),username.getText().toString().trim());
        disableNextButton();
    }

    private void disableNextButton() {
        nextButton.setEnabled(false);
        nextButton.setText(getResources().getString(R.string.please_wait));
    }

    private void enableNextButton() {
        nextButton.setEnabled(true);
        nextButton.setText(getResources().getString(R.string.next_button_text));
    }

    public void showErrorMessage(String message) {
        tilMobileNumber.setError(message);
        enableNextButton();
    }

    public interface InteractionListener {
        void onNextButtonClicked(String mobileNumber,String username);
    }
}
