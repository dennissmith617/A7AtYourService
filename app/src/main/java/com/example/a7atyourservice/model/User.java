package com.example.a7atyourservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by aniru on 2/18/2017.
 */

public class User {

    public String username;
    public int stickersSent;
    public int stickersRecieved;
    public String dateSent;


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