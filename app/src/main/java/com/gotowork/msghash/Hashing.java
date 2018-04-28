package com.gotowork.msghash;

import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Created by sysop on 23.04.2018.
 */

public class Hashing {
    public static String getHash(String string)
    {
        String sha512 = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");

            byte[] digest = messageDigest.digest(string.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                stringBuilder.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }
            sha512 = stringBuilder.toString();
            Toast.makeText(MainActivity.context, sha512, Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(MainActivity.context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
        //Todo: add something like .hexdigest()
        return sha512;
    }
}
