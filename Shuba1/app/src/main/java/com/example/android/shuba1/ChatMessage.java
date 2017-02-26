package com.example.android.shuba1;

import java.util.Date;

/**
 * Created by Kennedy on 26/02/2017.
 */

public class ChatMessage {

    private String fromId;
    private String msg;
    private String name;
    private Long timestamp;

    public ChatMessage(String msg, String name, String fromId) {
        this.msg = msg;
        this.name = name;
        this.fromId = fromId;

        // Initialize to current time
        timestamp = new Date().getTime();
    }

    public ChatMessage(){

    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
