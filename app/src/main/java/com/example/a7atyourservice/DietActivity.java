package com.example.a7atyourservice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.a7atyourservice.model.Foods;
import com.example.a7atyourservice.model.LiftInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DietActivity extends AppCompatActivity implements View.OnClickListener{
    private FloatingActionButton buttonAddFood;
    private Spinner mealSpinner;
    private ArrayList<Foods> foodsList;
    private RecyclerView foodsView;
    private Context context = this;
    private long time;
    private DietInfoAdapter adapter;
    private TextView calToday;
    private TextView calRemain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        buttonAddFood = findViewById(R.id.addFoodButton);
        buttonAddFood.setOnClickListener(this);

        foodsList = new ArrayList<>();

        foodsView = findViewById(R.id.mealRecycleView1);
        foodsView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DietInfoAdapter(foodsList, this);
        foodsView.setAdapter(adapter);

        calToday = findViewById(R.id.foodCals);
        calRemain = findViewById(R.id.remainingCals);

        Calendar today = Calendar.getInstance();
        today.set(Calendar.MILLISECOND, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.HOUR_OF_DAY, 0);
        time = today.getTimeInMillis();

        // read and display diet data from database
        Helpers.getCollectionReferenceForDiets().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                foodsList.clear();
                Log.e("QuerySnapshot",value.toString());
                List<DocumentSnapshot> datas = value.getDocuments();
                for (int i = 0; i < datas.size(); i++) {
                    Foods foodsInfo = datas.get(i).toObject(Foods.class);
                    Log.e("foodsName",foodsInfo.getName());
                    long afterDay = time+24*60*60*1000;
                    if (foodsInfo.getTimestamp().getSeconds()*1000>time&&foodsInfo.getTimestamp().getSeconds()*1000<afterDay){
                        foodsList.add(foodsInfo);
                        int cals = 0;
                        for (Foods food : foodsList){
                            cals += food.getCal();
                        }
                        calToday.setText(String.valueOf(cals));
                        calRemain.setText(String.valueOf(2000 - cals));
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

//        mealSpinner = (Spinner) findViewById(R.id.mealSpinner);
//        // set up the meal spinner
//        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
//                .createFromResource(this, R.array.meal_array,
//                        android.R.layout.simple_spinner_item);
//        staticAdapter
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mealSpinner.setAdapter(staticAdapter);



        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.diet);

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
                        startActivity(new Intent(getApplicationContext(),LiftingActivity.class));
                        overridePendingTransition(0,0);
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
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this);
        builder.setTitle("Add a new food");

        final View customLayout
                = getLayoutInflater().inflate(R.layout.add_food, null);
        builder.setView(customLayout);
        builder.setPositiveButton("Save",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText food = customLayout.findViewById(R.id.tv_foodEdit);
                        EditText fat = customLayout.findViewById(R.id.tv_fatEdit);
                        EditText carb = customLayout.findViewById(R.id.tv_carbEdit);
                        EditText prot = customLayout.findViewById(R.id.tv_ProtEdit);
                        saveMeal("meal", food.getText().toString(),
                                Integer.valueOf(fat.getText().toString()),
                                Integer.valueOf(carb.getText().toString()),
                                Integer.valueOf(prot.getText().toString()));
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveMeal(String meal, String name, int fat, int carb, int protein){
        DocumentReference documentReference;
        documentReference = Helpers.getCollectionReferenceForDiets().document();
        Foods foodToSave = new Foods(meal, name, fat, carb, protein);
        foodToSave.setTimestamp(Timestamp.now());
        documentReference.set(foodToSave).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Helpers.showToast(DietActivity.this, "food successfully added");
                } else {
                    Helpers.showToast(DietActivity.this, "food unable to be added");
                }
            }
        });
    }
}