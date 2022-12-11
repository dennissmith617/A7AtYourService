package com.example.a7atyourservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.a7atyourservice.model.Check;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SmartFitMainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private Button checkinButton;
    private Button finishButton;
    private TextView completion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sf_main);

        // set up calendar
        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Intent intent = new Intent(SmartFitMainActivity.this, DayActivity.class);
                intent.putExtra("day",eventDay.getCalendar().getTimeInMillis());
                startActivity(intent);
            }
        });

        // set up check in button
        checkinButton = findViewById(R.id.checkinButton);
        finishButton = findViewById(R.id.finishButton);
        checkinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("Check")
                        . document(FirebaseAuth.getInstance().getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot snapshot = task.getResult();
                                    List<String> list = null;
                                    try {
                                        if (snapshot != null && snapshot.exists()) {
                                            Check check = snapshot.toObject(Check.class);
                                            list = check.time;
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    if (list==null){
                                        list = new ArrayList<>();
                                    }

                                    Date totay = new Date();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String todayStr = sdf.format(totay);
                                    if (list.contains(todayStr)){
                                        return;
                                    }
                                    list.add(todayStr);
                                    Map<String, Object> data =  new HashMap<>();
                                    data.put("uid",FirebaseAuth.getInstance().getUid());
                                    data.put("time",list);
                                    Helpers.getCheckRef().set(data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            });
                                }
                            }
                        });

            }
        });

        // add checked in dates and completion
        completion = findViewById(R.id.tv_comp);
        FirebaseFirestore.getInstance().collection("Check")
                . document(FirebaseAuth.getInstance().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.exists()) {
                            List<String> list = null;
                            try {
                                Check check = value.toObject(Check.class);
                                list = check.time;
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            if (list != null) {
                                Date today = stringToDate(
                                        new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
                                List<EventDay> events = new ArrayList<>();
                                completion.setText(Integer.toString(list.size()));
                                for (int i = list.size() - 1; i >= 0; i--) {
                                    String temp = list.get(i);
                                    Date date = stringToDate(temp);
                                    // show completed banner if already checked in
                                    if (date.compareTo(today) == 0){
                                        finishButton.setVisibility(View.VISIBLE);
                                    }
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);
                                    events.add(new EventDay(calendar, R.drawable.check));
                                }
                                calendarView.setEvents(events);
                            }
                        }
                    }
                });


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

    // helper method to transform date from string format to date obj
    public  Date stringToDate(String strTime)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
