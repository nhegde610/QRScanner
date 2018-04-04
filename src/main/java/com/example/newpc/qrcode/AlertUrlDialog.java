package com.example.newpc.qrcode;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.app.AlertDialog;

public class AlertUrlDialog extends DialogFragment {
    String UrlToDisplay;


    static AlertUrlDialog newInstance(String Url) {
                    AlertUrlDialog f = new AlertUrlDialog();

                    // Supply url input as an argument.
                    Bundle args = new Bundle();
                    args.putString("url",Url);
                    f.setArguments(args);

                    return f;
                }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UrlToDisplay = getArguments().getString("url");
        // Using the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.open_url);
        builder.setMessage(UrlToDisplay)
                .setPositiveButton(R.string.open, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Open the url
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(UrlToDisplay));
                        startActivity(browserIntent);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}