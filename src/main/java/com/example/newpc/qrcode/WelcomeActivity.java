package com.example.newpc.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        //noinspection ConstantConditions
        getSupportActionBar().hide();
        LogoLauncher logolauncher = new LogoLauncher();
        logolauncher.start();

    }

    private class LogoLauncher extends Thread {
        public void run() {
            try {
                int SLEEP_TIMER = 3;
                sleep(1000* SLEEP_TIMER);

            } catch (InterruptedException e) {
                e.printStackTrace();

            }
            Intent intent = new Intent(WelcomeActivity.this,ReaderActivity.class);
            startActivity(intent);
            WelcomeActivity.this.finish();
        }
    }
}

