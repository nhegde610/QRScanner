package com.example.newpc.qrcode;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


/**
 * Created to receive the broadcast from RedirectService
 */

public class RedirectReceiver extends BroadcastReceiver {

    public RedirectReceiver() {

        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String url = intent.getStringExtra(RedirectService.URLCreated);
        String redirect = intent.getStringExtra(RedirectService.RedirectCheck);

       Intent createdialog = new Intent(context,createDialog.class);
                createdialog.putExtra("url", url);
                createdialog.putExtra("redirect",redirect);
                createdialog.addFlags(FLAG_ACTIVITY_NEW_TASK);
       context.startActivity(createdialog);
    }
}
