package com.example.a7atyourservice;

import static com.google.firebase.firestore.model.Values.isInteger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.a7atyourservice.model.Foods;
import com.example.a7atyourservice.model.Goal;
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
    private View buttonAddBf;
    private View buttonAddLc;
    private View buttonAddDn;
    private View buttonAddSn;
    private boolean isFABOpen;
    private ArrayList<Foods> bfList;
    private ArrayList<Foods> lcList;
    private ArrayList<Foods> dnList;
    private ArrayList<Foods> snList;
    private RecyclerView bfView;
    private RecyclerView lcView;
    private RecyclerView dnView;
    private RecyclerView snView;
    private long time;
    private DietInfoAdapter bfAdapter;
    private DietInfoAdapter lcAdapter;
    private DietInfoAdapter dnAdapter;
    private DietInfoAdapter snAdapter;
    private EditText calGoal;
    private TextView calToday;
    private TextView calRemain;
    private TextView protToday;
    private TextView carbToday;
    private TextView fatToday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        buttonAddFood = findViewById(R.id.addFoodButton);
        buttonAddBf = findViewById(R.id.bfButton);
        buttonAddBf.setOnClickListener(this);
        buttonAddLc = findViewById(R.id.lcButton);
        buttonAddLc.setOnClickListener(this);
        buttonAddDn = findViewById(R.id.dnButton);
        buttonAddDn.setOnClickListener(this);
        buttonAddSn = findViewById(R.id.snButton);
        buttonAddSn.setOnClickListener(this);
        isFABOpen = false;
        buttonAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });

        // initiate recycle view and adapters for each meal
        bfList = new ArrayList<>();
        lcList = new ArrayList<>();
        dnList = new ArrayList<>();
        snList = new ArrayList<>();

        bfView = findViewById(R.id.rv_bf);
        bfView.setLayoutManager(new LinearLayoutManager(this));
        bfAdapter = new DietInfoAdapter(bfList, this);
        bfView.setAdapter(bfAdapter);
        lcView = findViewById(R.id.rv_lc);
        lcView.setLayoutManager(new LinearLayoutManager(this));
        lcAdapter = new DietInfoAdapter(lcList, this);
        lcView.setAdapter(lcAdapter);
        dnView = findViewById(R.id.rv_dn);
        dnView.setLayoutManager(new LinearLayoutManager(this));
        dnAdapter = new DietInfoAdapter(dnList, this);
        dnView.setAdapter(dnAdapter);
        snView = findViewById(R.id.rv_sn);
        snView.setLayoutManager(new LinearLayoutManager(this));
        snAdapter = new DietInfoAdapter(snList, this);
        snView.setAdapter(snAdapter);

        calGoal = findViewById(R.id.goalCals);
        calToday = findViewById(R.id.foodCals);
        calRemain = findViewById(R.id.remainingCals);
        protToday = findViewById(R.id.tv_protNow);
        carbToday = findViewById(R.id.tv_carbNow);
        fatToday = findViewById(R.id.tv_fatNow);

        Calendar today = Calendar.getInstance();
        today.set(Calendar.MILLISECOND, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.HOUR_OF_DAY, 0);
        time = today.getTimeInMillis();

        // display current goal; if no goal has been set, set it to default 2000 and keep in database
        Helpers.getCollectionReferenceForGoals().addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    Goal goal = value.toObject(Goal.class);
                    calGoal.setText(Integer. toString
                            (goal.getGoal()));
                    calRemain.setText(String.valueOf(
                            Integer.valueOf(calGoal.getText().toString()) -
                                    Integer.valueOf(calToday.getText().toString())));
                } else {
                    Helpers.getCollectionReferenceForGoals().set(new Goal(2000));
                }
            }

        });

        // set new goal to replace the old goal and keep in database
        calGoal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                calGoal.setCursorVisible(true);
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before,
                                      int count) {
            }
            @Override
            public void afterTextChanged(final Editable s) {
                calGoal.setSelection(calGoal.getText().toString().length());
                Helpers.getCollectionReferenceForGoals()
                        .update("goal",Integer.valueOf(calGoal.getText().toString()));
            }
        });


        // read and display diet data from database
        Helpers.getCollectionReferenceForDiets().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                bfList.clear();
                lcList.clear();
                dnList.clear();
                snList.clear();
                Log.e("QuerySnapshot",value.toString());
                List<DocumentSnapshot> datas = value.getDocuments();
                int cals = 0;
                int prot = 0;
                int carbs = 0;
                int fat = 0;
                for (int i = 0; i < datas.size(); i++) {
                    Foods foodsInfo = datas.get(i).toObject(Foods.class);
                    Log.e("foodsName",foodsInfo.getName());
                    long afterDay = time+24*60*60*1000;
                    if (foodsInfo.getTimestamp().getSeconds()*1000>time&&foodsInfo.getTimestamp().getSeconds()*1000<afterDay){
                        switch (foodsInfo.getMeal()){
                            case "Breakfast":
                                bfList.add(foodsInfo);
                                Log.e("bfAdd",foodsInfo.getName());
                                for (Foods food : bfList){
                                    cals += food.getCal();
                                    prot += food.getProtein();
                                    carbs += food.getCarb();
                                    fat += food.getFat();
                                }
                                protToday.setText(prot + " g");
                                carbToday.setText(carbs + " g");
                                fatToday.setText(fat + " g");
                                calToday.setText(String.valueOf(cals));
                                calRemain.setText(String.valueOf(
                                        Integer.valueOf(calGoal.getText().toString()) - cals));
                                bfAdapter.notifyDataSetChanged();
                                break;
                            case "Lunch":
                                lcList.add(foodsInfo);
                                Log.e("lcAdd",foodsInfo.getName());
                                for (Foods food : lcList){
                                    cals += food.getCal();
                                    prot += food.getProtein();
                                    carbs += food.getCarb();
                                    fat += food.getFat();
                                }
                                protToday.setText(prot + " g");
                                carbToday.setText(carbs + " g");
                                fatToday.setText(fat + " g");
                                calToday.setText(String.valueOf(cals));
                                calRemain.setText(String.valueOf(
                                        Integer.valueOf(calGoal.getText().toString()) - cals));
                                lcAdapter.notifyDataSetChanged();
                                break;
                            case "Dinner":
                                dnList.add(foodsInfo);
                                Log.e("dnAdd",foodsInfo.getName());
                                for (Foods food : dnList){
                                    cals += food.getCal();
                                    prot += food.getProtein();
                                    carbs += food.getCarb();
                                    fat += food.getFat();
                                }
                                protToday.setText(prot + " g");
                                carbToday.setText(carbs + " g");
                                fatToday.setText(fat + " g");
                                calToday.setText(String.valueOf(cals));
                                calRemain.setText(String.valueOf(
                                        Integer.valueOf(calGoal.getText().toString()) - cals));
                                dnAdapter.notifyDataSetChanged();
                                break;
                            case "Snacks":
                                snList.add(foodsInfo);
                                Log.e("snAdd",foodsInfo.getName());
                                for (Foods food : snList){
                                    cals += food.getCal();
                                    prot += food.getProtein();
                                    carbs += food.getCarb();
                                    fat += food.getFat();
                                }
                                protToday.setText(prot + " g");
                                carbToday.setText(carbs + " g");
                                fatToday.setText(fat + " g");
                                calToday.setText(String.valueOf(cals));
                                calRemain.setText(String.valueOf(
                                        Integer.valueOf(calGoal.getText().toString()) - cals));
                                snAdapter.notifyDataSetChanged();
                                break;
                        }
                    }
                }
            }
        });


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

    // FAB menu animation helper function - open
    private void showFABMenu(){
        isFABOpen=true;
        buttonAddBf.animate().translationY(-560);
        findViewById(R.id.tv_bf).setVisibility(View.VISIBLE);
        buttonAddLc.animate().translationY(-420);
        findViewById(R.id.tv_lc).setVisibility(View.VISIBLE);
        buttonAddDn.animate().translationY(-280);
        findViewById(R.id.tv_dn).setVisibility(View.VISIBLE);
        buttonAddSn.animate().translationY(-140);
        findViewById(R.id.tv_sn).setVisibility(View.VISIBLE);
    }

    // FAB menu animation helper function - close
    private void closeFABMenu(){
        isFABOpen=false;
        buttonAddBf.animate().translationY(0);
        findViewById(R.id.tv_bf).setVisibility(View.INVISIBLE);
        buttonAddLc.animate().translationY(0);
        findViewById(R.id.tv_lc).setVisibility(View.INVISIBLE);
        buttonAddDn.animate().translationY(0);
        findViewById(R.id.tv_dn).setVisibility(View.INVISIBLE);
        buttonAddSn.animate().translationY(0);
        findViewById(R.id.tv_sn).setVisibility(View.INVISIBLE);
    }

    // set up FAB menu listener
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bfButton:
                popUpWindow("Breakfast");
                break;
            case R.id.lcButton:
                popUpWindow("Lunch");
                break;
            case R.id.dnButton:
                popUpWindow("Dinner");
                break;
            case R.id.snButton:
                popUpWindow("Snacks");
                break;
        }
    }

   // promp input for the food
    public void popUpWindow(String meal){
        final String myMeal = meal;
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
                        saveMeal(myMeal, food.getText().toString(),
                                Integer.valueOf(fat.getText().toString()),
                                Integer.valueOf(carb.getText().toString()),
                                Integer.valueOf(prot.getText().toString()));
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // save food to the database
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