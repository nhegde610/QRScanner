package com.example.newpc.qrcode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
/**
 * Created to start fragment.
 */

public class createDialog extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String url = getIntent().getStringExtra("url");
        DialogFragment UrlFragment = AlertUrlDialog.newInstance(url);
        UrlFragment.show(getSupportFragmentManager(),"RedirectUrl");

    }
}
