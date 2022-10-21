package com.example.a7atyourservice;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.SafeBrowsingResponseCompat;
import androidx.webkit.WebViewClientCompat;
import androidx.webkit.WebViewCompat;
import androidx.webkit.WebViewFeature;

public class WebViewActivity extends AppCompatActivity {

    private WebView mWebView;
    private EditText mWebDestEditText;
    private boolean safeBrowsingIsInitialized;

    private static final String TAG = "WebViewActivity2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        mWebView = (WebView)findViewById(R.id.webview);
        mWebDestEditText = (EditText)findViewById(R.id.webview_edit_text);
        mWebView.getSettings().setJavaScriptEnabled(true);
        safeBrowsingIsInitialized = false;

        mWebView.setWebViewClient(new WebViewClientCompat(){
            @Override
            public void onSafeBrowsingHit(WebView view, WebResourceRequest request,
                                          int threatType, SafeBrowsingResponseCompat callback) {
                if (WebViewFeature.isFeatureSupported(WebViewFeature.SAFE_BROWSING_RESPONSE_BACK_TO_SAFETY)) {
                    callback.backToSafety(true);
                    Toast.makeText(view.getContext(), "Unsafe web page blocked.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        // for devices/browsers Safe browser is not supported
        if (WebViewFeature.isFeatureSupported(WebViewFeature.START_SAFE_BROWSING)) {
            WebViewCompat.startSafeBrowsing(this, new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean success) {
                    Log.d(TAG, "onReceiveValue() called.");

                    safeBrowsingIsInitialized = true;
                    if (!success) {
                        Log.e(TAG, "Unable to initialize Safe Browsing!");
                    }
                }
            });
        }

    }

    public void loadWebsite(View view){
        String url = mWebDestEditText.getText().toString().trim();
        try {
            url = NetworkUtil.validInput(url);
            mWebView.loadUrl(url);
        } catch (NetworkUtil.MyException e) {
            Toast.makeText(getApplication(),e.toString(),Toast.LENGTH_SHORT).show();
        }


    }

}
