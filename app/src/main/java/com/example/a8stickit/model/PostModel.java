package com.example.a8stickit.model;


public class PostModel {
    private String status;
    private EmailInfo data;

    // Get response data from the email API
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
