package com.example.a7atyourservice;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.a7atyourservice.User;

import java.util.ArrayList;

public class StickItActivity extends AppCompatActivity {

    private static final String TAG = StickItActivity.class.getSimpleName();

    private DatabaseReference mDatabase;
    private ImageButton smiley;
    private Button loginButton;
    private EditText loginText;
    private TextView usernameView;
    private TextView friendsList;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stickit);

        // Connect with firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        loginText = (EditText) findViewById(R.id.username);
        loginButton = findViewById(R.id.login);
        smiley = findViewById(R.id.firebaseImage);
        usernameView = findViewById(R.id.username_view);
        friendsList = findViewById(R.id.friends_list);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser(loginText.getText().toString());
            }
        });


        smiley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStickerToDB(usernameView.getText().toString(), smiley.getTransitionName());
            }
        });

        // Update the friends list in realtime
        mDatabase.child("users").addChildEventListener(
                new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        displayFriends(dataSnapshot);
                        Log.e(TAG, "onChildAdded: dataSnapshot = " + dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.v(TAG, "onChildChanged: " + dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled:" + databaseError);
                        Toast.makeText(getApplicationContext()
                                , "DBError: " + databaseError, Toast.LENGTH_SHORT).show();
                    }
                }
        );
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

    // Add Users to DB
    public void addUser(String username) {

        User user;
        user = new User(username, "0");
        Task t1 = mDatabase.child("users").child(user.username).setValue(user);

        if(!t1.isSuccessful()){
            Toast.makeText(getApplicationContext(),"Unable to login!",Toast.LENGTH_SHORT).show();
        }
        String displayText = "username: " + loginText.getText().toString();
        usernameView.setText(displayText);
        smiley.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.INVISIBLE);
        loginText.setVisibility(View.INVISIBLE);
    }

    public void displayFriends(DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        String current_friends = friendsList.getText().toString();
        String new_friends_list = current_friends + ", " + user.username;
        friendsList.setText(new_friends_list);
    }
}
