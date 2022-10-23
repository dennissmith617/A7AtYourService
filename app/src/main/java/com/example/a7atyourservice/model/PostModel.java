package com.example.a7atyourservice.model;

import com.google.gson.annotations.SerializedName;

public class PostModel {
    private String status;
    private EmailInfo data;

    public PostModel(String status, EmailInfo data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public EmailInfo getData() {
        return data;
    }

}
