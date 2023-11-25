package com.aa.fittracker;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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
    OkHttpClient client;
    Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        client=new OkHttpClient();
        gson=new Gson();

        /*************Fetch users weight logs****************/
        networkHelper.getWeightLog(client);
        dateTV = (TextView) findViewById(R.id.dateTV);
        /**************Wait for network logic to finish*************/
        while (store.getDateStrings().equals("")){
            Log.i("Fetching data", store.getDateStrings());
        }
        /************map from json*******************/
        WeightEntry[] entryList = gson.fromJson(store.getDateStrings(),WeightEntry[].class);
        for(WeightEntry x : entryList){
            store.addToDatesWithLogs(x.getWeight_date());
            store.addToWeightEntries(x);
        }
        for(String x : store.getDatesWithLogs()){
            Log.i("DATE STRIING ", x);
        }



        
    }

    @Override
    public void onDateClicked(String date) {
        dateTV.setText(date);
    }

    @Override
    public void onMatchFound(WeightEntry x) {
        Log.i("Match found in fragment",x.getWeight_value() + " " + x.getWeight_date());
    }
}