package com.example.a7atyourservice;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.a7atyourservice.User;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StickItActivity extends AppCompatActivity {

    private static final String TAG = StickItActivity.class.getSimpleName();

    private DatabaseReference mDatabase;
    private ImageButton smiley;
    private TextView smileySent;
    private Button loginButton;
    private Button sendButton;
    private Button historyButton;
    private EditText loginText;
    private TextView usernameView;
    private TextView friendsList;
    private TextView stickerSentTv;
    private EditText recipientText;
    private String selectedSticker;
    private boolean CheckExists = false;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stickit);

        // Connect with firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Create notification channel
        createNotificationChannel();

        loginText = (EditText) findViewById(R.id.username);
        loginButton = findViewById(R.id.login);
        sendButton = findViewById(R.id.send_button);
        historyButton = findViewById(R.id.history_button);
        smiley = findViewById(R.id.smiley_sticker);
        smileySent = findViewById(R.id.smiley_numSent);
        usernameView = findViewById(R.id.username_view);
        friendsList = findViewById(R.id.friends_list);
        recipientText = findViewById(R.id.recipient);
        stickerSentTv = findViewById(R.id.stickerSentTv);


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
                sendSticker(usernameView.getText().toString(), recipientText.getText().toString(),
                        selectedSticker);
            }
        });

        // Show sticker histories
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_window, null);

                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                //dismiss
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
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
                        displaySentTimes(dataSnapshot);
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

    public void sendSticker(String curr_username, String friend_username, String sticker_name) {
        DatabaseReference curr_user = mDatabase.child("users").child(curr_username);
        DatabaseReference to_user = mDatabase.child("users").child(friend_username);
        DatabaseReference sticker_sent = mDatabase.child("users").child(friend_username).child(sticker_name);

        // increment
        DatabaseReference curr_user_sent_count= curr_user.child("stickersSent");
        DatabaseReference to_user_sticker_count = to_user.child("stickersRecieved");
        DatabaseReference sticker_sent_count = sticker_sent.child("NumbersSent");

        curr_user_sent_count.setValue(ServerValue.increment(1));
        to_user_sticker_count.setValue(ServerValue.increment(1));
        sticker_sent_count.setValue(ServerValue.increment(1));

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
        historyButton.setVisibility(View.VISIBLE);
        smileySent.setVisibility(View.VISIBLE);
    }

    // to check if the user already exist
    private boolean userExist(String nameToCheck) {
        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> userChildren = dataSnapshot.getChildren();

                for (DataSnapshot user : userChildren) {
                    User u = user.getValue(User.class);
                    if (u.username.equals(nameToCheck)) {
                        CheckExists = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return CheckExists;
    }

    public void displayFriends(DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        String current_friends = friendsList.getText().toString();
        String new_friends_list = current_friends + user.username + ", ";
        friendsList.setText("Friends: " + new_friends_list);
    }

    public void displaySentTimes(DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        String name = user.getName();
        String SmileySentTimes = dataSnapshot.child("users").child(name)
                .child("smiley_sticker").child("NumbersSent").getValue().toString();
        smileySent.setText("sent: " + SmileySentTimes);

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
}
