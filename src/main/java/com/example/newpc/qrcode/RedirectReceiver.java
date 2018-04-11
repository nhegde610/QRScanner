package com.example.newpc.qrcode;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * Created to receive the broadcast from RedirectService
 */

public class RedirectReceiver extends BroadcastReceiver {
    String url;

    public RedirectReceiver() {

        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        url = intent.getStringExtra(RedirectService.URLCreated);

       Intent createdialog = new Intent(context,createDialog.class);
                createdialog.putExtra("url",url);
       context.startActivity(createdialog);
    }
}
