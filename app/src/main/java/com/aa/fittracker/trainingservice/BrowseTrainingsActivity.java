package com.aa.fittracker.trainingservice;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aa.fittracker.R;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.Training;
import com.aa.fittracker.network.networkHelper;
import com.aa.fittracker.presentation.trainingAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;

public class BrowseTrainingsActivity extends Activity implements onItemClickListener {
    trainingAdapter adapter;
    RecyclerView rv;


    TextView nameTv;
    TextView descTv;
    EditText searchEt;

    Button filter;
    Button reset;

    OkHttpClient clientel;
    List<Training> localList;

    Timer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_trainings);

        searchEt = (EditText) findViewById(R.id.searchET);
        filter = (Button) findViewById(R.id.filter);
        reset = (Button) findViewById(R.id.reset);

        clientel = new OkHttpClient();
        Map<String, String> params = new HashMap<>();
        params.put("username", store.getUSERNAME());
        try {
            networkHelper.getExc(clientel, "http://165g123.e2.mars-hosting.com/api/userinfo/getUserTrainings", params);
        } catch (IOException e) {
            e.printStackTrace();
        }

        timer = new Timer();
        timer.schedule(timerTask, 5000);


        nameTv = (TextView) findViewById(R.id.showNameTv);
        descTv = (TextView) findViewById(R.id.descTV);

        rv = (RecyclerView) findViewById(R.id.trainingList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(layoutManager);


        localList = store.getUserTrainings();
        if (localList.isEmpty()) {
            while (localList.isEmpty()) {
                localList = store.getUserTrainings();
                Log.i("Fetching data: ", localList.toString());
            }
        }
        adapter = new trainingAdapter(localList, this);
        adapter.setOnItemClickListener(this);
        rv.setAdapter(adapter);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("filter triggered", "true");
               List<Training> filtered = new ArrayList<>();
               String input = searchEt.getText().toString();
               for(Training x : store.getUserTrainings()){
                   if(x.getTraining_name().toLowerCase(Locale.ROOT).contains(input.toLowerCase(Locale.ROOT))){
                       Log.i("match found on click",x.toString());
                       filtered.add(x);
                   }
               }
               adapter.setDataList(filtered);
               rv.setAdapter(adapter);
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList();

            }
        });


    }
    public void refreshList(){
        Log.i("refresh triggered", "true");
        adapter.setDataList(store.getUserTrainings());
        rv.setAdapter(adapter);
    }
    //callback to set training description to display
    @Override
    public void onTrainingFocus(Training training) {
        nameTv.setText(training.getTraining_name());
        descTv.setText(training.getTraining_desc());
    }

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            localList=store.getUserTrainings();
        }
    };
}


