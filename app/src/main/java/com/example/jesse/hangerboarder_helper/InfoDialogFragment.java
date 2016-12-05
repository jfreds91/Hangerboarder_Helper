package com.example.jesse.hangerboarder_helper;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;



/**
 * Created by Jesse on 11/27/2016.
 */

public class InfoDialogFragment extends DialogFragment {
    //This dialog does not have callbacks, and therefore does not have to implement a listener or onAttach method

    public InfoDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static InfoDialogFragment newInstance() {
        InfoDialogFragment f = new InfoDialogFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.info_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView infoTextview = (TextView) view.findViewById(R.id.infoTextView);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            infoTextview.setText(Html.fromHtml(this.getResources().getString(R.string.infotext),Html.FROM_HTML_MODE_LEGACY));
        } else {
            infoTextview.setText(Html.fromHtml(this.getResources().getString(R.string.infotext)));
        }

        Button closeInfoButton =(Button) view.findViewById(R.id.info_ok);
        View.OnClickListener oclCloseInfoButton = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeInfo(v);
            }
        };
        closeInfoButton.setOnClickListener(oclCloseInfoButton);
/*        // Get field from view
        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    */
    }

    public void closeInfo(View view) {
        this.dismiss();
    }

}
