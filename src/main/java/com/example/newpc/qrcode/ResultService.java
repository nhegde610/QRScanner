package com.example.newpc.qrcode;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.JsonReader;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created to retrieve the result
 */

public class ResultService extends IntentService{

    public final static String POSITIVE= "positive";
    public final static String TOTAL ="total";
    public final static String LINK="link";
    public final static String API_KEY= "";
    public String positives;
    public String total;

    public ResultService(String name) {
        super(name);
    }

    public ResultService(){
        super("resultService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String scan_id = intent.getStringExtra("scan_id");
        String link = intent.getStringExtra("link");

        try {

            Uri.Builder builder = new Uri.Builder()
                    .scheme("https")
                    .authority("www.virustotal.com")
                    .appendPath("vtapi")
                    .appendPath("v2")
                    .appendPath("url")
                    .appendPath("report")
                    .appendQueryParameter("apikey",API_KEY)
                    .appendQueryParameter("resource",scan_id)
                    .appendQueryParameter("allinfo","true");
            String query = builder.build().toString();

            URL url = new URL(query);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            int responseCode = conn.getResponseCode();
            System.out.println("GET Response Code :: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                InputStream ins = conn.getInputStream();

                InputStreamReader in = new InputStreamReader(ins);
                JsonReader jsoreader = new JsonReader(in);

                ExtractId(jsoreader);
                jsoreader.close();
                in.close();

            } else if (responseCode == 204){

                Log.d("Request exceeded","Request exceed");

            } else {

                conn.disconnect();
                Log.d("GET not work","GET request not worked");

            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }

        Intent broadcastresult = new Intent();
        broadcastresult.putExtra(POSITIVE,positives);
        broadcastresult.putExtra(TOTAL,total);
        broadcastresult.putExtra(LINK,link);
        broadcastresult.setAction("com.example.newpc.qrcode.RESULT_VIA_INTENT");
        sendBroadcast(broadcastresult);
    }

    private void ExtractId(JsonReader jsonreader) throws IOException {
        try {
            jsonreader.beginObject(); // Start processing the JSON object
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (jsonreader.hasNext()) { // Loop through all keys
            String key = jsonreader.nextName(); // Fetch the next key
            if (key.equals("positives")) { // Check if desired key
                // Fetch the value as a String
                String value = jsonreader.nextString();
                positives = value;
            } else if (key.equals("total")){
                String value = jsonreader.nextString();
                total = value;
                break; // Break out of the loop
            } else {
                jsonreader.skipValue(); // Skip values of other keys
            }
        }
        jsonreader.close();
    }
}
