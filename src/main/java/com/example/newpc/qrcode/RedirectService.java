package com.example.newpc.qrcode;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created this file to run a background service which can check for the redirect url.
 */

public class RedirectService extends IntentService {

    //String to fragment
    public final static String URLCreated = "url";
    public final static String RedirectCheck= "redirect";

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
        String link = intent != null ? intent.getStringExtra("url") : null;
        String originalLink = intent != null ? intent.getStringExtra("url") : null;
        try {

            boolean checkDone = true;
            while (checkDone) {
                URLConnection con = new URL(link).openConnection();
                con.connect();
                InputStream is = con.getInputStream();
                link = con.getURL().toString();

                URL url = new URL(link);
                HttpURLConnection ucon = (HttpURLConnection) url.openConnection();
                ucon.setInstanceFollowRedirects(false);
                int response = ucon.getResponseCode();

                if (ucon.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM || ucon.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
                    String linkcon = ucon.getHeaderField("Location").toString();
                    URL base = ucon.getURL();
                    URL secondURL = null;

                    if(CheckData.isDataUrl(linkcon)) {
                        secondURL = new URL(linkcon);
                    } else {
                        secondURL = new URL(base,linkcon);
                    }
                    URLConnection conn = secondURL.openConnection();
                    link = conn.getURL().toString();
                } else
                    checkDone = false;
            }

            String redirect = originalLink.equals(link) ? "false" : "true";


            //return the result
            Intent broadCastResult = new Intent();
            broadCastResult.putExtra(URLCreated, link);
            broadCastResult.putExtra(RedirectCheck,redirect);
            broadCastResult.setAction("com.example.newpc.qrcode.MESSAGE_VIA_INTENT");
            sendBroadcast(broadCastResult);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
}

