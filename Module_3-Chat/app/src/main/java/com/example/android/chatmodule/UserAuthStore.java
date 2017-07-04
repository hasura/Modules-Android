package com.example.android.chatmodule;

import android.os.AsyncTask;

import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraClient;
import io.hasura.sdk.HasuraErrorCode;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseListener.AuthResponseListener;
import io.hasura.sdk.responseListener.OtpStatusListener;
import io.hasura.sdk.responseListener.SignUpResponseListener;

/**
 * Created by jaison on 30/03/17.
 */

public class UserAuthStore {

    public interface Listener {
        void onStateChanged(AuthStoreState state, Object data);
    }

    /**
     * AUTHENTICATION
     **/

    Listener listener;
    private String mobileNumber;
    private String username;
    private String otp;
    private int oldUser;

    HasuraUser user;
    HasuraClient client = Hasura.getClient();

    public UserAuthStore(Listener listener) {
        this.listener = listener;
    }

    public void authenticateUser(String mobileNumber,String username) {
        this.mobileNumber = mobileNumber;
        this.username = username;
        user = client.getUser();
        user.setUsername(username);
        user.setMobile(mobileNumber);

        checkIfuserExists(mobileNumber,username);

        user.setMobile(mobileNumber);
        user.setUsername(username);

    }

    public void resendOtp() {
        authenticateUser(mobileNumber,username);
    }

    private void broadcastState(AuthStoreState storeState) {
        if (listener != null) {
            listener.onStateChanged(storeState, null);
        }
    }

    private void broadcastState(AuthStoreState storeState, Object data) {
        if (listener != null) {
            listener.onStateChanged(storeState, data);
        }
    }

    /**API**/


    public void verifyOTP(String otp) {
        this.otp = otp;

        if(oldUser == 1){
            user.otpLogin(otp, new AuthResponseListener() {
                @Override
                public void onSuccess(String s) {
                    broadcastState(AuthStoreState.VERIFYING_OTP_SUCCESSFUL);
                }

                @Override
                public void onFailure(HasuraException e) {
                    broadcastState(AuthStoreState.VERIFYING_OTP_FAILED, e);
                }
            });
        } else {
            user.confirmMobileAndLogin(otp, new AuthResponseListener() {
                @Override
                public void onSuccess(String s) {
                    broadcastState(AuthStoreState.VERIFYING_OTP_SUCCESSFUL);
                }

                @Override
                public void onFailure(HasuraException e) {
                    broadcastState(AuthStoreState.VERIFYING_OTP_FAILED, e);
                }
            });
        }
    }

    private void checkIfuserExists(String mobile, String username){

        broadcastState(AuthStoreState.SENDING_OTP);

        user.otpSignUp(new SignUpResponseListener() {
            @Override
            public void onSuccessAwaitingVerification(HasuraUser hasuraUser) {
                broadcastState(AuthStoreState.SENDING_OTP_SUCCESSFUL);
            }

            @Override
            public void onSuccess(HasuraUser hasuraUser) {

            }

            @Override
            public void onFailure(HasuraException e) {
                if (e.getCode() == HasuraErrorCode.USER_ALREADY_EXISTS) {
                    oldUser = 1;
                    user.sendOtpToMobile(new OtpStatusListener() {
                        @Override
                        public void onSuccess(String s) {
                            broadcastState(AuthStoreState.SENDING_OTP_SUCCESSFUL);
                        }

                        @Override
                        public void onFailure(HasuraException e) {
                            broadcastState(AuthStoreState.SENDING_OTP_SUCCESSFUL);
                        }
                    });

                }
            }
        });
    }


    /**
     * MOCKS
     **/

    private static final long SLEEP_TIME = 5000;

    public class MockUserAuthentication extends AsyncTask<Void, Void, Boolean> {

        String mobileNumber;

        MockUserAuthentication(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // Simulate network access.
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean successful) {
            broadcastState(successful ? AuthStoreState.SENDING_OTP_SUCCESSFUL : AuthStoreState.SENDING_OTP_FAILED);
        }

        @Override
        protected void onCancelled() {
        }
    }

    public class MockOTPVerification extends AsyncTask<Void, Void, Boolean> {

        String mobileNumber;
        String otp;

        MockOTPVerification(String mobileNumber, String otp) {
            this.mobileNumber = mobileNumber;
            this.otp = otp;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // Simulate network access.
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean successful) {
            broadcastState(successful ? AuthStoreState.VERIFYING_OTP_SUCCESSFUL : AuthStoreState.VERIFYING_OTP_FAILED);
        }

        @Override
        protected void onCancelled() {
        }
    }

}
