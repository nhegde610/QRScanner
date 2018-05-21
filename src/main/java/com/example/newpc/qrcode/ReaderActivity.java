package com.example.newpc.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ReaderActivity extends AppCompatActivity implements PasswordInputDialog.OnDialogDismissListener {

    private boolean isurl = false;
    private String DataFromScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        Button scan_btn = (Button) findViewById(R.id.scan_btn);
        Button scangal_btn = (Button) findViewById(R.id.scangal_btn);
        Button genQR_btn = (Button) findViewById(R.id.genQr_btn);
        final Activity activity = this;
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setCaptureActivity(CaptureActivityPotrait.class);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
        scangal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReaderActivity.this,GalleryReader.class);
                startActivity(intent);
            }
        });
        genQR_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ReaderActivity.this,GeneratorActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {
                    try {
                            super.onActivityResult(requestCode,resultCode,data);
                            DataFromScan = result.getContents();

                            String SubDataString = DataFromScan.substring(0,4);
                            if(SubDataString.equals("ENC;")){

                                DataFromScan = DecryptionData.RemoveEncrypt(DataFromScan);
                                DialogFragment alertDialogAndroid = new PasswordInputDialog();
                                alertDialogAndroid.show(getSupportFragmentManager(),"PasswordDialog");
                            }
                            else {
                                validate(DataFromScan);
                                if(isurl){
                                    StartRedirectService(DataFromScan);
                                }
                                else {
                                    DialogFragment TextFragment = AlertTextDialog.newInstance(DataFromScan);
                                    TextFragment.show(getSupportFragmentManager(), "DataFromScan");
                                }
                            }

                    }catch(Exception e){
                        e.printStackTrace();
                        Toast.makeText(this, result.getContents(),Toast.LENGTH_LONG).show();
                    }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void StartRedirectService(String url) {
        Intent RedirectCheck = new Intent(this, RedirectService.class);
        RedirectCheck.putExtra("url", url);
        this.startService(RedirectCheck);
    }

    private void validate(String Data){
        isurl = CheckData.isDataUrl(Data);
    }

    @Override
    public void onDialogDismissListener(String secret) {

        DataFromScan = DecryptionData.decrypt(DataFromScan,secret);

        validate(DataFromScan);
        if(isurl){
            StartRedirectService(DataFromScan);
        }
        else {
            DialogFragment TextFragment = AlertTextDialog.newInstance(DataFromScan);
            TextFragment.show(getSupportFragmentManager(), "DataFromScan");
        }
    }
}
