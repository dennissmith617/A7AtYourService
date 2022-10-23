package com.example.a7atyourservice;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a7atyourservice.model.IPlaceholder;
import com.example.a7atyourservice.model.PostModel;
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
                getActivity();
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.boredapi.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        api = retrofit.create(IPlaceholder.class);


    }

    private void getActivity(){
        // Loading bar
        loadingBar = findViewById(R.id.progressBar1);
        loadingBar.setVisibility(View.VISIBLE);

        // to execute the call
        Call<PostModel> call = api.getActivityModels();

        //call.execute() runs on the current thread, which is main at the momement. This will crash
        // use Retrofit's method enque. This will automaically push the network call to background thread
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                //This gets called when atleast the call reaches a server and there was a response BUT 404 or any legitimate error code from the server, also calls this
                // check response code is between 200-300 and API was found

                if(!response.isSuccessful()){
                    Log.d(TAG, "Call failed!" + response.code());
                    String error = "Call failed!" + response.code();
                    // Stop loading
                    loadingBar.setVisibility(View.GONE);

                    // Set error text
                    webFeedback = findViewById(R.id.webFeedback);
                    webFeedback.setVisibility(View.VISIBLE);
                    webFeedback.setText(error);
                    return;
                }

                Log.d(TAG, "Call Success!");
                PostModel postModels = response.body();
                StringBuffer  str = new StringBuffer();
                str.append("HTTP Response Code:")
                        .append(response.code())
                        .append("\n")
                        .append("Activity: ")
                        .append(postModels.getActivity())
                        .append("\n")
                        .append("Type: ")
                        .append(postModels.getType())
                        .append("\n")
                        .append("Participants: ")
                        .append(postModels.getParticipants())
                        .append("\n");

                    // Logs for debugging
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
                Log.d(TAG, "Call failed!" + t.getMessage());
                String errorMessage = "Call failed!" + t.getMessage();

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