package com.example.a7atyourservice.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Message {

    public String stickerName;
    public String fromUser;
    public String toUser;
    public String messageTime;

    public Message() {

    }

    public Message (String stickerName, String fromUser, String toUser) {
        this.stickerName = stickerName;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.messageTime = dateTime();
    }

    public static String dateTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatted = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(dateTimeFormatted);
    }
}
