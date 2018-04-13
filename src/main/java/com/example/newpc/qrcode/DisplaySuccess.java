package com.example.newpc.qrcode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

/**
 * Created to display fragment
 * */

public class DisplaySuccess extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String scan_id = getIntent().getStringExtra("scan_id");
        String link = getIntent().getStringExtra("link");
        DialogFragment SuccessFragment = AlertSuccessDialog.newInstance(scan_id,link);
        SuccessFragment.show(getSupportFragmentManager(),"Success");

    }

}
