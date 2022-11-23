package com.example.a7atyourservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button loginButton;
    ProgressBar progressBar;
    TextView createAccountBtnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progressBar);
        createAccountBtnView = findViewById(R.id.create_acct_view_btn);

        loginButton.setOnClickListener(v -> loginUser());
        createAccountBtnView.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class)));
    }

    private void loginUser() {
        String email_text = email.getText().toString();
        String password_text = password.getText().toString();

        boolean isValidated = validateInput(email_text,password_text);

        if(!isValidated) {
            return;
        }

        loginAccountFirebase(email_text, password_text);
    }

    private void loginAccountFirebase(String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()) {
                    if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                        startActivity(new Intent(LoginActivity.this, SmartFitMainActivity.class));
                    } else {
                        Helpers.showToast(LoginActivity.this, "Email not verified. Check email to verify!");
                    }
                } else {
                    Helpers.showToast(LoginActivity.this, task.getException().getLocalizedMessage());
                }
            }
        });
    }

    private void changeInProgress(boolean inProgress) {
        if(inProgress) {
            progressBar.setProgress(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
        } else {
            progressBar.setProgress(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }

    private boolean validateInput(String email_string, String password_text) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email_string).matches()) {
            email.setError("Email is invalid");
            return false;
        }
        if (password_text.length() < 6){
            password.setError("Password length if not valid");
            return false;
        }
        return true;
    }
}