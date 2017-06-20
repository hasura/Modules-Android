# Hasura Android Module - Login

This module portrays different ways in which you can use Hasura Auth for SignUp and Login.

## 1. Configuring the Hasura Android SDK:

Once you have created your android project, you will have to add [Hasura-Android SDK](https://github.com/hasura/android-sdk). 

## 2. Initializing your Hasura Project:

To access your Hasura Project through android, you will have to first initialize it.

```
  Hasura.setProjectConfig(new ProjectConfig.Builder()
                .setProjectName("Project-Name")
                .build())
                .enableLogs()
                .initialise(this);

```

## 3. OTP SignUp and Login:

For using mobile OTP SignUp and Login, you will first have to enable mobile verification in the console and create an account on MSG91.
Following is a link to a blogpost for setting up mobile verification and MSG91:
https://medium.com/@amogh.karve/configuring-msg91-and-hasura-console-74184712e950

### 4. OTP SignUp:
Step 1: 
  Create a new HasuraUser object(say "user") and initialize it.
  ```
  HasuraUser user = Hasura.getClient().getUser();
  
  ```
  
Step2: 
  When the signUp button is clicked,use
  user.setMobile(mobilenumber) to set the mobile number.
  Then call the user.signup() to request otp.
  
  ```
  user.otpSignUp(new SignUpResponseListener() {
                    @Override
                    public void onSuccessAwaitingVerification(HasuraUser hasuraUser) {
                    
                    }
                    @Override
                    public void onSuccess(String s) {
                        
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        
                    }
                });
  
  ```
Step3:
  If the request is successful, receive the otp in the onSuccessAwaitingVerification() method.
  Then call user.confirmMobile(otp,MobileConfirmationResponseListener) to confirm the otp.
  
  ```
  user.confirmMobile("received otp", new MobileConfirmationResponseListener() {
                                    @Override
                                    public void onSuccess() {
                                        
                                    }

                                    @Override
                                    public void onFailure(HasuraException e) {
                                    
                                    }
                                });
  
  ```
Step4:
  Once this is done, you can now login using this username and mobile.
  
### 5. OTP Login:
Step 1:
  Create a new HasuraUser object(say "user") and initialize it.
Step 2:
  When loginbutton is clicked, use
  user.setMobile(mobilenumber) to set the mobile number.
  user.enableMobileOtpLogin() is essential to use OTP login.
Step 3:
  Call user.enableMobileOtpLogin() to enable LogIn using OTP.
  
  ```
  user.setMobile("mobile number");
  user.enableMobileOtpLogin();
  
  ```
  
Step 4:
  Call user.sendOtpToMobile(OtpStatusListener) to send OTP to mobile.
  
  ```
  user.sendOtpToMobile(new OtpStatusListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        
                    }
                });
  
  ```
    (Currently there is a slight is issue with the sendOtpToMobile() API, it will give a 401 and then send the OTP.
    This will be fixed soon, for the time being you can call the confirmMobile() in the onFailure() to make it work) 
    
Step 5:
  If the above step is successful, you will receive the otp.
  Receive this otp and call user.otpLogin(otp,AuthResponseListener) to confirm the otp.
  
  ```
  user.otpLogin("received otp", new AuthResponseListener() {
                                    @Override
                                    public void onSuccess(HasuraUser hasuraUser) {
                                        
                                    }

                                    @Override
                                    public void onFailure(HasuraException e) {
                                    
                                    }
                                });
  
  ```
  
Step 6:
  On success, you will receive the auth token and hasura-id in the response.

