package com.aa.fittracker;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aa.fittracker.dialog.InputDialog;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.TrainingEntry;
import com.aa.fittracker.models.WeightEntry;
import com.aa.fittracker.network.networkHelper;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;

public class calendarActivity extends AppCompatActivity implements OnDateClickListener {

    TextView dateTV;
    TextView infoLabel;
    TextView optimalLabel;
    TextView infoTv;
    TextView optimalTv;

    Button missingInfoButton;
    Button missingOptimalButton;
    Button deleteEntryTrigger;

    Context context;


    OkHttpClient client;
    Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        /*******************Ui Initializations**********************/
        infoLabel = (TextView) findViewById(R.id.infoLabel);
        optimalLabel = (TextView) findViewById(R.id.optimalLabel);
        infoTv = (TextView) findViewById(R.id.infoTv);
        optimalTv = (TextView) findViewById(R.id.optimalTv);
        dateTV = (TextView) findViewById(R.id.dateTV);

        missingInfoButton=(Button)findViewById(R.id.missingInfoButton);
        missingOptimalButton=(Button)findViewById(R.id.missingOptimalButton);
        deleteEntryTrigger=(Button)findViewById(R.id.deleteEntryTrigger);
        deleteEntryTrigger.setVisibility(View.INVISIBLE);
        /**********************Neccesary**********************/
        client = new OkHttpClient();
        gson = new Gson();
        context=getApplicationContext();
        /*********************User mode determiner**********************/
        Intent incoming = getIntent();
        switch (store.getUserMode()) {
            case "journal":
                labelSeter("Trained:", "Rest Day:");

                break;
            case "weight":
                labelSeter("Weight:","Optimal:");
                optimalTv.setText(store.getUserWeightKg());
                break;
            case "cals":
                labelSeter("Intake:","Allowed:");
                break;
        }
        /************************OnClickListeners***************************/
        missingInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(calendarActivity.this,InputInfoActivity.class);
                startActivity(intent);*/
                InputDialog inputDialog = new InputDialog(calendarActivity.this);
                inputDialog.show();
                missingInfoButton.setVisibility(View.GONE);
            }
        });
        deleteEntryTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**********Param Validation**********/
                /********Execution switch********/
                switch (store.getUserMode()){
                    case "journal":
                        networkHelper.deleteExcEntry(client);
                        /**********Result Validation***************/
                        while (store.getServerResponseDeletedTrainingEntry().equals("")){
                            Log.i("Waiting...","...");
                        }
                        if(store.getServerResponseDeletedTrainingEntry().contains("ok") &&
                                !store.getServerResponseDeletedTrainingEntry().contains("!")){
                            Toast.makeText(calendarActivity.this, "Deleted Training Entry For: " + store.getDateInFocus(),Toast.LENGTH_SHORT).show();
                            listRemover(1);
                        }
                        break;
                    case "weight":
                        networkHelper.deleteWeightEntry(client);
                        while (store.getServerResponseDeletedWeightEntry().equals("")){
                            Log.i("Waiting...","...");
                        }
                        if(store.getServerResponseDeletedWeightEntry().contains("ok") &&
                                !store.getServerResponseDeletedTrainingEntry().contains("!")){
                            Toast.makeText(calendarActivity.this, "Deleted Weight Entry For: " + store.getDateInFocus(),Toast.LENGTH_SHORT).show();
                            listRemover(2);
                        }
                       /**************clear the button so no malicous deletes happed*******************/
                }
                deleteEntryTrigger.setVisibility(View.INVISIBLE);



            }
        });
    }
    /****************Helper functions begin*******************/
    //removes resutls from frontend list for instant refresh
    public void listRemover(int i){
        //i=1-TRAININGSERVICE
        //I=2 - WEIGHTSERVICE
        switch (i){
            case 1:
                Log.i("size before",String.valueOf(store.getTrainingEntries().size()));
                store.removeFromTrainingEntries(store.getDateInFocus());
                Log.i("size after",String.valueOf(store.getTrainingEntries().size()));
                break;
            case 2:
                Log.i("size before",String.valueOf(store.getWeightEntries().size()));
                store.removeFromWeightEntries(store.getDateInFocus());
                Log.i("size after",String.valueOf(store.getWeightEntries().size()));
                break;
        }
    }

    public void labelSeter(String infoString,String optimalString){
        infoLabel.setText(infoString);
        optimalLabel.setText(optimalString);
    }
    /****************Helper functions end*******************/
    /*******************Callbacks received********************/
    @Override
    public void onDateClicked(String date) {
        dateTV.setText(date);
        infoTv.setText("");
        missingInfoButton.setVisibility(View.VISIBLE);
        deleteEntryTrigger.setVisibility(View.INVISIBLE);
    }
    /************** !very important this callback happens after onDateClicked! **************/
    @Override
    public void onMatchFound(WeightEntry x) {
        if(store.getUserMode().equals("weight")) {
            Log.i("Weight Match Found", x.getWeight_value() + " " + x.getWeight_date());
            missingInfoButton.setVisibility(View.GONE);
            infoTv.setText(x.getWeight_value());
            deleteEntryTrigger.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTrainingMatchFound(TrainingEntry x) {
        if (store.getUserMode().equals("journal")) {
            Log.i("Training Match Found", x.getTraining_name() + " " + x.getTraining_date());
            missingInfoButton.setVisibility(View.GONE);
            infoTv.setText(x.getTraining_name());
            deleteEntryTrigger.setVisibility(View.VISIBLE);
        }
    }


}