package com.example.a7atyourservice.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by aniru on 2/18/2017.
 */

public class User {

    public String username;
    public int stickersSent;
    public int stickersRecieved;
    public String dateSent;
    public int smiley_sticker;
    public int party_sticker;


    public User() {
    }

    public User(String username, int stickersSent, int stickersRecieved) {
        this.username = username;
        this.stickersSent = stickersSent;
        this.stickersRecieved = stickersRecieved;
        this.dateSent = date();
    }

    public static String date() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        return ft.format(dNow);
    }

    public String getName(){
        return this.username;
    }


}