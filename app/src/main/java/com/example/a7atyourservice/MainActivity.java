package com.example.a7atyourservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        // Set button for main activity screen
        Button atyoursvcbtn = findViewById(R.id.atyoursvcbtn);
        atyoursvcbtn.setOnClickListener(this);
    }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.atyoursvcbtn) {
                openServiceScreen();
            }
        }

    public void openServiceScreen() {
        Intent svcIntent = new Intent(this, Service.class);
        startActivity(svcIntent);
    }

}
