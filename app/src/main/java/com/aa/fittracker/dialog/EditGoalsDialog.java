package com.aa.fittracker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aa.fittracker.R;
import com.aa.fittracker.logic.store;

public class EditGoalsDialog extends Dialog {

    TextView usernameTvEgd;

    public EditGoalsDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_goals_dialog);

        usernameTvEgd=(TextView) findViewById(R.id.usernameDialogLabel);

        usernameTvEgd.setText(store.getUSERNAME());

    }
}
