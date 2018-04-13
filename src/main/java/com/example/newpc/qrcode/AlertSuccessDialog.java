package com.example.newpc.qrcode;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.List;

/**
 * Created to display that request was made
 */

public class AlertSuccessDialog extends DialogFragment {

    String DataToDisplay;
    String link;
    static AlertSuccessDialog newInstance(String Data,String link) {
        AlertSuccessDialog f = new AlertSuccessDialog();

        // Supply data input as an argument.
        Bundle args = new Bundle();
        args.putString("data", Data);
        args.putString("link",link);
        f.setArguments(args);

        return f;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        link = getArguments().getString("link");
        DataToDisplay = getArguments().getString("data");
        // Using the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.retrieve);
        builder.setMessage(DataToDisplay)
                .setPositiveButton(R.string.result, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent getresult = new Intent(getActivity(),ResultService.class);
                        getresult.putExtra("scan_id",DataToDisplay);
                        getresult.putExtra("link",link);
                        getActivity().startService(getresult);
                        getActivity().finish();

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        getActivity().finish();
                    }
                });
        builder.setNeutralButton(R.string.open, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                getActivity().finish();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                PackageManager packageManager = getActivity().getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(browserIntent, 0);
                boolean isIntentSafe = activities.size() > 0;

                String title = getResources().getString(R.string.choose_title);
                Intent chooser = Intent.createChooser(browserIntent, title);

                if (isIntentSafe) {

                    startActivity(chooser);

                }
            }
        });

        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().finish();
    }

    @Override
    public void onStop(){
        super.onStop();
        getActivity().finish();
    }

    @Override
    public void onPause(){
        super.onPause();
        getActivity().finish();
    }
}