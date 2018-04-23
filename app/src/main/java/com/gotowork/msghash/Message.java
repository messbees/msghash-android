package com.gotowork.msghash;

import com.orm.SugarRecord;

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

    public void pin() {
        isPinned = true;
    }

    private void setTime() {
        Date currentTime = Calendar.getInstance().getTime();
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        time = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(calendar.get(Calendar.MINUTE));
        fullTime = currentTime.toString();
    }

    private void setHash() {
        String temp = "";
        temp += name;
        temp += text;
        temp += time;
        temp = Hashing.getHash(temp);
        this.hash = temp;
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
