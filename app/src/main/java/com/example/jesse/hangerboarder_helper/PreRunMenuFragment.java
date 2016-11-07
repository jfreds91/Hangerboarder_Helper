package com.example.jesse.hangerboarder_helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Jesse on 11/5/2016.
 */

public class PreRunMenuFragment extends DialogFragment {
    public final static String SER_KEY = "com.example.HangerBoarderHelper.ser";

    //The activity that creates and instance of this dialog fragment must
    //implement this interface in order to receive event callbacks.
    //Each method passes the DialogFragment in case the host needs to query it.
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, int i);
        public void onDialogNegativeClick(DialogFragment dialog);
        public void onDialogNeutralClick(DialogFragment dialog, int i);
    }

    //Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    //Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a=(Activity) context;
        // Verify that the host activity implements the callback interface
        try {
            //Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) a;
        } catch (ClassCastException e) {
            //The activity doesn't implement the interface, throw exception
            throw new ClassCastException(a.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public static PreRunMenuFragment newInstance(String s, int i) {
        PreRunMenuFragment f = new PreRunMenuFragment();

        Bundle args = new Bundle();
        args.putString("name", s);
        args.putInt("workoutIndex", i);
        f.setArguments(args);

        return f;
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title;
        title = getArguments().getString("name");
        final int i = getArguments().getInt("workoutIndex");

        //Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(title) //R.string.dialogtitle
                .setPositiveButton("RUN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do RUN action
                        mListener.onDialogPositiveClick(PreRunMenuFragment.this, i);
                        }
        })
                .setNegativeButton("RETURN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do RETURN action
                        mListener.onDialogNegativeClick(PreRunMenuFragment.this);
                        }
                })
                .setNeutralButton("EDIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do EDIT action
                        mListener.onDialogNeutralClick(PreRunMenuFragment.this, i);
                    }
                });
        return builder.create();
        }

}
