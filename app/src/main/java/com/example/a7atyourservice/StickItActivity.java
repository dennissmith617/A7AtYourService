package com.example.a7atyourservice;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StickItActivity extends AppCompatActivity {

    private static final String TAG = StickItActivity.class.getSimpleName();

    private DatabaseReference mDatabase;
    private ImageButton smiley;
    private Button loginButton;
    private EditText loginText;
    private TextView usernameView;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stickit);

        // Connect with firebase
        //
        mDatabase = FirebaseDatabase.getInstance().getReference();

        loginText = (EditText) findViewById(R.id.username);
        loginButton = findViewById(R.id.login);
        smiley = findViewById(R.id.firebaseImage);
        usernameView = findViewById(R.id.username_view);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameView.setText(loginText.getText().toString());
                smiley.setVisibility(View.VISIBLE);
            }
        });


        smiley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStickerToDB(usernameView.getText().toString(), smiley.getTransitionName());
            }
        });
    }

    public void addStickerToDB(String username, String image_id) {
        // Write a message to the database
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(String.valueOf(username));

        Task t = myRef.setValue(image_id);
        if (!t.isSuccessful()) {
            Toast.makeText(getApplicationContext()
                    , "Failed to write value into firebase. ", Toast.LENGTH_SHORT).show();
        }
    }
}
