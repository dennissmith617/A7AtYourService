package com.example.a7atyourservice;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import com.applandeo.materialcalendarview.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.applandeo.materialcalendarview.EventDay;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SmartFitMainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private Button checkinButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sf_main);
        // set up calendar
        calendarView = findViewById(R.id.calendarView);
        checkinButton = findViewById(R.id.checkinButton);

        // add checked in dates
        List<EventDay> events = new ArrayList<>();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2022,11,1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2022,11,4);
        events.add(new EventDay(calendar1, R.drawable.check));
        events.add(new EventDay(calendar2, R.drawable.check));
        calendarView.setEvents(events);

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        // Perform item selected listener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        return true;
                    case R.id.lifting:
                        startActivity(new Intent(getApplicationContext(),LiftingActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.camera:
                        startActivity(new Intent(getApplicationContext(), CameraActivity.class));
                        overridePendingTransition(0, 0);
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
}
