package com.example.jesse.hangerboarder_helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.nio.charset.Charset;

/**
 * Created by Jesse on 10/29/2016.
 */

public class CreateWorkoutActivity_V2 extends Activity{

    private RelativeLayout mContainerView;
    private LinearLayout mScrollLinearView_Create;
    private Button mAddButton;
    private EditText editTextWorkoutName;

    //There should always be only one empty row, other rows will be removed
    private View mExclusiveEmptyView;

    public final static String SER_KEY = "com.example.HangerBoarderHelper.ser";
    public final static String WO_KEY = "com.example.HangerBoarderHelper.wokey";
    private Button btnCreateWorkout;
    private TextView catitle;

    int viewIndex = -1;
    Workout_obj newWorkout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.row_container);
        mContainerView = (RelativeLayout) findViewById(R.id.parentView);
        mScrollLinearView_Create = (LinearLayout) findViewById(R.id.scrollLinearView_create);
        mAddButton = (Button) findViewById(R.id.btnAddNewItem);
        catitle = (TextView) findViewById(R.id.createTitle);
        btnCreateWorkout = (Button) findViewById(R.id.createWorkoutButton);
        editTextWorkoutName = (EditText) findViewById(R.id.workoutName);

        if (savedInstanceState != null) {
            //don't do anything, this is handled by onRestoreInstanceState
        } else {
            //Try to get workout viewindex from intent. If it defaults, run normally. Otherwise, load workout
            Intent intent = this.getIntent();
            viewIndex = intent.getIntExtra(WO_KEY, viewIndex);
            if (viewIndex != -1) {
                newWorkout = (Workout_obj) intent.getSerializableExtra(SER_KEY);
                editTextWorkoutName.setText(newWorkout.getName());
                for (int i = 0; i < newWorkout.size(); i++) {
                    inflateEditRow(newWorkout.get(i));
                }
                //make delete button visible
                findViewById(R.id.deleteWorkoutButton).setVisibility(View.VISIBLE);
            } else {
                // Add some examples
                inflateEditRow(null);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        // encapsulate information in a parcelable object, and save it into the state bundle.
        encapsulateInput(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore value of members from saved state
        newWorkout = (Workout_obj) savedInstanceState.getSerializable(SER_KEY);
        viewIndex = savedInstanceState.getInt(WO_KEY, viewIndex);
        editTextWorkoutName.setText(newWorkout.getName());
        for (int i=0; i < newWorkout.size(); i++) {
            inflateEditRow(newWorkout.get(i));
        }
        if (viewIndex!=-1) {
            findViewById(R.id.deleteWorkoutButton).setVisibility(View.VISIBLE);
        }
    }

    //onClick handler for the "Add New" button
    public void onAddNewClicked(View v) {
        //inflate a new row and hide the button self
        inflateEditRow(null);
        v.setVisibility(View.GONE);
    }

    //onClick handler for the "Save and Return" button
    public void onSaveAndReturnClicked(View v) {
        saveAndReturn();
    }

    //onClick handler for the "Delete and Return" button
    public void onDeleteAndReturnClicked(View v) {
        deleteAndReturn();
    }


    // onClick handler for the "X" button of each row
    public void onDeleteClicked(View v) {
        //remove the row by calling the getParent on button
        mScrollLinearView_Create.removeView((View) v.getParent());
    }

    //Helper for inflating a row
    private void inflateEditRow(Exercise_obj exercise_obj) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.row, mContainerView, false);
        final ImageButton deleteButton = (ImageButton) rowView.findViewById(R.id.buttonDelete);
        EditText editText = (EditText) rowView.findViewById(R.id.editText);
        final Spinner spinner = (Spinner) rowView.findViewById(R.id.spinnerCategory);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        if (exercise_obj != null && !exercise_obj.getName().isEmpty()) {
            editText.setText(exercise_obj.getName());
            spinner.setSelection(exercise_obj.getSpinnerPosition());
        } else {
            mExclusiveEmptyView = rowView;
            deleteButton.setVisibility(View.INVISIBLE);
        }

        //A TextWatcher to control the visibility of the "Add new" button and
        //handle the exclusive empty view
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.toString().isEmpty()) {
                    mAddButton.setVisibility(View.GONE);
                    deleteButton.setVisibility(View.INVISIBLE);

                    if (mExclusiveEmptyView != null && mExclusiveEmptyView != rowView) {
                        mScrollLinearView_Create.removeView(mExclusiveEmptyView);
                    }
                    mExclusiveEmptyView = rowView;

                } else {

                    if (mExclusiveEmptyView == rowView) {
                        mExclusiveEmptyView = null;
                    }

                    mAddButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);
                }
            }
        });

        //Inflate at the end of scroll layout, but before the add new button
        mScrollLinearView_Create.addView(rowView, mScrollLinearView_Create.getChildCount() - 1);
    }

    public void saveAndReturn() {
        // verify that workout has at least one exercise in it????
        Bundle bundle = new Bundle();
        encapsulateInput(bundle);
        //if (newWorkout.size() == 0) {catitle.setText("need at least one exercise"); return; }

        //Attach workout object to intent
        Intent returnIntent = new Intent();
        //Bundle bundle = new Bundle();
        bundle.putSerializable(SER_KEY, newWorkout);
        returnIntent.putExtras(bundle);
        if (viewIndex == -1) { //new workout case
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        } else { //edit workout case
            returnIntent.putExtra(WO_KEY, viewIndex);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }
    }

    public Bundle encapsulateInput(Bundle bundle) {
        //TODO Add a way to get the viewindex back as well
        //assign all values to a new Workout_obj
        String newName = editTextWorkoutName.getHint().toString(); //default if name field is blank
        if (!editTextWorkoutName.getText().toString().equals("")) {
            newName = editTextWorkoutName.getText().toString();
        }
        if (viewIndex == -1) { //meaning this workout does not exist
            newWorkout = new Workout_obj(newName);
        } else {
            newWorkout.setName(newName);
            newWorkout.clear(); //clearing this because otherwise it adds duplicates of each exercise in the workout. an unintended sideffect is that it deletes weight history
        }
        //generate temporary exercise, use it to save user input into newWorkout via loop
        Exercise_obj tempEx;
        Integer n = new Integer(mScrollLinearView_Create.getChildCount());
        for (int i = 0; i < n; i++) {
            if(mScrollLinearView_Create.getChildAt(i) instanceof LinearLayout) {
                LinearLayout child = (LinearLayout) mScrollLinearView_Create.getChildAt(i);
                EditText e = (EditText) child.getChildAt(0);
                if (!e.getText().toString().equals("")) {
                    tempEx = new Exercise_obj(e.getText().toString());
                    Spinner spinner = (Spinner) child.getChildAt(1);
                    tempEx.setSpinnerPosition(spinner.getSelectedItemPosition());
                    tempEx.setSpinnerType(spinner.getSelectedItem().toString());
                    newWorkout.add(tempEx);
                }
            }
        }

        bundle.putSerializable(SER_KEY, newWorkout);
        bundle.putInt(WO_KEY, viewIndex);
        return bundle;
    };



    public void deleteAndReturn() {
        newWorkout.clearAll();

        //Attach workout object to intent
        Intent returnIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SER_KEY, newWorkout);
        returnIntent.putExtras(bundle);
        returnIntent.putExtra(WO_KEY, viewIndex);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

}
