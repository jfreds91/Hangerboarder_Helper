package com.example.jesse.hangerboarder_helper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class HH_home extends AppCompatActivity {
    public final static String SER_KEY = "com.example.HangerBoarderHelper.ser";
    static final int NEW_WORKOUT_REQUEST = 1;  // The request code
    public final static String EXTRA_MESSAGE = "com.example.Hangerboarder_Helper.MESSAGE";
    Button btngenerate, btntest, btnaddworkout;
    TextView tvtitle;
    ArrayList<Workout_obj> allworkouts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hh_home);
        // find view elements, so we can assign on click listeners to them later
        btngenerate = (Button) findViewById(R.id.generateWorkoutButton);
        btntest = (Button) findViewById(R.id.testButton);
        btnaddworkout = (Button) findViewById(R.id.add_workout);
        tvtitle = (TextView) findViewById(R.id.mainTitle);



        //set click listener which sends you to CreateWorkoutActivity
        OnClickListener oclBtnAddWorkout = new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Workout_obj nworkout = new Workout_obj("new_workout");
                //allworkouts.add(nworkout);
                Intent intent = new Intent(v.getContext(), CreateWorkoutActivity_V2.class);
                //Bundle bundle = new Bundle();
                //bundle.putSerializable(SER_KEY, nworkout);
                //intent.putExtras(bundle);
                //intent.putExtra(SER_KEY, nworkout);
                //startActivityForResult?
                startActivityForResult(intent, NEW_WORKOUT_REQUEST);
            }
        };
        btnaddworkout.setOnClickListener(oclBtnAddWorkout);



        //set click listener which initializes nworkout and adds exercises
        OnClickListener oclBtnGenerate = new OnClickListener() {
            @Override
            public void onClick(View v) {

                tvtitle.setText("Workout Generated");
                Workout_obj nworkout = new Workout_obj("first_workout");
                Exercise_obj aexercise = new Exercise_obj("exercise_a");
                Exercise_obj bexercise = new Exercise_obj("exercise_b");
                Exercise_obj cexercise = new Exercise_obj("exercise_c");

                aexercise.add(1);
                aexercise.add(2);
                aexercise.add(3);

                allworkouts.add(nworkout);
                nworkout.add(aexercise);
                nworkout.add(bexercise);
                nworkout.add(cexercise);
            }
        };
        //assign click listener to button
        btngenerate.setOnClickListener(oclBtnGenerate);

    }

    //this is called if after the user returns from CreateWorkoutActivity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_WORKOUT_REQUEST) {
            if (resultCode == RESULT_OK) {
                tvtitle.setText("RESULT_OK");
                //add returned workout to workouts list allworkouts
                Workout_obj createdWorkout = (Workout_obj) data.getSerializableExtra(SER_KEY);
                allworkouts.add(createdWorkout);
            }
            if (resultCode == RESULT_CANCELED) {
                tvtitle.setText("RESULT_CANCELED");
            }
        }
    }

    /** Called when the user clicks the Test button */
    public void runTest(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, TestResultsActivity.class);

        //check to see if workout exists
        //String message = (allworkouts.getFirst() == null) ? "workout not defined" : allworkouts.getFirst().extoString();

        String message = "No workouts defined";
        try {
            message = allworkouts.get(allworkouts.size() - 1).extoString();
        } catch(NoSuchElementException nsee) {}
        catch(IndexOutOfBoundsException ioobe) {}

        intent.putExtra(EXTRA_MESSAGE, message);

        startActivity(intent);
    }



}


