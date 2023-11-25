package com.aa.fittracker;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.WeightEntry;
import com.aa.fittracker.network.networkHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class calendarActivity extends AppCompatActivity implements OnDateClickListener {

    TextView dateTV;
    TextView infoLabel;
    TextView optimalLabel;
    TextView infoTv;
    TextView optimalTv;

    Button missingInfoButton;
    Button missingOptimalButton;


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
        /**********************Neccesary**********************/
        client = new OkHttpClient();
        gson = new Gson();
        /*********************User mode determiner**********************/
        Intent incoming = getIntent();
        store.setUserMode(incoming.getStringExtra("key"));
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
    }
    /****************Helper functions begin*******************/

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
    }
    /************** !very important this callback happens after onDateClicked! **************/
    @Override
    public void onMatchFound(WeightEntry x) {
        if(store.getUserMode().equals("weight")) {
            Log.i("Match found in fragment", x.getWeight_value() + " " + x.getWeight_date());
            missingInfoButton.setVisibility(View.GONE);
            infoTv.setText(x.getWeight_value());
        }
    }

}