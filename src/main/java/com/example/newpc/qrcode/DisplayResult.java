package com.example.newpc.qrcode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

/**
 * Created to start a fragment
 */

public class DisplayResult extends FragmentActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String positives = getIntent().getStringExtra("positive");
        String total = getIntent().getStringExtra("total");
        String link = getIntent().getStringExtra("link");
        DialogFragment ResultFragment = AlertResultDialog.newInstance(total,positives,link);
        ResultFragment.show(getSupportFragmentManager(),"Result");
    }
}
