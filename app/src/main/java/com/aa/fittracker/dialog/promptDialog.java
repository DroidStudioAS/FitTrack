package com.aa.fittracker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aa.fittracker.R;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.Training;
import com.aa.fittracker.network.networkHelper;
import com.aa.fittracker.trainingservice.TrainingActivity;

import okhttp3.OkHttpClient;

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
        yesBut.setText("Lets Do It!");
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
        yesBut.setText("Lets Do It!");
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
        yesBut.setText("Ok!");
        noBut.setVisibility(View.GONE);
        yesBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    public void cantDeleteTrainingPrompt(){
        userPromptTv.setText("You Can Not Delete A Training You Have Logged In Your Calendar! " +
                                "Please Delete The Training From Your Log First! It A");
        noBut.setVisibility(View.GONE);
        yesBut.setText("Ok!");
        yesBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
    public void shareTrainingPrompt(Training toAdd, OkHttpClient client){
        userPromptTv.setText("Are You Sure You Want To Add: " + toAdd.getTraining_name() + " To Community Trainings? This Means Other Users Will Be Able To View And Download Your Training.");
        yesBut.setText("Lets Do It!");
        noBut.setText("Maybe Later");
        noBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        yesBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkHelper.addToSharedTrainings(client,toAdd);
                while (store.getServerResponseAddedSharedTraining().equals("")){
                    Log.i("Waiting", "...");
                }
                if(store.getServerResponseAddedSharedTraining().contains("ok") && !store.getServerResponseAddedSharedTraining().contains("!")){
                    //sucess
                    userPromptTv.setText("Thanks For Sharing " + toAdd.getTraining_name()+  " With The FitTracker Community");
                    store.setServerResponseAddedSharedTraining("");
                    noBut.setVisibility(View.GONE);
                    yesBut.setText("Ok!");
                    yesBut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                        }
                    });
                }else if(store.getServerResponseAddedSharedTraining().contains("name is taken")){
                    userPromptTv.setText("Oops.. This Name Is Already Taken. Please Rename Your Training To A Unique Name!");
                    noBut.setVisibility(View.GONE);
                    yesBut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            store.setServerResponseAddedSharedTraining("");
                            dismiss();
                        }
                    });
                }
            }
        });
    }
}
