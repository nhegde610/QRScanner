package com.example.newpc.qrcode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created to receiver the broadcast from ResultService
 */

public class ResultReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String postive = intent.getStringExtra(ResultService.POSITIVE);
        String total = intent.getStringExtra(ResultService.TOTAL);
        String link = intent.getStringExtra(ResultService.LINK);

        Intent ShowResult = new Intent(context,DisplayResult.class);
        ShowResult.putExtra("positive",postive)
                   .putExtra("total",total)
                   .putExtra("link",link)
                    .addFlags(FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(ShowResult);
    }
}
