package com.example.newpc.qrcode;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.app.AlertDialog;
import java.util.List;

public class AlertUrlDialog extends DialogFragment {
    String DataToDisplay;

    static AlertUrlDialog newInstance(String Data) {
        AlertUrlDialog f = new AlertUrlDialog();

        // Supply data input as an argument.
        Bundle args = new Bundle();
        args.putString("data", Data);
        f.setArguments(args);

        return f;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataToDisplay = getArguments().getString("data");
        // Using the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.Title);
        builder.setMessage(DataToDisplay)
                .setPositiveButton(R.string.open, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Open the url
                        getActivity().finish();
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
                        getActivity().finish();
                    }
                });
        // Create the AlertDialog object and return it
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


