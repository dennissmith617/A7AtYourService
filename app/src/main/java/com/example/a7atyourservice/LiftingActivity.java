package com.example.a7atyourservice;

import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class LiftingActivity extends AppCompatActivity implements View.OnClickListener {
    private volatile boolean running = false;
    private Handler mainHandler = new Handler();
    private int n = 0;
    String startText = "Workout Length: 0 minutes";

    Button startWorkoutButton;
    Button endWorkoutButton;
    TextView timeSinceStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifting);

        startWorkoutButton = findViewById(R.id.startWorkout);
        startWorkoutButton.setOnClickListener(this);

        endWorkoutButton = findViewById(R.id.endWorkout);
        endWorkoutButton.setOnClickListener(this);

        timeSinceStart = findViewById(R.id.TimeSinceStart);

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.lifting);

        // Perform item selected listener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),SmartFitMainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.lifting:
                        return true;
                    case R.id.camera:
                        startActivity(new Intent(getApplicationContext(),CameraActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.social:
                        startActivity(new Intent(getApplicationContext(),SocialActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.diet:
                        startActivity(new Intent(getApplicationContext(),DietActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


    }


    public void startThread(View view) {
        CounterRunnable runnable = new CounterRunnable();
        new Thread(runnable).start();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.startWorkout:
                running = true;
                startThread(view);
                endWorkoutButton.setVisibility(View.VISIBLE);
                break;
            case R.id.endWorkout:
                running = false; // Flip to False to halt worker thread

                // end of workout, reset buttons + timer (maybe save state here?)
                startWorkoutButton.setVisibility(View.VISIBLE);
                endWorkoutButton.setVisibility(View.INVISIBLE);
                n = 0;
                timeSinceStart.setText(startText);

                break;
        }
    }

    class CounterRunnable implements Runnable {

        @Override
        public void run() {
            while(running) {
                // Change current number text based on n
                mainHandler.post(new Runnable(){
                    @Override
                    public void run() {
                        String timerPhrase =
                                String.format("Workout Length: " + n + " minutes");
                        timeSinceStart.setText(timerPhrase);
                    }
                });
                try{
                    Thread.sleep(60000);
                }catch(Exception e){
                    e.printStackTrace();
                }
                n += 1;
            }
        }
    }
}