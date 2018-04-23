package com.gotowork.msghash;

/**
 * Created by sysop on 23.04.2018.
 */

public class Message {
    private String name, text, time, hash;
    private boolean isPinned;

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

    }

    private void setHash() {

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
