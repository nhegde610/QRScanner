package com.example.newpc.qrcode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created to receive the validateurlservice
 */

public class ValidateReceiver extends BroadcastReceiver {

    String scan_id;
    String link;
    public ValidateReceiver(){
        super();
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        scan_id = intent.getStringExtra(ValidateUrlService.SCAN_ID);
        link = intent.getStringExtra(ValidateUrlService.LINK_VALIDATE);

        Intent displaySuccess = new Intent(context,DisplaySuccess.class);
        displaySuccess.putExtra("scan_id",scan_id);
        displaySuccess.putExtra("link",link);
        context.startActivity(displaySuccess);
    }
}
