package com.aa.fittracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.WeightEntry;
import com.aa.fittracker.network.networkHelper;
import com.aa.fittracker.trainingservice.TrainingsIndex;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

public class IndexActivity extends AppCompatActivity {
OkHttpClient client;
OkHttpClient clientel;

TextView desiredWeightView;
TextView weightTv;
TextView welcomeTv;

ImageView journalButton;
ImageView trainingsButton;
ImageView noutritionButton;
ImageView weightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        /******************UI initializations***********************/
        weightTv=(TextView)findViewById(R.id.weightTv);
        desiredWeightView = (TextView)findViewById(R.id.desiredWeightTv);
        welcomeTv = (TextView)findViewById(R.id.welcomeTv);

        journalButton =   (ImageView) findViewById(R.id.button);
        trainingsButton = (ImageView) findViewById(R.id.button2);
        weightButton =    (ImageView) findViewById(R.id.button3);
        noutritionButton =(ImageView) findViewById(R.id.button4);

        /****************Network Block******************/
        client=new OkHttpClient();

        //fetch user desiredWeight
        networkHelper.getWeight(client);
        networkHelper.getExcEntries(client);
        while (store.getTrainingEntries().equals("") || store.getUserWeightKg().equals("")){
            Log.i("Waiting",store.getUserWeightKg());
        }
        userWeightModeActivate(); //fetches weight log


        Intent intent = new Intent(this,calendarActivity.class);

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        String dateTodaycalendar = calendar.get(Calendar.YEAR) + "-" +month + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        Log.i("Date today", dateTodaycalendar);
        for(WeightEntry x : store.getWeightEntries()){
            if(x.getWeight_date().contains(dateTodaycalendar)){
                weightTv.setText(x.getWeight_value());
            }
        }



        ImageView[] buts = new ImageView[]{journalButton,weightButton,noutritionButton};








        welcomeTv.setText("Welcome: " + store.getUSERNAME());
        desiredWeightView.setText(store.getUserWeightKg());
        journalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store.setUserMode("journal");
                intent.putExtra("key","journal");
                startActivity(intent);
            }
        });
       weightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store.setUserMode("weight");
                intent.putExtra("key","weight");
                startActivity(intent);
            }
        });
        noutritionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store.setUserMode("cals");
                intent.putExtra("key","cals");
                startActivity(intent);
            }
        });


        trainingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrainingsIndex.class);
                startActivity(intent);
            }
        });
        clientel = new OkHttpClient();
        Map<String, String> params = new HashMap<>();
        params.put("username", store.getUSERNAME());

        try {
            networkHelper.getExc(clientel, "http://165g123.e2.mars-hosting.com/api/userinfo/getUserTrainings", params);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void userWeightModeActivate(){
        /*************Fetch users weight logs****************/
        OkHttpClient client = new OkHttpClient();
        networkHelper.getWeightLog(client);
        Gson gson = new Gson();
        /**************Wait for network logic to finish*************/
        while (store.getDateStrings().equals("")) {
            Log.i("Fetching data", store.getDateStrings());
        }
        /************map from json*******************/
        WeightEntry[] entryList = gson.fromJson(store.getDateStrings(), WeightEntry[].class);
        for (WeightEntry x : entryList) {
            store.addToDatesWithLogs(x.getWeight_date());
            store.addToWeightEntries(x);
        }
        for (String x : store.getDatesWithLogs()) {
            Log.i("DATE STRIING ", x);
        }
    }
}
