package com.example.newpc.qrcode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created to receive the validateurlservice
 */

public class ValidateReceiver extends BroadcastReceiver {

    public ValidateReceiver(){
        super();
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String scan_id = intent.getStringExtra(ValidateUrlService.SCAN_ID);
        String link = intent.getStringExtra(ValidateUrlService.LINK_VALIDATE);

        Intent displaySuccess = new Intent(context,DisplaySuccess.class);
        displaySuccess.putExtra("scan_id", scan_id);
        displaySuccess.putExtra("link", link);
        displaySuccess.addFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(displaySuccess);
    }
}
