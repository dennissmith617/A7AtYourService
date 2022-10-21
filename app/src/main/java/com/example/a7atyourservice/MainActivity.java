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

    public void startLaunchWebActivity(View view){
        startActivity(new Intent(MainActivity.this, com.example.a7atyourservice.LaunchWebActivity.class));
    }

    public void startWebViewActivity(View view){
        startActivity(new Intent(MainActivity.this, com.example.a7atyourservice.WebViewActivity.class));
    }

    public void startWebServiceActivity(View view){
        startActivity(new Intent(MainActivity.this, WebServiceActivity.class));
    }

    public void startNetworkInfoActivity(View view){
        startActivity(new Intent(MainActivity.this, NetworkInfoActivity.class));
    }

    public void startRetrofitActivity(View view){
        startActivity(new Intent(MainActivity.this, com.example.a7atyourservice.RetrofitActivity.class));
    }



}





