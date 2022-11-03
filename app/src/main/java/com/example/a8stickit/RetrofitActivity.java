package com.example.a8stickit;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a8stickit.model.IPlaceholder;
import com.example.a8stickit.model.PostModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitActivity extends AppCompatActivity {
    private static final String TAG = "RetrofitActivity";

    private Retrofit retrofit;
    private Button retrofitBtn;
    private IPlaceholder api;
    TextView webFeedback;
    ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        retrofitBtn = findViewById(R.id.button4);
        retrofitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEmailsWithQuery();
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.eva.pingutil.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        api = retrofit.create(IPlaceholder.class);


    }

    private void getEmailsWithQuery(){
        // Loading bar
        loadingBar = findViewById(R.id.progressBar1);
        loadingBar.setVisibility(View.VISIBLE);

        // Get input email
        EditText emailText = (EditText) findViewById(R.id.textEntry);

        // to execute the call
        //api:: https://api.eva.pingutil.com/email?email=INPUT
        Call<PostModel> call = api.getEmailsWithQuery(emailText.getText().toString());


        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if(!response.isSuccessful()){
                    String error = "Call failed!" + response.code();

                    Log.d(TAG, error);

                    // Stop loading
                    loadingBar.setVisibility(View.GONE);

                    // Set error text
                    webFeedback = findViewById(R.id.webFeedback);
                    webFeedback.setVisibility(View.VISIBLE);
                    webFeedback.setText(error);
                    return;
                }

                Log.d(TAG, "Call Successed!");
                PostModel postModel = response.body();
                StringBuffer  str = new StringBuffer();
                str.append("Status: ")
                        .append(postModel.getStatus())
                        .append("\n")
                        .append("Email Address: ")
                        .append(postModel.getData().getEmail_address())
                        .append("\n")
                        .append("Domain: ")
                        .append(postModel.getData().getDomain())
                        .append("\n")
                        .append("Valid Syntax: ")
                        .append(postModel.getData().getValid_syntax())
                        .append("\n")
                        .append("Disposable: ")
                        .append(postModel.getData().getDisposable())
                        .append("\n")
                        .append("Webmail: ")
                        .append(postModel.getData().getWebmail())
                        .append("\n")
                        .append("Deliverable: ")
                        .append(postModel.getData().getDeliverable())
                        .append("\n")
                        .append("Catch All: ")
                        .append(postModel.getData().getCatch_all())
                        .append("\n")
                        .append("Gibberish: ")
                        .append(postModel.getData().getGibberish())
                        .append("\n")
                        .append("Spam: ")
                        .append(postModel.getData().getSpam())
                        .append("\n");


                Log.d(TAG, str.toString());

                // Stop loading
                loadingBar.setVisibility(View.GONE);

                // Set result text
                webFeedback = findViewById(R.id.webFeedback);
                webFeedback.setVisibility(View.VISIBLE);
                webFeedback.setText(str);
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                String errorMessage = "Call failed! " + t.getMessage();

                // Debug logs
                Log.d(TAG, errorMessage);

                // Stop loading
                loadingBar.setVisibility(View.GONE);

                // Set result text
                webFeedback = findViewById(R.id.webFeedback);
                webFeedback.setVisibility(View.VISIBLE);
                webFeedback.setText(errorMessage);

            }
        });
    }
}