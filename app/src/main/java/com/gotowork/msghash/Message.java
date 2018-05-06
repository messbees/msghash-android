package com.gotowork.msghash;

import android.widget.Toast;

import com.orm.SugarRecord;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sysop on 23.04.2018.
 */

public class Message extends SugarRecord<Message> {
    private String name, text, time, fullTime, hash;
    private boolean isPinned;

    public Message() {
    }

    public Message(String name, String text) {
        this.name = name;
        this.text = text;
        this.isPinned = false;
        setTime();
        setHash();
    }
    public Message (String name, String text, String time, String fullTime) {
        this.name = name;
        this.text = text;
        this.isPinned = false;
        this.fullTime = fullTime;
        this.time = time;
        setHash();
    }

    public boolean pin(KeyPair keyPair) {
        isPinned = true;
        save();
        try {
            Sawtooth.pin(keyPair, hash);
            return true;
        }
        catch (SignatureException e) {
            Toast.makeText(MainActivity.context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        catch (InvalidKeyException e) {
            Toast.makeText(MainActivity.context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        catch (NoSuchAlgorithmException e) {
            Toast.makeText(MainActivity.context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        catch (NoSuchProviderException e) {
            Toast.makeText(MainActivity.context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public boolean checkPinned() {
        return isPinned;
    }

    public boolean checkOld() {
        Date dateCurrent = Calendar.getInstance().getTime();
        Date dateMessage = new Date();
        String[] strings = fullTime.split(" ");
        String date = strings[0];
        try {
            dateMessage = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        }
        catch (Exception e) {

        }
        int temp = dateCurrent.compareTo(dateMessage);
        if (temp == 1)
            return true;
        else
            return false;
    }

    private void setTime() {
        int day, month, year;
        Date currentTime = Calendar.getInstance().getTime();
        Calendar calendar = Calendar.getInstance(Locale.getDefault());

        fullTime = "";
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        if (day < 10)
            fullTime += "0";
        fullTime += day;
        fullTime += "/";
        if (month < 10)
            fullTime += "0";
        fullTime += month;
        fullTime += "/";
        fullTime += year;

        time = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(calendar.get(Calendar.MINUTE));

        fullTime += time;

    }

    private void setHash() {
        String temp = "";
        temp += text;
        temp += name;
        temp += fullTime;
        try {
            temp = Hashing.getHash(temp.getBytes(StandardCharsets.UTF_8));
        }
        catch (NoSuchAlgorithmException e) {
            //sad =(
        }
        this.hash = temp.substring(64);
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getFullTime() {
        return fullTime;
    }

    public String getTime() {
        return time;
    }

    public String getHash() {
        return hash;
    }
}