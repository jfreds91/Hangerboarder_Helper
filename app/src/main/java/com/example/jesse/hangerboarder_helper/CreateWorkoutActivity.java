package com.example.jesse.hangerboarder_helper;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateWorkoutActivity extends AppCompatActivity {

    public final static String SER_KEY = "com.example.HangerBoarderHelper.ser";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);

        //assign object names to each view using view id
        final TextView helpText = (TextView) findViewById(R.id.helpful_hint);
        final EditText editText0 = (EditText) findViewById(R.id.new_workout);
        final EditText editText1 = (EditText) findViewById(R.id.exercise_1);
        final EditText editText2 = (EditText) findViewById(R.id.exercise_2);
        final EditText editText3 = (EditText) findViewById(R.id.exercise_3);
        final EditText editText4 = (EditText) findViewById(R.id.exercise_4);
        Button btnCreateWorkout = (Button) findViewById(R.id.createWorkoutButton);



        //set click listener which sends you to creates a Workout_obj, creates exercises for it and adds them, and destroys the activity
        View.OnClickListener oclBtnCreateWorkout = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wo1 = editText0.getText().toString();
                String ex1 = editText1.getText().toString();
                String ex2 = editText2.getText().toString();
                String ex3 = editText3.getText().toString();
                String ex4 = editText4.getText().toString();

                if(!wo1.equals("") && !ex1.equals("") && !ex2.equals("") && !ex3.equals("") && !ex4.equals("")) {
                    Intent returnIntent = new Intent();//v.getContext(), HH_home.class);
                    //Initialize exercises and workout
                    Workout_obj wo_o1 = new Workout_obj(wo1);
                    Exercise_obj ex_o1 = new Exercise_obj(ex1);
                    Exercise_obj ex_o2 = new Exercise_obj(ex2);
                    Exercise_obj ex_o3 = new Exercise_obj(ex3);
                    Exercise_obj ex_o4 = new Exercise_obj(ex4);

                    //Add exercises to workout
                    wo_o1.add(ex_o1);
                    wo_o1.add(ex_o2);
                    wo_o1.add(ex_o3);
                    wo_o1.add(ex_o4);

                    //Attach workout object to intent
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(SER_KEY, wo_o1);
                    returnIntent.putExtras(bundle);
                    setResult(Activity.RESULT_OK,returnIntent);
                    //startActivity(returnIntent);
                    finish();

                } else {
                    helpText.setText("You had a blank field you dumb fuck");
                    //Intent returnIntent_c = new Intent();
                    //setResult(Activity.RESULT_CANCELED, returnIntent_c);
                    //finish();
                }

                //Workout_obj nworkout = new Workout_obj("new_workout");
                //allworkouts.add(nworkout);

                //Bundle bundle = new Bundle();
                //bundle.putSerializable(SER_KEY, nworkout);
                //intent.putExtras(bundle);
                //intent.putExtra(SER_KEY, nworkout);
                //startActivityForResult?

            }
        };
        btnCreateWorkout.setOnClickListener(oclBtnCreateWorkout);


        //Intent intent = this.getIntent();
        //Bundle bundle = intent.getExtras();
        //List<Workout_obj> nworkout = (List<Workout_obj>)bundle.getSerializable("nworkout");
        //Workout_obj nworkout = (Workout_obj) getIntent().getSerializableExtra(HH_home.SER_KEY);
        //intent.getExtras().get("SER_KEY");
        //String message = new String nworkout.getName();

        //String message = (nworkout.getFirst() == null) ? "workout not defined" : nworkout.getFirst().extoString();
    }
}
