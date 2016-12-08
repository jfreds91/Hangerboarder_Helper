package com.example.jesse.hangerboarder_helper;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import static java.lang.StrictMath.toIntExact;

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
    TextView RWA_exName;
    TextView RWA_woName;
    TextView RWA_iteration;
    TextView RWA_exType;
    long startTime = 0;
    long elapsedTime = 0;
    long maxTime = 90000; //Must be in milliseconds
    long timeOnPerInterval = 10;
    long interval = 15;
    long millis;
    long progressAdjustedTimeOn;
    long progressAdjustedTimeOff;
    int activeExNum;
    int activeWoNum;
    boolean hanging = false;
    enum Timerstate {Running, Stopped, Paused};
    final Timerstate[] stateArray = {Timerstate.Stopped};// = {{Timerstate.Stopped}};
    ProgressBar mProgress;

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
                progressAdjustedTimeOn = ((((System.currentTimeMillis() - startTime + elapsedTime) %(interval*1000)) -((interval-timeOnPerInterval)*1000)) / timeOnPerInterval/10    );
                progressAdjustedTimeOff = (((System.currentTimeMillis() - startTime + elapsedTime) %(interval*1000))/(interval-timeOnPerInterval)/10);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                int mseconds = ((int) millis % 1000)/10;

                timerTextView.setText(String.format("%02d:%02d:%02d", minutes, seconds, mseconds));
                if (timeOnPerInterval != interval) {
                    if ((seconds+1) % interval > timeOnPerInterval || (seconds+1) % interval == 0) {
                        //Climber is OFF the hangboard
                        if (hanging == true){
                            ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 200);
                            toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP2,1000);
                            hanging = false;
                        }
                        timerTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorOff, null));
                    } else {
                        //Climber is ON the hangboard
                        if (hanging == false){
                            ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 200);
                            toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP,1000);
                            hanging = true;
                        }
                        timerTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorOn, null));
                    }
                    if(hanging == true) {
                        mProgress.setProgress((int) progressAdjustedTimeOn);
                        //runWorkoutTitle.setText(Integer.toString((int) ((((System.currentTimeMillis() - startTime + elapsedTime) %(interval*1000)) -((interval-timeOnPerInterval)*1000)) / timeOnPerInterval/10    )));
                    } else {
                        mProgress.setProgress((int) progressAdjustedTimeOff);
                        //runWorkoutTitle.setText(Integer.toString((int) progressAdjustedTimeOff));
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
        //runWorkoutTitle = (TextView) findViewById(R.id.runWorkoutTitle);
        //runWorkoutTitle.setText("Exercise " + activeWorkout.get(activeExNum).getName() + ", (" + activeWorkout.get(activeExNum).getSpinnerType() + "), " + thisExS + "/" +totalExS);
        RWA_exName = (TextView) findViewById(R.id.RWA_ExerciseName);
        RWA_woName = (TextView) findViewById(R.id.RWA_WorkoutName);
        RWA_iteration = (TextView) findViewById(R.id.RWA_iteration);
        RWA_exType = (TextView) findViewById(R.id.RWA_ExerciseType);

        RWA_exName.setText(activeWorkout.get(activeExNum).getName());
        RWA_woName.setText(activeWorkout.getName());
        RWA_iteration.setText("(" + thisExS + "/" + totalExS + ")");
        RWA_exType.setText(activeWorkout.get(activeExNum).getSpinnerType());

        lastWeightTextView = (TextView) findViewById(R.id.lastWeightTextView);
        lastWeightTextView.setText(Double.toString(activeWorkout.get(activeExNum).getLast()));

        //Initialize timer settings
        switch (activeWorkout.get(activeExNum).getSpinnerPosition()) {
            case 0: // 10/5 Repeater
                timeOnPerInterval = 10;
                interval = 15;
                maxTime = 90000;
                break;
            case 1: // 7/3 Repeater
                timeOnPerInterval = 7;
                interval = 10;
                maxTime = 90000;
                break;
            case 2: // 5/5 Repeater
                timeOnPerInterval = 5;
                interval = 10;
                maxTime = 90000;
                break;
            case 3: // Max Hang
                break;
        }

        timerTextView = (TextView) findViewById(R.id.runWorkoutTimerText);
        int minutes = (int) (maxTime/1000) /60;
        int seconds = (int) (maxTime/1000) % 60;
        int mseconds = ((int) millis % 1000)/10;
        timerTextView.setText(String.format("%02d:%02d:%02d", minutes, seconds, mseconds));
        timerTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorOn, null));

        mProgress = (ProgressBar) findViewById(R.id.progressbar);

        final Button b = (Button) findViewById(R.id.runWorkoutStartPauseButton);
        b.setText("Start");
        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Button b = (Button) v;

                switch (stateArray[0]) {
                    case Stopped: //pressed button when timer was stopped. should run timer
                        stateArray[0] = Timerstate.Running;
                        b.setText("Pause");
                        startTime = System.currentTimeMillis();
                        timerHandler.postDelayed(timerRunnable,0);
                        mProgress.setVisibility(View.VISIBLE);
                        break;
                    case Paused: //pressed button when timer was paused. should run timer
                        stateArray[0] = Timerstate.Running;
                        b.setText("Pause");
                        startTime = System.currentTimeMillis();
                        timerHandler.postDelayed(timerRunnable,0);
                        mProgress.setVisibility(View.VISIBLE);
                        break;
                    case Running: //pressed button when timer was running. should pause timer
                        stateArray[0] = Timerstate.Paused;
                        timerHandler.removeCallbacks(timerRunnable);
                        elapsedTime = elapsedTime + (System.currentTimeMillis() - startTime);
                        b.setText("Start");
                        break;
                }
            }
        });

        Button br = (Button) findViewById(R.id.runWorkoutResetButton);
        br.setText("Reset");
        br.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Button br = (Button) v;
                stateArray[0] = Timerstate.Stopped;
                timerHandler.removeCallbacks(timerRunnable);
                elapsedTime = 0;
                millis = 0;
                int minutes = (int) (maxTime/1000) /60;
                int seconds = (int) (maxTime/1000) % 60;
                int mseconds = ((int) millis % 1000)/10;
                timerTextView.setText(String.format("%02d:%02d:%02d", minutes, seconds, mseconds));
                timerTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorOn, null));
                hanging = false;
                mProgress.setVisibility(View.GONE);
                b.setText("Start");
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
    public void onStop() {
        super.onStop();
        stateArray[0] = Timerstate.Stopped;
        timerHandler.removeCallbacks(timerRunnable);
        Button b = (Button) findViewById(R.id.runWorkoutStartPauseButton);
        b.setText("Stopped");
    }

}
