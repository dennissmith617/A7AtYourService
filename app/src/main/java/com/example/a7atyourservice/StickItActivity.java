package com.example.a7atyourservice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.a7atyourservice.User;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;

public class StickItActivity extends AppCompatActivity {

    private static final String TAG = StickItActivity.class.getSimpleName();

    private DatabaseReference mDatabase;
    private ImageButton smiley;
    private Button loginButton;
    private Button sendButton;
    private EditText loginText;
    private TextView usernameView;
    private TextView friendsList;
    private EditText recipientText;

    private String selectedSticker;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
        setContentView(R.layout.activity_stickit);

        // Connect with firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Create notification channel
        createNotificationChannel();

        loginText = (EditText) findViewById(R.id.username);
        loginButton = findViewById(R.id.login);
        sendButton = findViewById(R.id.send_button);
        smiley = findViewById(R.id.smiley_sticker);
        usernameView = findViewById(R.id.username_view);
        friendsList = findViewById(R.id.friends_list);
        recipientText = findViewById(R.id.recipient);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser(loginText.getText().toString());
            }
        });


        smiley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectSticker(smiley);
            }
        });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSticker(usernameView.getText().toString(), recipientText.getText().toString());
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
                        sendNotification();
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

    public void selectSticker(ImageButton sticker) {
        selectedSticker = sticker.getTransitionName();
        // selected sticker is tinted red
        sticker.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
    }

    public void sendSticker(String curr_username, String friend_username) {
        DatabaseReference curr_user = mDatabase.child("users").child(curr_username);
        DatabaseReference to_user = mDatabase.child("users").child(friend_username);

        // increment
        DatabaseReference curr_user_sent_count= curr_user.child("stickersSent");
        DatabaseReference to_user_sticker_count = to_user.child("stickersRecieved");


        curr_user_sent_count.setValue(ServerValue.increment(1));
        to_user_sticker_count.setValue(ServerValue.increment(1));
    }

    // Add Users to DB
    public void addUser(String username) {

        User user;
        // Start off with no stickers
        user = new User(username, 0, 0);
        Task t1 = mDatabase.child("users").child(user.username).setValue(user);

        if(!t1.isSuccessful()){
            Toast.makeText(getApplicationContext(),"Unable to login!",Toast.LENGTH_SHORT).show();
        }
        String displayText = "username: " + loginText.getText().toString();

        // Hide Login Info
        loginButton.setVisibility(View.INVISIBLE);
        loginText.setVisibility(View.INVISIBLE);

        // Show sticker "view"
        usernameView.setText(displayText);
        smiley.setVisibility(View.VISIBLE);
        recipientText.setVisibility(View.VISIBLE);
        sendButton.setVisibility(View.VISIBLE);
    }

    public void displayFriends(DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        String current_friends = friendsList.getText().toString();
        String new_friends_list = "Friends: " + current_friends + user.username + ", ";
        friendsList.setText(new_friends_list);
    }

    // code here from -> https://developer.android.com/develop/ui/views/notifications/build-notification
    private void createNotificationChannel() {
        // Create the NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.channel_id), name, importance);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void sendNotification() {


        // Sticker notification
        String channelId = getString(R.string.channel_id);
        NotificationCompat.Builder notifyBuild = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.party)
                .setContentTitle("New sticker alert!")
                .setContentText("Subject")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                // hide the notification after its selected
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, notifyBuild.build());

    }

    //TODO: screen rotation support
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    //TODO: screen rotation support
    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }


    //TODO: implement notification sending, move this implementation to database listener, or leave it here
    //Yufeng: I'm thinking about including the sticker image in the notification. Need some predefined images in res.
    public void sendNotification(View view) {
        //TODO: customize intent based on notification options
        PendingIntent notificationIntent = PendingIntent.getActivity(this, (int)System.currentTimeMillis(),
                new Intent(this, StickItActivity.class), 0);

        //TODO: create and send notification, see https://developer.android.com/develop/ui/views/notifications/build-notification
        //TODO: to use additional styling with images, see https://developer.android.com/develop/ui/views/notifications#Templates
        //Note that the dev website uses Compat classes for notification and manager,
        //as opposed to what was shown in lecture,
        //the methods we are using is follow the same pattern.
    }

    //TODO: sticker tap-select, can be its own activity depending on layout and image size.
}
