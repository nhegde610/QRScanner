package com.example.newpc.qrcode;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created this file to run a background service which can check for the redirect url.
 */

public class RedirectService extends IntentService {

    //String to fragment
    public final static String URLCreated = "url";

    public RedirectService(String name) {
        //naming the thread for debugging purposes.
        super(RedirectService.class.getName());
        setIntentRedelivery(true);
        /*ensuring that if  the process is killed before onHandleIntent() returns,
        it’ll be restarted and the last delivered intent will be redelivered.
         */
    }

    public RedirectService() {
        super("Redirectservice");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String link = intent.getStringExtra("url");
        boolean checkDone = true;
        try {
            while (checkDone) {
                URL url = new URL(link);
                HttpURLConnection ucon = (HttpURLConnection) url.openConnection();
                ucon.setInstanceFollowRedirects(false);
                Log.w("ucon",ucon.toString());
                int response = ucon.getResponseCode();
                Log.w("ucon",Integer.toString(response));
                if (ucon.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM || ucon.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
                    URL secondURL = new URL(ucon.getHeaderField("Location"));
                    URLConnection conn = secondURL.openConnection();
                    link = conn.getURL().toString();
                } else
                    checkDone = false;
            }

            //return the result
            Intent broadCastResult = new Intent();
            broadCastResult.putExtra(URLCreated, link);
            broadCastResult.setAction("com.example.newpc.qrcode.MESSAGE_VIA_INTENT");
            sendBroadcast(broadCastResult);

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
}

