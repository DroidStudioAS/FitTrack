package com.aa.fittracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.TrainingEntry;
import com.aa.fittracker.models.WeightEntry;
import com.aa.fittracker.network.networkHelper;
import com.aa.fittracker.trainingservice.TrainingActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

public class IndexActivity extends AppCompatActivity {
OkHttpClient client;
OkHttpClient clientel;

TextView desiredWeightTv;
TextView currentWeightTv;
TextView startWeightTv;
TextView welcomeTv;


ImageView journalButton;
ImageView trainingsButton;
ImageView noutritionButton;
ImageView weightButton;

ConstraintLayout root;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.basic_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        /******************UI initializations***********************/
        root=(ConstraintLayout)findViewById(R.id.root);
        currentWeightTv=(TextView)findViewById(R.id.CurrentWeightTv);
        desiredWeightTv = (TextView)findViewById(R.id.desiredWeightTv);
        startWeightTv = (TextView)findViewById(R.id.startWeightTv);
        welcomeTv = (TextView)findViewById(R.id.welcomeTv);

        journalButton =(ImageView) findViewById(R.id.button);
        trainingsButton =(ImageView) findViewById(R.id.button2);
        weightButton =(ImageView) findViewById(R.id.button3);
        noutritionButton =(ImageView) findViewById(R.id.button4);
        /***********ActionBar*************/
        ActionBar ab = getSupportActionBar();
        if(ab!=null){
            ab.setDisplayShowHomeEnabled(true);
            ab.setTitle("Fit Tracker");
        }

        /****************Network Block******************/
        client=new OkHttpClient();
        Map<String, String> params = new HashMap<>();
        params.put("username", store.getUSERNAME());
        boolean new_user = getIntent().getBooleanExtra("new_user",false);
        if(!new_user) {
            try {
                networkHelper.getExc(client, "http://165g123.e2.mars-hosting.com/api/userinfo/getUserTrainings", params);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //fetch user desiredWeight
            networkHelper.getWeight(client);
            networkHelper.getExcEntries(client);
            networkHelper.getStartWeight(client);
            while (store.getTrainingEntries().equals("") || store.getUserWeightKg().equals("") || store.getUserStartWeight().equals("")) {
                Log.i("Fetching Data", "...");
            }
            //fetches weight log
        }else{
            currentWeightTv.setText(store.getCurrentUserWeight() + "KG");
            desiredWeightTv.setText(store.getUserWeightKg());
        }
        userWeightModeActivate();


        Intent intent = new Intent(this,calendarActivity.class);

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        String dateTodaycalendar = calendar.get(Calendar.YEAR) + "-" +month + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        Log.i("Date today", dateTodaycalendar);
        for(WeightEntry x : store.getWeightEntries()){
            if(x.getWeight_date().contains(dateTodaycalendar)){
                store.setCurrentUserWeight(x.getWeight_value());
                currentWeightTv.setText(store.getCurrentUserWeight() + " KG");
            }
        }


       /* MenuItem menuItem = new MenuItem(this);
        menuItem.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.noutrition_service_drawable));
        Point initialPosition = new Point(200, 200);
        menuItem.setPosition(initialPosition);
        root.addView(menuItem);*/


        welcomeTv.setText("Welcome: " + store.getUSERNAME());
        startWeightTv.setText(store.getUserStartWeight() + "KG");
        desiredWeightTv.setText(store.getUserWeightKg() + " KG");
       /* journalButton.setVisibility(View.GONE);
        weightButton.setVisibility(View.GONE);
        trainingsButton.setVisibility(View.GONE);*/

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
                Toast.makeText(getApplicationContext(),"Currently Not Supported...",Toast.LENGTH_SHORT).show();
                /*store.setUserMode("cals");
                intent.putExtra("key","cals");
                startActivity(intent);*/
            }
        });


        trainingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrainingActivity.class);
                startActivity(intent);
            }
        });
        Logger();


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
    public static void Logger(){
        for(TrainingEntry x : store.getTrainingEntries()){
            Log.i("Training Entry Date", x.getTraining_date());
            Log.i("Training Entry Value", x.getTraining_name());
        }
        for(WeightEntry x : store.getWeightEntries()){
            Log.i("Weight Entry Date", x.getWeight_date());
            Log.i("Weight Entry Value", x.getWeight_value());
        }
    }

}
