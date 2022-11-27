package com.example.a7atyourservice;

import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.a7atyourservice.model.LiftInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;


public class LiftingActivity extends AppCompatActivity implements View.OnClickListener {
    private volatile boolean running = false;
    private Handler mainHandler = new Handler();
    private int n = 0;
    String startText = "Workout Length: 0 minutes";

    Button startWorkoutButton;
    Button endWorkoutButton;
    TextView timeSinceStart;
    EditText liftEditText;
    EditText weightEditText;
    EditText repsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifting);

        startWorkoutButton = findViewById(R.id.startWorkout);
        startWorkoutButton.setOnClickListener(this);

        endWorkoutButton = findViewById(R.id.endWorkout);
        endWorkoutButton.setOnClickListener(this);

        timeSinceStart = findViewById(R.id.timeSinceStart);

        liftEditText = findViewById(R.id.indivliftname);
        weightEditText = findViewById(R.id.repsNum);
        repsEditText = findViewById(R.id.weightNum);


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


                // Save to DB
                saveWorkout(n);

                // end of workout, reset buttons + timer
                startWorkoutButton.setVisibility(View.VISIBLE);
                endWorkoutButton.setVisibility(View.INVISIBLE);
                n = 0;
                timeSinceStart.setText(startText);
                break;
        }
    }

    private void saveWorkout(int workoutLen) {
        String liftName = liftEditText.getText().toString();

        String weightString = weightEditText.getText().toString();
        int weight=Integer.parseInt(weightString);

        String repString = repsEditText.getText().toString();
        int reps =Integer.parseInt(repString);

        LiftInfo liftInfo = new LiftInfo();
        liftInfo.setLiftName(liftName);
        liftInfo.setReps(reps);
        liftInfo.setWeight(weight);
        liftInfo.setTimestamp(Timestamp.now());
        liftInfo.setLength(workoutLen);

        saveWorkoutToFirebase(liftInfo);
    }

    private void saveWorkoutToFirebase(LiftInfo liftInfo) {
        DocumentReference documentReference;
        documentReference = Helpers.getCollectionReferenceForLifting().document();
        documentReference.set(liftInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Helpers.showToast(LiftingActivity.this, "Workout successfully added");
                } else {
                    Helpers.showToast(LiftingActivity.this, "Workout unable to be added");
                }
            }
        });
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