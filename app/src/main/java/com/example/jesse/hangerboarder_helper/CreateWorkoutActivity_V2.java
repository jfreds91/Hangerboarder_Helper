package com.example.jesse.hangerboarder_helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Jesse on 10/29/2016.
 */

public class CreateWorkoutActivity_V2 extends Activity{
    // Parent view for all rows and the add button
    private LinearLayout mContainerView;
    // The "Add new" button
    private Button mAddButton;

    //There should always be only one empty row, other rows will be removed
    private View mExclusiveEmptyView;

    public final static String SER_KEY = "com.example.HangerBoarderHelper.ser";
    private Button btnCreateWorkout;
    private TextView catitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.row_container);

        mContainerView = (LinearLayout) findViewById(R.id.parentView);
        mAddButton = (Button) findViewById(R.id.btnAddNewItem);
        catitle = (TextView) findViewById(R.id.createTitle);
        btnCreateWorkout = (Button) findViewById(R.id.createWorkoutButton);

        // Add some examples
        inflateEditRow("Workout name");
        inflateEditRow("EX1");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        // TODO: Handle screen rotation:
        // encapsulate information in a parcelable object, and save it
        // into the state bundle.
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // TODO: Handle screen rotation:
        // restore the saved items and inflate each one with inflateEditRow;
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


    // onClick handler for the "X" button of each row
    public void onDeleteClicked(View v) {
        //remove the row by calling the getParent on button
        mContainerView.removeView((View) v.getParent());
    }

    //Helper for inflating a row
    private void inflateEditRow(String name) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.row, mContainerView, false); //instead of "null", could be "parent, false"
        final ImageButton deleteButton = (ImageButton) rowView.findViewById(R.id.buttonDelete);
        final EditText editText = (EditText) rowView.findViewById(R.id.editText);

        if (name != null && !name.isEmpty()) {
            editText.setText(name);
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
                        mContainerView.removeView(mExclusiveEmptyView);
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

        //Inflate at the end of all rows but before the "Add new" button and Save and Return button
        mContainerView.addView(rowView, mContainerView.getChildCount() - 2);
    }

    public void saveAndReturn() {
        //test title actions
        Integer n = new Integer(mContainerView.getChildCount());
        //catitle.setText(n.toString());
        //Do:
        // check 2+ LinearLayouts
        // check No LinearLayout editText child is empty
        // Get LinearLayout editText children strings
        int exnum = 0;
        String newName = "";
        for (int i = 0; i < n; i++) {
            if(mContainerView.getChildAt(i) instanceof LinearLayout) {
                LinearLayout child = (LinearLayout) mContainerView.getChildAt(i);
                EditText e = (EditText) child.getChildAt(0);
                if (!e.getText().toString().equals("")) {
                    exnum++;
                }
                if (exnum == 1){
                    newName = e.getText().toString();
                }
            }
        }
        if(exnum < 2) {
            catitle.setText("need at least two named fields");
            return;
        }

        //error checking done - create workout to return
        Workout_obj newWorkout = new Workout_obj(newName);
        Exercise_obj tempEx;
        String exName;
        exnum = 0;
        for (int i = 0; i < n; i++) {
            if(mContainerView.getChildAt(i) instanceof LinearLayout) {
                LinearLayout child = (LinearLayout) mContainerView.getChildAt(i);
                EditText e = (EditText) child.getChildAt(0);
                if (!e.getText().toString().equals("")) {
                    exnum++;
                    if (exnum >= 1) {
                        tempEx = new Exercise_obj(e.getText().toString());
                        newWorkout.add(tempEx);
                    }
                }
            }
        }
        //Attach workout object to intent
        Intent returnIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SER_KEY, newWorkout);
        returnIntent.putExtras(bundle);
        setResult(Activity.RESULT_OK,returnIntent);
        //startActivity(returnIntent);
        finish();
    }

}
