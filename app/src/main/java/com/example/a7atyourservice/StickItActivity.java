package com.example.a7atyourservice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StickItActivity extends Activity {

    private static final String TAG = StickItActivity.class.getSimpleName();

    private DatabaseReference mDatabase;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
        setContentView(R.layout.activity_stickit);

        // Connect with firebase
        //
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    // Add data to firebase button
    public void doAddDataToDb(View view) {
        // Write a message to the database
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("message");

        Task t = myRef.setValue("Hello, World!");
        Log.i(TAG, "doAddDataToDb: " + t.getResult());
        if(!t.isSuccessful()){
            Toast.makeText(getApplicationContext()
                    , "Failed to write value into firebase. " , Toast.LENGTH_SHORT).show();
            return;
        }

        // Read from the database by listening for a change to that item.
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                TextView tv = (TextView) findViewById(R.id.username);
                tv.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
                Toast.makeText(getApplicationContext()
                        , "Failed to write value into firebase. " , Toast.LENGTH_SHORT).show();
            }

        });

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

    //TODO: notification channel registration
    //largely copied from android developer site
    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
