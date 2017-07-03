package io.hasura.drive_android.stores;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import io.hasura.drive_android.enums.AuthStoreState;
import io.hasura.drive_android.enums.ServerErrorType;
import io.hasura.drive_android.enums.UserStatus;
import io.hasura.drive_android.hasura.Hasura;
import io.hasura.drive_android.models.AuthRequest;
import io.hasura.drive_android.models.AuthResponse;
import io.hasura.drive_android.models.MessageResponse;
import io.hasura.drive_android.models.ServerError;
import io.hasura.drive_android.utils.SharedPrefManager;

/**
 * Created by jaison on 30/03/17.
 */

public class UserAuthStore {

    public interface Listener {
        void onStateChanged(AuthStoreState state, Object data);
    }

    final static String USERPREFS_NAME = "UserPrefsFile";
    final static String PREFS_USERSESSION_KEY = "UserSessionKey";
    final static String PREFS_USERID_KEY = "UserIdKey";

    public static UserStatus getUserStatus() {
        return getUserSession().isEmpty() ? UserStatus.UNAUTHENTICATED : UserStatus.AUTHENTICATED;
    }

    /**
     * SESSION
     **/

    public static String getUserSession() {
        SharedPreferences prefs = SharedPrefManager.getPref(USERPREFS_NAME);
        return prefs.getString(PREFS_USERSESSION_KEY, "");
    }

    public static void setUserSession(String authToken) {
        SharedPreferences.Editor editor = SharedPrefManager.getPref(USERPREFS_NAME).edit();
        editor.putString(PREFS_USERSESSION_KEY, authToken);
        editor.apply();
    }

    public static int getUserId() {
        SharedPreferences prefs = SharedPrefManager.getPref(USERPREFS_NAME);
        return prefs.getInt(PREFS_USERID_KEY, -1);
    }

    public static void setUserId(int userId) {
        SharedPreferences.Editor editor = SharedPrefManager.getPref(USERPREFS_NAME).edit();
        editor.putInt(PREFS_USERID_KEY, userId);
        editor.apply();
    }

    public void setSession(AuthResponse response) {
        UserAuthStore.setUserSession(response.getAuthToken());
        UserAuthStore.setUserId(response.getId());
    }

    public static void clearSession() {
        UserAuthStore.setUserSession("");
        UserAuthStore.setUserId(-1);
    }

    /**
     * AUTHENTICATION
     **/

    Listener listener;
    private String mobileNumber;
    private String otp;

    public UserAuthStore(Listener listener) {
        this.listener = listener;
    }

    public void authenticateUser(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        broadcastState(AuthStoreState.SENDING_OTP);
        verifyMobile();
    }

    public void resendOtp() {
        authenticateUser(mobileNumber);
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

    private void verifyMobile() {
        Hasura.auth.verifyMobile(new AuthRequest(mobileNumber))
                .enqueue(new BaseResponseListener<MessageResponse>() {
                    @Override
                    public void onSuccessfulResponse(MessageResponse response) {
                        broadcastState(AuthStoreState.SENDING_OTP_SUCCESSFUL);
                    }

                    @Override
                    public void onFailureResponse(ServerError error) {
                        if (error.getType() == ServerErrorType.USER_INVALID)
                            registerMobile();
                        else if (error.getErrorMessage().equals("OTP sent to mobile"))
                            broadcastState(AuthStoreState.SENDING_OTP_SUCCESSFUL);
                        else broadcastState(AuthStoreState.SENDING_OTP_FAILED, error);
                    }
                });
    }

    private void registerMobile() {
        Hasura.auth.registerMobile(new AuthRequest(mobileNumber))
                .enqueue(new BaseResponseListener<AuthResponse>() {
                    @Override
                    public void onSuccessfulResponse(AuthResponse response) {
                        verifyMobile();
                    }

                    @Override
                    public void onFailureResponse(ServerError error) {
                        broadcastState(AuthStoreState.SENDING_OTP_FAILED, error);
                    }
                });
    }

    public void verifyOTP(String otp) {
        this.otp = otp;
        broadcastState(AuthStoreState.VERIFYING_OTP);
        Hasura.auth.verifyOtp(new AuthRequest(mobileNumber, otp))
                .enqueue(new BaseResponseListener<AuthResponse>() {
                    @Override
                    public void onSuccessfulResponse(AuthResponse response) {
                        setSession(response);
                        broadcastState(AuthStoreState.VERIFYING_OTP_SUCCESSFUL);
                    }

                    @Override
                    public void onFailureResponse(ServerError error) {
                        broadcastState(AuthStoreState.VERIFYING_OTP_FAILED, error);
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
