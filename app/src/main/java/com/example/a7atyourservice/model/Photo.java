package com.example.a7atyourservice.model;

public class Photo {

    private String compressedString;

    public Photo() {

    }

    public Photo(String compressedString){
        this.compressedString = compressedString;
    }

    public String getCompressedString(){
        return compressedString;
    }
    public void setCompressedString(String compressedString) { this.compressedString = compressedString;}
}
