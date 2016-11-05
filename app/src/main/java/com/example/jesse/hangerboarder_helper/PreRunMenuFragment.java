package com.example.jesse.hangerboarder_helper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Jesse on 11/5/2016.
 */

public class PreRunMenuFragment extends DialogFragment {
    public final static String SER_KEY = "com.example.HangerBoarderHelper.ser";

/**
    //This is my attempt to receive and attach a workout object
    public static PreRunMenuFragment newInstance(Workout_obj wo) {
        PreRunMenuFragment f = new PreRunMenuFragment();
        Bundle args = new Bundle();
        args.putSerializable(SER_KEY, wo);
        f.setArguments(args);
        return f;
    }

 */

    public static PreRunMenuFragment newInstance(String s) {
        PreRunMenuFragment f = new PreRunMenuFragment();

        Bundle args = new Bundle();
        args.putString("name", s);
        f.setArguments(args);

        return f;
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title;
        title = getArguments().getString("name");

        //Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(title) //R.string.dialogtitle
                .setPositiveButton("RUN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do RUN action
                        }
        })
                .setNegativeButton("RETURN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do RETURN action
                        }
                })
                .setNeutralButton("EDIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do EDIT action
                    }
                });
        return builder.create();
        }

}
