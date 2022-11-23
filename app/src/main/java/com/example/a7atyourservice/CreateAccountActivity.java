package com.example.a7atyourservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountActivity extends AppCompatActivity {

    EditText email, password, confirmPassword;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginButtonView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        confirmPassword = findViewById(R.id.confirm_login_password);
        createAccountBtn = findViewById(R.id.checkin);
        progressBar = findViewById(R.id.progressBar);
        loginButtonView = findViewById(R.id.login_view_btn);

        createAccountBtn.setOnClickListener(v -> checkInUser());
        loginButtonView.setOnClickListener(v -> startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class)));

    }

    public void checkInUser() {
        String email_text = email.getText().toString();
        String password_text = password.getText().toString();
        String confirm_password_text = confirmPassword.getText().toString();

        boolean isValidated = validateInput(email_text,password_text, confirm_password_text);

        if(!isValidated) {
            return;
        }

        createFireBaseAccount(email_text, password_text);
    }

    public void createFireBaseAccount(String email, String password) {
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                CreateAccountActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if(task.isSuccessful()) {
                            Helpers.showToast(CreateAccountActivity.this, "Successfully created an account, Check Email to verify");
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        } else {
                            Helpers.showToast(CreateAccountActivity.this, task.getException().getLocalizedMessage());
                        }
                    }
                }
        );

    }

    public void changeInProgress(boolean inProgress) {
        if(inProgress) {
            progressBar.setProgress(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        } else {
            progressBar.setProgress(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
    }

    public boolean validateInput(String email_string, String password_text, String confirm_password_text) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email_string).matches()) {
            email.setError("Email is invalid");
            return false;
        }
        if (password_text.length() < 6){
            password.setError("Password length if not valid");
            return false;
        }
        if (!confirm_password_text.equals(password_text)) {
            confirmPassword.setError("Passwords do not match");
            return false;
        }
        return true;
    }

}