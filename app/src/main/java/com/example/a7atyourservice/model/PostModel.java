package com.example.a7atyourservice.model;

import com.google.gson.annotations.SerializedName;

public class PostModel {
    private String activity;
    private String type;
    private int participants;

    //We can have different name, just use specify which field is to be associated as below

    @SerializedName("body")
    private String text;

    public PostModel(String activity, String type, int participants) {
        this.activity = activity;
        this.type = type;
        this.participants = participants;
    }

    // declare
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
