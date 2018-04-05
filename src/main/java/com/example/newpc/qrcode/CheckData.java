package com.example.newpc.qrcode;

import java.net.URL;

/** Created just to check whether data from QRCode Scanner is URL or not
 *
 */
public class CheckData{

    public static boolean isDataUrl(String Data){
        try {
            new URL(Data).toURI();
            return true;
        }

        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }

}
