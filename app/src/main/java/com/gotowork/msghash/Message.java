package com.gotowork.msghash;

import com.orm.SugarRecord;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by sysop on 23.04.2018.
 */

public class Message extends SugarRecord<Message> {
    String name, text, time, hash;
    boolean isPinned;

    public Message() {
    }

    public Message(String name, String text) {
        this.name = name;
        this.text = text;
        this.isPinned = false;
        setTime();
        setHash();
    }

    public void pin(){
        isPinned = true;
    }

    private void setTime() {
        Date currentTime = Calendar.getInstance().getTime();
        time = currentTime.toString();
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

    public String getTime() {
        return time;
    }

    public String getHash() {
        return hash;
    }
}
