package com.example.a7atyourservice;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SmartFitMainActivity extends AppCompatActivity {

    EditText email, password;
    Button checkInButton;
    ProgressBar progressBar;

    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sf_main);

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

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        checkInButton = findViewById(R.id.checkin);
        progressBar = findViewById(R.id.progressBar);

        checkInButton.setOnClickListener(v -> checkInUser());

    }

    public void checkInUser() {
        String email_text = email.getText().toString();
        String password_text = password.getText().toString();

        boolean isValidated = validateInput(email_text,password_text);

        if(!isValidated) {
            return;
        }

        createFireBaseAccount(email_text, password_text);
    }

    public void createFireBaseAccount(String email, String password) {
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                SmartFitMainActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if(task.isSuccessful()) {
                            Toast.makeText(SmartFitMainActivity.this, "Successfully created an account, Check Email to verify", Toast.LENGTH_SHORT).show();
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        } else {
                            Toast.makeText(SmartFitMainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

    }

    public void changeInProgress(boolean inProgress) {
        if(inProgress) {
            progressBar.setProgress(View.VISIBLE);
            checkInButton.setVisibility(View.GONE);
        } else {
            progressBar.setProgress(View.GONE);
            checkInButton.setVisibility(View.VISIBLE);
        }
    }

    public boolean validateInput(String email_string, String password_text) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email_string).matches()) {
            email.setError("Email is invalid");
            return false;
        }
        if (password_text.length() < 6){
            password.setError("Password length if not valid");
            return  false;
        }
        return true;
    }
}
