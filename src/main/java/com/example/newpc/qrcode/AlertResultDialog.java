package com.example.newpc.qrcode;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.List;

/**
 * Created to display the result
 */

public class AlertResultDialog extends DialogFragment {
    String DataToDisplay;
    String Link;
    String Title;

    static AlertResultDialog newInstance(String total,String positives,String link) {

        AlertResultDialog f = new AlertResultDialog();

        // Supply data input as an argument.
        Bundle args = new Bundle();
        args.putString("total",total);
        args.putString("positive",positives);
        args.putString("link",link);
        f.setArguments(args);

        return f;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        DataToDisplay = analyseResult();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(Title);
        builder.setMessage(DataToDisplay)
                .setPositiveButton(R.string.open, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Open the url
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Link));
                        PackageManager packageManager = getActivity().getPackageManager();
                        List<ResolveInfo> activities = packageManager.queryIntentActivities(browserIntent, 0);
                        boolean isIntentSafe = activities.size() > 0;

                        String title = getResources().getString(R.string.choose_title);
                        Intent chooser = Intent.createChooser(browserIntent, title);

                        if (isIntentSafe) {

                            startActivity(chooser);

                        }
                        getActivity().finish();

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        getActivity().finish();
                    }
                });
        return builder.create();
    }

    private String analyseResult(){
        String parsedata = "";
        String data ="";
        Link = getArguments().getString("link");

        int total = Integer.parseInt(getArguments().getString("total"));
        int positive = Integer.parseInt(getArguments().getString("positive"));

        if(positive == 0){
            Title = "The url is safe to visit";
            parsedata = "Score" + ":" + positive + "/" + total;
            data = parsedata + "\n" + Link;
        }else if (positive <= total/2){
            Title = "The url seems to contain malicious data! Visit on your caution";
            parsedata = "Score" + ":" + positive + "/" + total;
            data = parsedata + "\n" + Link;
        } else {
            Title = "The url is directing to a malicious website! Do not visit it";
            parsedata = "Score" + ":" + positive + "/" + total;
            data = parsedata + "\n" + Link;
        }

        return data;
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
