package com.example.a7atyourservice.model;

import com.google.gson.annotations.SerializedName;

public class PostModel {
    private String activity;
    private String type;
    private int participants;

    @SerializedName("body")
    private String text;

    public PostModel(String activity, String type, int participants) {
        this.activity = activity;
        this.type = type;
        this.participants = participants;
    }

    public String getActivity() {
        return activity;
    }

    public String getType() {
        return type;
    }

    public int getParticipants() {
        return participants;
    }

}
