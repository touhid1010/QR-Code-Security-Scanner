package com.netizenbd.netichecker;

import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by Md. Touhidul Islam on 11/23/2016.
 */

public class MySendSMS {

    Context context;

    public MySendSMS(Context context) {
        this.context = context;
    }


    public void sendMySMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(context, "Message Sent", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
