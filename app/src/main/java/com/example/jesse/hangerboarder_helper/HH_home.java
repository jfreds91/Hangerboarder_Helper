package com.example.jesse.hangerboarder_helper;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
//import android.support.design.widget.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class HH_home extends AppCompatActivity
                     implements PreRunMenuFragment.NoticeDialogListener{

    public final static String SER_KEY = "com.example.HangerBoarderHelper.ser";
    public final static String EX_KEY = "com.example.HangerBoarderHelper.exkey";
    public final static String WO_KEY = "com.example.HangerBoarderHelper.wokey";
    static final int NEW_WORKOUT_REQUEST = 1;  // The request code
    static final int RUN_WORKOUT = 2; //Request code
    static final int EDIT_WORKOUT = 3; //Request code
    public final static String EXTRA_MESSAGE = "com.example.Hangerboarder_Helper.MESSAGE";
    static final String datafile = ".com.example.HangerBoarderHelper.serdata";

    Button btngenerate, btntest, btnaddworkout;
    public LinearLayout mScrollLinearView;
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
        mScrollLinearView = (LinearLayout) findViewById(R.id.scrollLinearView);


        //Load Datafile
        loadData();
        Toast.makeText(getApplicationContext(), "Load Successful",
                Toast.LENGTH_SHORT).show();

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

                aexercise.add(new Double(1));
                aexercise.add(new Double(2));
                aexercise.add(new Double(3));

                allworkouts.add(nworkout);
                nworkout.add(aexercise);
                nworkout.add(bexercise);
                nworkout.add(cexercise);
            }
        };
        //assign click listener to button
        btngenerate.setOnClickListener(oclBtnGenerate);
    }


    //The problem with this method of loading is that it reloads the save file right after a workout is deleted, nullifying deletion
/*    @Override
    public void onResume(){
        super.onResume();
        loadData();
    }*/

    //this is called if after the user returns from an activity for a result
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //WORKOUT CREATION
        if (requestCode == NEW_WORKOUT_REQUEST) {
            if (resultCode == RESULT_OK) {
                tvtitle.setText("RESULT_OK");
                //add returned workout to workouts list allworkouts
                Workout_obj createdWorkout = (Workout_obj) data.getSerializableExtra(SER_KEY);
                allworkouts.add(createdWorkout);
                showWorkout(createdWorkout);
                Toast.makeText(getApplicationContext(), "Workout Created",
                        Toast.LENGTH_SHORT).show();
            }
            if (resultCode == RESULT_CANCELED) {
                tvtitle.setText("RESULT_CANCELED");
            }
        }

        //WORKOUT EDITED
        if (requestCode == EDIT_WORKOUT) {
            if (resultCode == RESULT_OK) {
                Workout_obj editedWorkout = (Workout_obj) data.getSerializableExtra(SER_KEY);
                int viewIndex = data.getIntExtra(WO_KEY, allworkouts.size()); //the default here is allworkouts.size() just in case there's a problem, so the workout is added to the end
                Button btn = (Button) mScrollLinearView.getChildAt(viewIndex);
                if (editedWorkout.isEmpty()) {
                    //delete workout
                    allworkouts.remove(viewIndex);
                    mScrollLinearView.removeView(btn);
                    tvtitle.setText("Workout successfully deleted");
                    Toast.makeText(getApplicationContext(), "Workout Deleted",
                            Toast.LENGTH_SHORT).show();
                } else {
                    //update edited workout
                    allworkouts.set(viewIndex, editedWorkout);
                    //should not need to call showWorkout... but do need to rename button
                    btn.setText(editedWorkout.getName());
                    tvtitle.setText("Workout successfully edited");
                    Toast.makeText(getApplicationContext(), "Workout Edited",
                            Toast.LENGTH_SHORT).show();
                }
            }
            if (resultCode == RESULT_CANCELED) {
                tvtitle.setText("edit workout RESULT_CANCELED");
            }
        }

        //RUNNING WORKOUT
        if (requestCode == RUN_WORKOUT) {
            if (resultCode == RESULT_OK) {
                tvtitle.setText("Run RESULT_OK");
                //Check if there is another exercise
                Workout_obj runningWorkout = (Workout_obj) data.getSerializableExtra(SER_KEY);
                int runningWoNum = data.getIntExtra(WO_KEY, -1);
                int runningExNum = data.getIntExtra(EX_KEY, -1);
                    if (runningExNum == -1) {
                        tvtitle.setText("RunWorkoutActivity did not return EX_KEY");
                    } else if (runningWoNum == -1) {
                        tvtitle.setText("RunWorkoutActivity did not return WO_KEY");
                    } else {
                        runningExNum++;
                        try {
                            Exercise_obj testEx =  runningWorkout.get(runningExNum); //This is just to make sure that there is another exercise left

                            Intent intent = new Intent(this, RunWorkoutActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(SER_KEY, runningWorkout);
                            intent.putExtras(bundle);
                            intent.putExtra(EX_KEY, runningExNum);
                            startActivityForResult(intent, RUN_WORKOUT); //RUN_WORKOUT is the for-result key
                        } catch(IndexOutOfBoundsException ioobe) {
                            tvtitle.setText("Workout Complete!!!");
                            Toast.makeText(getApplicationContext(), "Workout Complete",
                                    Toast.LENGTH_SHORT).show();
                            allworkouts.set(runningWoNum, runningWorkout);
                        }
                    }
            }
        }
    }

    /** Called when the user clicks the Test button */
    public void runTest(View view) {
        /*Intent intent = new Intent(this, TestResultsActivity.class);
        String message = "No workouts defined";
        try {
            message = allworkouts.get(allworkouts.size() - 1).extoString();
        } catch(NoSuchElementException nsee) {}
        catch(IndexOutOfBoundsException ioobe) {}

        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);*/

        //Test save data
        saveData();

    }

    public void runTest2(View view) {
        //load data
        loadData();
    }

    public void showWorkout(Workout_obj showWorkout){
        //this handles the case where we just pass 1 workout to the main view it should:
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View buttHolderView = inflater.inflate(R.layout.workoutbutt, mScrollLinearView, false);
        final Button btn = (Button) buttHolderView.findViewById(R.id.workoutButtButt);
        ((ViewGroup) btn.getParent()).removeView(btn);
        btn.setText(showWorkout.getName());
        mScrollLinearView.addView(btn);//, mScrollLinearView.getChildCount()-1);
    };

    public void onWorkoutButtClick(View v) {
        //This is called when a workout button is clicked
        //needs to find a way to pass its index to the dialog
        int viewIndex = mScrollLinearView.indexOfChild(v);
        String s = allworkouts.get(viewIndex).getName();
        DialogFragment newFragment = PreRunMenuFragment.newInstance(s, viewIndex);
        newFragment.show(getFragmentManager(), "prerun");
    }


    //The dialog fragment receives a reference to this Activity through the
    //Fragment.onAttach() callback, which it used to call the following methods
    //defined by the PreRunMenuFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, int viewIndex) {
        //User touched RUN
        String s = Integer.toString(viewIndex);
        tvtitle.setText("DialogPositiveClick " + s);
        Workout_obj activeWorkout = allworkouts.get(viewIndex);

        Intent intent = new Intent(this, RunWorkoutActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(SER_KEY, activeWorkout);
        intent.putExtras(bundle);
        intent.putExtra(WO_KEY,viewIndex);
        //intent.putExtra(EX_KEY, 0); //This should be used to tell the activity to run the first exercise MAY NOT BE NEEDED
        startActivityForResult(intent, RUN_WORKOUT); //RUN_WORKOUT is the for-result key
    }
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        //User touched RETURN
        tvtitle.setText("DialogNegativeClick");
    }
    @Override
    public void onDialogNeutralClick(DialogFragment dialog, int viewIndex) {
        //User touched RUN
        tvtitle.setText("DialogNeutralClick");
        Workout_obj activeWorkout = allworkouts.get(viewIndex);
        Intent intent = new Intent(this, CreateWorkoutActivity_V2.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(SER_KEY, activeWorkout);
        intent.putExtras(bundle);
        intent.putExtra(WO_KEY, viewIndex);

        startActivityForResult(intent, EDIT_WORKOUT);
    }

    public void saveData(){
        FileOutputStream fos = null;
        File file;
        try {
            file = new File(this.getFilesDir() + datafile);
            try {fos = this.openFileOutput(datafile, Context.MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                file.createNewFile();
            }
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(allworkouts);
            os.close();
            fos.close();
            tvtitle.setText("save successful...?");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData(){
        FileInputStream fis = null;
        File file;
        try {
            file = new File(this.getFilesDir() + datafile);
            try {fis = this.openFileInput(datafile);
            } catch (FileNotFoundException e) {
                file.createNewFile();
            }
            ObjectInputStream is = new ObjectInputStream(fis);
            try {
                allworkouts = (ArrayList<Workout_obj>) is.readObject();
            } catch (ClassNotFoundException e) {}
            is.close();
            fis.close();
            tvtitle.setText("load successful...?");
            //remove buttons and re-add them from scratch
            mScrollLinearView.removeAllViews();
            for (Workout_obj wo: allworkouts) {
                showWorkout(wo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveData();
    }
}


