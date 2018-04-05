package com.example.newpc.qrcode;


import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.app.AlertDialog;

import java.util.List;

import static android.R.attr.label;
import static android.R.attr.text;
import static android.content.Context.CLIPBOARD_SERVICE;

public class AlertUrlDialog extends DialogFragment {
    String DataToDisplay;


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

        DataToDisplay = getArguments().getString("url");
        // Using the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if(CheckData.isDataUrl(DataToDisplay)) {
            builder.setTitle(R.string.Title);
            builder.setMessage(DataToDisplay)
                    .setPositiveButton(R.string.open, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Open the url
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(DataToDisplay));
                            PackageManager packageManager = getActivity().getPackageManager();
                            List<ResolveInfo> activities = packageManager.queryIntentActivities(browserIntent, 0);
                            boolean isIntentSafe = activities.size() > 0;

                            String title = getResources().getString(R.string.choose_title);
                            Intent chooser = Intent.createChooser(browserIntent, title);

                            if (isIntentSafe) {
                                startActivity(chooser);
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
        }
        else{
            builder.setTitle(R.string.Title);
            builder.setMessage(DataToDisplay)
                    .setPositiveButton(R.string.copy, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // TODO Copy the Data to clipboard
                            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText(DataToDisplay,DataToDisplay);
                            clipboard.setPrimaryClip(clip);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        }
                    });
        }
        // Create the AlertDialog object and return it
        return builder.create();
    }
}