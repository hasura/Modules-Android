package io.hasura.drive_android.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsReceiver extends BroadcastReceiver {

    public interface SmsListener {
        void messageReceived(String otp);
    }

    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Text", "On Received message");
        Bundle data = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for (int i = 0; i < pdus.length; i++) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            if (!sender.equals("AD-Hasura"))
                return;
            //You must check here if the sender is your provider and not another one with same text.
            Pattern p = Pattern.compile("(|^)\\d{6}");
            String messageBody = smsMessage.getMessageBody();
            if (messageBody != null) {
                Matcher m = p.matcher(messageBody);
                if (m.find()) {
                    mListener.messageReceived(m.group(0));
                } else {
                    //something went wrong
                }
            }

        }

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}