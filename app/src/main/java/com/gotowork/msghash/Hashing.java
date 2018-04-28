package com.gotowork.msghash;

import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Created by sysop on 23.04.2018.
 */

// !!! Hashing algorhitm is SHA-512 !!!

public class Hashing {
    public static String getHash(String string) {
        String sha512 = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");

            byte[] digest = messageDigest.digest(string.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                stringBuilder.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }
            sha512 = stringBuilder.toString();
            //Toast.makeText(MainActivity.context, sha512, Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(MainActivity.context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
        return sha512;
    }

    public static int getHexHash (String string) {
        int hexInt = 0;
        String hash = getHash(string);
        String hex = String.format("%040x", new BigInteger(1, hash.getBytes(StandardCharsets.UTF_8)));
        try {
            hexInt = Integer.parseInt(hex);
        }
        catch (Exception e) {
            Toast.makeText(MainActivity.context, e.toString()+" "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
        return hexInt;
    }
}
