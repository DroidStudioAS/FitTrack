package com.aa.fittracker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aa.fittracker.R;
import com.aa.fittracker.logic.store;

public class EditGoalsDialog extends Dialog {

    TextView usernameTvEgd;

    EditText newWeightGoalEt;

    ImageView closeDialogButton;

    public EditGoalsDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_goals_dialog);
        /*********UI initializations**********/
        usernameTvEgd=(TextView) findViewById(R.id.usernameDialogLabel);
        newWeightGoalEt=(EditText) findViewById(R.id.newWeightGoalEt);
        closeDialogButton=(ImageView)findViewById(R.id.closeButton);

        /*********End  Of UI initializations**********/
        //set the username for the user
        usernameTvEgd.setText(store.getUSERNAME());
        //set the ET to the old goal
        newWeightGoalEt.setText(store.getUserWeightKg());

        /*************OnClicklisteners*****************/
        closeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        /*************End Of OnClicklisteners*****************/

    }
}
