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
import com.example.a7atyourservice.model.User;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;


public class StickItActivity extends AppCompatActivity {

    private static final String TAG = StickItActivity.class.getSimpleName();

    private DatabaseReference mDatabase;
    private ImageButton smiley;
    private ImageButton party;
    private Button loginButton;
    private Button sendButton;
    private EditText loginText;
    private TextView usernameView;
    private TextView friendsList;
    private EditText recipientText;

    private String selectedSticker;
    private String username;
    private String currentFriends;

    private TextView smileySent;
    private TextView partySent;
    private Button historyButton;


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
        smiley = findViewById(R.id.smiley_sticker);
        party = findViewById(R.id.party_sticker);
        usernameView = findViewById(R.id.username_view);
        friendsList = findViewById(R.id.friends_list);
        recipientText = findViewById(R.id.recipient);
        currentFriends = new String();

        historyButton = findViewById(R.id.history_button);
        smileySent = findViewById(R.id.smiley_numSent);
        partySent = findViewById(R.id.party_numSent);


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

        party.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectSticker(party);
            }
        });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedSticker != null) {
                    sendSticker(loginText.getText().toString(), recipientText.getText().toString(), selectedSticker);
                } else {
                    Toast.makeText(getApplicationContext(), "Please select a sticker", Toast.LENGTH_SHORT).show();
                }
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
                        sendNotification();
                        Log.e(TAG, "onChildAdded: dataSnapshot = " + dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        displaySentTimes(dataSnapshot);
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
        smiley.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        party.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        sticker.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
    }

    public void sendSticker(String curr_username, String friend_username, String sticker_name) {
        DatabaseReference curr_user = mDatabase.child("users").child(curr_username);
        DatabaseReference to_user = mDatabase.child("users").child(friend_username);
        DatabaseReference sticker_sent = mDatabase.child("users").child(curr_username).child(sticker_name);

        // increment
        DatabaseReference curr_user_sent_count= curr_user.child("stickersSent");
        DatabaseReference to_user_sticker_count = to_user.child("stickersRecieved");

        curr_user_sent_count.setValue(ServerValue.increment(1));
        to_user_sticker_count.setValue(ServerValue.increment(1));
        sticker_sent.setValue(ServerValue.increment(1));

    }

    // Add Users to DB
    public void addUser(String username) {
        String displayText;
        this.username = username;
        // check if the user already exists, if not, create a new user profile
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("users").child(username).exists()){
                    User user;
                    // Start off with no stickers
                    user = new User(username, 0, 0);
                    Task t1 = mDatabase.child("users").child(user.username).setValue(user);

                    if (!t1.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Unable to login!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        displayText = "username: " + loginText.getText().toString();

        // Hide Login Info
        loginButton.setVisibility(View.INVISIBLE);
        loginText.setVisibility(View.INVISIBLE);

        // Show sticker "view"
        usernameView.setText(displayText);
        friendsList.setVisibility(View.VISIBLE);
        smiley.setVisibility(View.VISIBLE);
        party.setVisibility(View.VISIBLE);
        recipientText.setVisibility(View.VISIBLE);
        sendButton.setVisibility(View.VISIBLE);
        historyButton.setVisibility(View.VISIBLE);

    }


    public void displayFriends(DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        if(user != null && user.username != null) {
            currentFriends = new StringBuilder(currentFriends).append(user.username).append(",").toString();
            friendsList.append(currentFriends);
        }
    }


    // show how many time each sticker has been used
    public void displaySentTimes(DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        String smileySentTimes = String.valueOf(user.smiley_sticker);
        String partySentTimes = String.valueOf(user.party_sticker);
        smileySent.setText("sent: " + smileySentTimes);
        partySent.setText("sent: " + partySentTimes);
        smileySent.setVisibility(View.VISIBLE);
        partySent.setVisibility(View.VISIBLE);
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
                .setSmallIcon(R.drawable.smiley)
                .setContentTitle("New sticker alert!")
                .setContentText("Congratulations on your sticker")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                // hide the notification after its selected
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, notifyBuild.build());

    }

    public void openAboutScreen(View view) {
        startActivity(new Intent(this, com.example.a7atyourservice.InfoScreen.class));
    }
}
