package com.example.a7atyourservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startSmartFitMainActivity(View view){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(MainActivity.this, com.example.a7atyourservice.LoginActivity.class));
        } else {
            startActivity(new Intent(MainActivity.this, com.example.a7atyourservice.SmartFitMainActivity.class));
        }
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





