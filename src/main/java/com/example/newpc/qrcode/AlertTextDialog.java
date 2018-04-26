package com.example.newpc.qrcode;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created to display text data from scan in a fragment
 */

public class AlertTextDialog extends DialogFragment {
    private String DataToDisplay;

    static AlertTextDialog newInstance(String Data) {
        AlertTextDialog box = new AlertTextDialog();

        // Supply data input as an argument.
        Bundle args = new Bundle();
        args.putString("data", Data);
        box.setArguments(args);

        return box;
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
                .setPositiveButton(R.string.copy, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText(DataToDisplay, DataToDisplay);
                        clipboard.setPrimaryClip(clip);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        return builder.create();
    }
}