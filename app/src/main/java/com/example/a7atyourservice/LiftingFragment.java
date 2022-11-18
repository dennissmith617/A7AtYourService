package com.example.a7atyourservice;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LiftingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LiftingFragment extends Fragment implements View.OnClickListener {

    private volatile boolean running = false;
    private Handler mainHandler = new Handler();
    private int n = 0;
    String startText = "Workout Length: 0 minutes";

    Button startWorkoutButton;
    Button endWorkoutButton;
    TextView timeSinceStart;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LiftingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LiftingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LiftingFragment newInstance(String param1, String param2) {
        LiftingFragment fragment = new LiftingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lifting, container, false);

        startWorkoutButton = view.findViewById(R.id.startWorkout);
        startWorkoutButton.setOnClickListener(this);

        endWorkoutButton = view.findViewById(R.id.endWorkout);
        endWorkoutButton.setOnClickListener(this);

        timeSinceStart = view.findViewById(R.id.TimeSinceStart);

        return view;
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.startWorkout:
                running = true;
                startThread(view);
                endWorkoutButton.setVisibility(View.VISIBLE);
                break;
            case R.id.endWorkout:
                running = false; // Flip to False to halt worker thread

                // end of workout, reset buttons + timer (maybe save state here?)
                startWorkoutButton.setVisibility(View.VISIBLE);
                endWorkoutButton.setVisibility(View.INVISIBLE);
                n = 0;
                timeSinceStart.setText(startText);

                break;
        }
    }

    public void startThread(View view) {
        CounterRunnable runnable = new CounterRunnable();
        new Thread(runnable).start();
    }

    class CounterRunnable implements Runnable {

        @Override
        public void run() {
            while(running) {
                // Change current number text based on n
                mainHandler.post(new Runnable(){
                    @Override
                    public void run() {
                        String timerPhrase =
                                String.format("Workout Length: " + n + " minutes");
                        timeSinceStart.setText(timerPhrase);
                    }
                });
                try{
                    Thread.sleep(60000);
                }catch(Exception e){
                    e.printStackTrace();
                }
                n += 1;
            }
        }
    }


}