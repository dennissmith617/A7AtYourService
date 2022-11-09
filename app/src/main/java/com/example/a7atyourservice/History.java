package com.example.a7atyourservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a7atyourservice.model.Message;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class History extends AppCompatActivity {

    private FirebaseListAdapter<Message> adapter;
    private DatabaseReference mDatabase;
    private ListView history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Connect with firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        history = findViewById(R.id.history_v);

        FirebaseListOptions<Message> options = new FirebaseListOptions.Builder<Message>()
            .setQuery(mDatabase, Message.class)
            .setLayout(R.layout.chat_history)
            .build();

        adapter = new FirebaseListAdapter<Message>(options)  {

            @Override
            protected void populateView(@NonNull View v, @NonNull Message model, int position) {
                ImageView sticker = v.findViewById(R.id.StickerView);
                TextView messageUser = v.findViewById(R.id.name);
                TextView messageTime = v.findViewById(R.id.time);

                // set each view
                if (model.stickerName.equals("smiley_sticker")){
                    sticker.setImageResource(R.drawable.smiley);
                } else {
                    sticker.setImageResource(R.drawable.party);
                }
                messageUser.setText(model.fromUser);
                messageTime.setText(model.messageTime);
            }
        };

        history.setAdapter(adapter);
    }
    
  
}
