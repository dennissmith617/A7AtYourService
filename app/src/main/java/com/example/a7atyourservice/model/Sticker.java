package com.example.a7atyourservice.model;

import java.util.Date;

public class Sticker {
    private String stickerName;
    private String fromUser;
    private long time;

    public Sticker(String stickerName, String fromUser){
        this.stickerName = stickerName;
        this.fromUser = fromUser;
        this.time = new Date().getTime();
    }

    public String getSticker() {
        return stickerName;
    }

    public void setStickerName(String stickerName) {
        this.stickerName = stickerName;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void serFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public long getTime() {
        return this.time;
    }
}
