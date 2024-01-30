package com.aa.fittracker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.aa.fittracker.R;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.network.networkHelper;

import okhttp3.OkHttpClient;

public class EditGoalsDialog extends Dialog {

    TextView usernameTvEgd;

    EditText newWeightGoalEt;

    ImageView closeDialogButton;

    Button confirmPatch;
    OkHttpClient client;

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
        confirmPatch=(Button)findViewById(R.id.confirmNewWeightGoalTrigger);

        client= new OkHttpClient();

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
        confirmPatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newWeightGoalEt.getText().toString().equals("")){
                    Toast.makeText(getContext(),"You Must Enter A Value!", Toast.LENGTH_SHORT).show();

                }
                networkHelper.patchWeightGoal(client,newWeightGoalEt.getText().toString());
                while(store.getServerResponseWeightGoalPatched().equals("")){
                    Log.i("waiting for response", "...");
                }
                if(store.getServerResponseWeightGoalPatched().contains("ok") && store.getServerResponseWeightGoalPatched().contains("!")){
                    //not ok
                }else if(store.getServerResponseWeightGoalPatched().contains("ok")){
                    //ok
                    store.setUserWeightKg(newWeightGoalEt.getText().toString());
                    Toast.makeText(getContext(),"Weight Goal Edited!", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });

        /*************End Of OnClicklisteners*****************/

    }
}
