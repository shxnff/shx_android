package com.shx.base.bean;

import android.text.TextUtils;

public class MessageEvent {

    private int type;
    private String message;

    public MessageEvent() {}

    public MessageEvent(int type) {
        this.type = type;
    }

    public MessageEvent(int type,String message) {
        this.type = type;
        this.message = message;
    }

    public String getMessage() {
        return TextUtils.isEmpty(message)?"":message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

