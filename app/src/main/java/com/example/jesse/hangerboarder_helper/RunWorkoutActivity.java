package com.example.jesse.hangerboarder_helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Jesse on 11/6/2016.
 */

public class RunWorkoutActivity extends Activity {
    public final static String SER_KEY = "com.example.HangerBoarderHelper.ser";
    public final static String EX_KEY = "com.example.HangerBoarderHelper.exkey";
    public final static String WO_KEY = "com.example.HangerBoarderHelper.wokey";

    TextView timerTextView;
    Button proceedButton;
    TextView runWorkoutTitle;
    TextView lastWeightTextView;
    EditText thisWeightEditText;
    long startTime = 0;
    long elapsedTime = 0;
    long maxTime = 90000; //Must be in milliseconds
    long timeOnPerTen = 10;
    long millis;
    int activeExNum;
    int activeWoNum;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            millis = maxTime-(System.currentTimeMillis() - startTime + elapsedTime);
            if (millis <= 0) {
                timerTextView.setText("SET COMPLETE");
            } else {
                int seconds = (int) (millis / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                int mseconds = ((int) millis % 1000)/10;

                timerTextView.setText(String.format("%02d:%02d:%02d", minutes, seconds, mseconds));
                if (timeOnPerTen != 10) {
                    if (seconds % 10 > timeOnPerTen || seconds % 10 == 0) {
                        timerTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorOff, null));
                    } else {
                        timerTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorOn, null));
                    }
                }
                timerHandler.postDelayed(this, 5);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.run_workout_activity);

        thisWeightEditText = (EditText) findViewById(R.id.thisWeightEditText);
        thisWeightEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);

        //get serialized content
        Intent intent = this.getIntent();
        final Workout_obj activeWorkout = (Workout_obj) intent.getSerializableExtra(SER_KEY);
        activeWoNum = intent.getIntExtra(WO_KEY, 0);
        activeExNum = intent.getIntExtra(EX_KEY, 0);
        final int totalEx = activeWorkout.size();
        String totalExS = Integer.toString(totalEx);
        String thisExS = Integer.toString(activeExNum + 1);
        runWorkoutTitle = (TextView) findViewById(R.id.runWorkoutTitle);
        runWorkoutTitle.setText("Exercise " + activeWorkout.get(activeExNum).getName() + ", (" + activeWorkout.get(activeExNum).getSpinnerType() + "), " + thisExS + "/" +totalExS);
        lastWeightTextView = (TextView) findViewById(R.id.lastWeightTextView);
        lastWeightTextView.setText(Double.toString(activeWorkout.get(activeExNum).getLast()));

        //Initialize timer settings
        switch (activeWorkout.get(activeExNum).getSpinnerPosition()) {
            case 0: // 7/3 Repeater
                timeOnPerTen = 7;
                break;
            case 1: // 5/5 Repeater
                timeOnPerTen = 5;
                break;
            case 2: // Max Hang
                break;
            case 3: // Other
                break;
        }

        timerTextView = (TextView) findViewById(R.id.runWorkoutTimerText);
        int minutes = (int) (maxTime/1000) /60;
        int seconds = (int) (maxTime/1000) % 60;
        int mseconds = ((int) millis % 1000)/10;
        timerTextView.setText(String.format("%02d:%02d:%02d", minutes, seconds, mseconds));
        timerTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorOn, null));
        Button b = (Button) findViewById(R.id.runWorkoutStartPauseButton);
        b.setText("Start");

        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals("Pause")) {
                    timerHandler.removeCallbacks(timerRunnable);
                    elapsedTime = elapsedTime + (System.currentTimeMillis() - startTime);
                    b.setText("Start");
                } else {
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable,0);
                    b.setText("Pause");
                }
            }
        });

        proceedButton = (Button) findViewById(R.id.runWorkoutProceedButton);
        proceedButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //The proceed button returns to the HH_home activity with the specified workout and active (now completed) ex index
                //Get completed weight

                String s = thisWeightEditText.getText().toString();
                Double d;
                if (s == null || s.isEmpty()) {
                    d = new Double(0);
                } else {
                    d = Double.parseDouble(thisWeightEditText.getText().toString());
                }
                activeWorkout.get(activeExNum).add(d);
                Intent proceedIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(SER_KEY, activeWorkout);
                proceedIntent.putExtras(bundle);
                proceedIntent.putExtra(EX_KEY, activeExNum); //this is the index number of the exercise which was just run
                proceedIntent.putExtra(WO_KEY, activeWoNum);
                setResult(Activity.RESULT_OK,proceedIntent);
                timerHandler.removeCallbacks(timerRunnable);
                finish();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        Button b = (Button) findViewById(R.id.runWorkoutStartPauseButton);
        b.setText("Start");
    }

}
