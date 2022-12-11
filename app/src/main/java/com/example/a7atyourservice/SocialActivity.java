package com.example.a7atyourservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SocialActivity extends AppCompatActivity {
    private Toolbar sToolbar;
    private ViewPager sViewPager;
    private TabLayout sTabLayout;
    private FirebaseAuth user;
    private FirebaseUser currentUser;
    private String userName;
    private Button newGroup;
    private Button Groups;
    private DatabaseReference dataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.social);

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
                        return true;
                    case R.id.diet:
                        startActivity(new Intent(getApplicationContext(),DietActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        //initialize things:
        sToolbar = findViewById(R.id.social_tool_bar);
        setSupportActionBar(sToolbar);
        getSupportActionBar().setTitle("SmartFit");

        sTabLayout = findViewById(R.id.social_tabs);
        newGroup = findViewById(R.id.newGroupButton);

        dataRef = FirebaseDatabase.getInstance().getReference();

        newGroup.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //what to do to create group
                requestNewGroup();
            }
        });

        Groups = findViewById(R.id.Groups);
        Groups.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), GroupsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void requestNewGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SocialActivity.this, R.style.AlertDialog);
        builder.setTitle("Enter group name: ");
        final EditText groupNameField = new EditText(SocialActivity.this);
        builder.setView(groupNameField);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName= groupNameField.getText().toString();
                createNewGroup(groupName);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void createNewGroup(String groupName) {
        dataRef.child("Groups").child(groupName).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Helpers.showToast(SocialActivity.this, "group successfully created");
                } else {
                    Helpers.showToast(SocialActivity.this, "group unable to be created");
                }
            }
        });


    }


    @Override
    protected void onStart()
    {
        super.onStart();

        user = FirebaseAuth.getInstance();
        currentUser = user.getCurrentUser();
        userName = currentUser.toString();
    }

}