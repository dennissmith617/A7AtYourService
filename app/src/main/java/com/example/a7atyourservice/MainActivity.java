package com.example.a7atyourservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startRetrofitActivity(View view){
        startActivity(new Intent(MainActivity.this, com.example.a7atyourservice.RetrofitActivity.class));
    }

    public void starStickItActivity(View view){
        startActivity(new Intent(MainActivity.this, com.example.a7atyourservice.StickItActivity.class));
    }

    public void openAboutScreen(View view) {
        startActivity(new Intent(MainActivity.this, com.example.a7atyourservice.InfoScreen.class));
    }
}





