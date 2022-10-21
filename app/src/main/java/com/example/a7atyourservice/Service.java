package com.example.a7atyourservice;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Service extends AppCompatActivity implements View.OnClickListener {
    EditText inputText;
    Button submitbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_screen);

        inputText = (EditText) findViewById(R.id.InfoInput);
        submitbtn = findViewById(R.id.submitbtn);
        submitbtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.submitbtn){
            //Figure out how to pass this user input into the new thread for webview
            Editable userInput = inputText.getText();
            startThread(view);
        }
    }

    public void startThread(View view) {
        WebRunnable runnable = new WebRunnable();
        new Thread(runnable).start();
    }

    class WebRunnable implements Runnable {

        @Override
        public void run() {
            WebView appWebView = findViewById(R.id.webView);
            appWebView.setVisibility(View.VISIBLE);

            TextView zipPrompt = findViewById(R.id.ZipText);
            zipPrompt.setVisibility(View.INVISIBLE);

        }
    }


}
