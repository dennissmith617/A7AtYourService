package com.example.a7atyourservice.model;

import com.google.firebase.Timestamp;

public class Foods {

    private String name;
    private String meal;
    private int fat;
    private int carb;
    private int protein;
    private int calories;
    private Timestamp timestamp;

    public Foods(String meal, String name, int fat, int carb, int protein){
        this.meal = meal;
        this.name = name;
        this.fat = fat;
        this.carb = carb;
        this.protein = protein;
    }

    public Foods(){

    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getFat(){
        return fat;
    }

    public int getCarb(){
        return carb;
    }

    public int getProtein(){
        return protein;
    }

    public int getCal(){
        this.calories = fat * 9 + carb * 4 + protein * 4;
        return this.calories;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }
}
