package com.example.a7atyourservice;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aniru on 2/18/2017.
 */

public class User {

    public String username;
    public String stickersSent;
    public String dateSent;


    public User() {
    }

    public User(String username, String stickersSent) {
        this.username = username;
        this.stickersSent = stickersSent;
        this.dateSent = date();
    }

    public static String date() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        return ft.format(dNow);
    }

}