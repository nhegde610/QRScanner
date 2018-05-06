package com.example.newpc.qrcode;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.Manifest;


import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;


public class GalleryReader extends FragmentActivity implements ActivityCompat.OnRequestPermissionsResultCallback,
        PasswordInputDialog.OnDialogDismissListener{

    private String contents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CheckForPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 3){
            // Request for external storage read
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start photo pick activity
                startPhotoPick();
            } else {
                // Permission request was denied.
                Toast.makeText(this,R.string.permission_denied,Toast.LENGTH_LONG).show();
            }
        }
    }

    private void CheckForPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                startPhotoPick();

            } else {
                Log.v("qrcode","Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("qrcode","Permission is granted1");
        }
    }

    private void startPhotoPick(){

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        int SELECT_PHOTO = 1;
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    /*
    @Override
    protected void onPause() {
        super.onPause();
        GalleryReader.this.finish();
    }*/
    /*
    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Here we need to check if the activity that was triggers was the Image Gallery.
        // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
        // If the resultCode is RESULT_OK and there is some data we know that an image was picked.
        int LOAD_IMAGE_RESULTS = 1;
        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();

            // Do something with the bitmap

            int[] intArray = new int[bitmap.getWidth()*bitmap.getHeight()];
//copy pixel data from the Bitmap into the 'intArray' array
            bitmap.getPixels(intArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

            LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), intArray);
            BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));

            Reader reader = new MultiFormatReader();
            Result result = null;
            try {
                result = reader.decode(bitmap1);
            } catch (NotFoundException | FormatException | ChecksumException e) {
                e.printStackTrace();
            }

            contents = result.getText();
            String SubDataString = contents.substring(0,4);

            if(SubDataString.equals("ENC;")){

                contents = DecryptionData.RemoveEncrypt(contents);
                DialogFragment alertDialogAndroid = new PasswordInputDialog();
                alertDialogAndroid.show(getSupportFragmentManager(),"PasswordDialog");
            }
            else {

                if(CheckData.isDataUrl(contents)){

                    Intent RedirectCheck = new Intent(this, RedirectService.class);
                    RedirectCheck.putExtra("url", contents);
                    this.startService(RedirectCheck);
                }
                else {
                    DialogFragment TextFragment = AlertTextDialog.newInstance(contents);
                    TextFragment.show(getSupportFragmentManager(), "DataFromScan");
                }
            }
        }
    }

    @Override
    public void onDialogDismissListener(String secret) {

        contents = DecryptionData.decrypt(contents,secret);

        if(CheckData.isDataUrl(contents)){

            Intent RedirectCheck = new Intent(this, RedirectService.class);
            RedirectCheck.putExtra("url", contents);
            this.startService(RedirectCheck);
        }
        else {
            DialogFragment TextFragment = AlertTextDialog.newInstance(contents);
            TextFragment.show(getSupportFragmentManager(), "DataFromScan");
        }

    }
}