package com.aa.fittracker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aa.fittracker.R;
import com.aa.fittracker.trainingservice.TrainingActivity;

public class promptDialog extends Dialog {
    TrainingActivity ta;
    TextView userPromptTv;
    Button yesBut;
    Button noBut;
    public promptDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prompt_dialog);


        userPromptTv=(TextView) findViewById(R.id.userPromptTv);
        yesBut=(Button) findViewById(R.id.yesBut);
        noBut=(Button) findViewById(R.id.noBut);

        if(noBut.getVisibility()== View.GONE){
            noBut.setVisibility(View.VISIBLE);
        }


    }

    //0- called from the TrainingActivity
    //1- called from elsewhere
    public void noTrainingsPrompt(int status){
        userPromptTv.setText(R.string.no_training_prompt);
        yesBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status==0) {
                    TrainingActivity.setCurrentPosition(0);
                }else{
                    Intent intent = new Intent(getContext(),TrainingActivity.class);
                    intent.putExtra("status","1");
                    getContext().startActivity(intent);
                }
                dismiss();
            }
        });
        noBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    public void trainingAddedPrompt(){
        userPromptTv.setText("Success. Check Trainings?");
        yesBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrainingActivity.setCurrentPosition(1);
                dismiss();
            }
        });
        noBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    public void noDateInFoucsPrompt(){
        userPromptTv.setText("You Need To Have A Date Selected To See More Data!");
        noBut.setVisibility(View.GONE);
        yesBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
