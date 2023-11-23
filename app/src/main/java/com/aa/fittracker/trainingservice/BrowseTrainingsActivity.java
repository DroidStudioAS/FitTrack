package com.aa.fittracker.trainingservice;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;

public class BrowseTrainingsActivity extends Activity implements onItemClickListener {
    trainingAdapter adapter;
    RecyclerView rv;


    TextView nameTv;
    TextView descTv;

    OkHttpClient clientel;
    List<Training> localList;

    Timer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_trainings);
        timer=new Timer();
        timer.schedule(timerTask,5000);

        localList = store.getUserTrainings();

        clientel=new OkHttpClient();

        nameTv=(TextView)findViewById(R.id.showNameTv);
        descTv=(TextView)findViewById(R.id.descTV);

        rv=(RecyclerView) findViewById(R.id.trainingList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rv.setLayoutManager(layoutManager);


        adapter = new trainingAdapter(localList,this);
        adapter.setOnItemClickListener(this);
        rv.setAdapter(adapter);

        Map<String,String> params = new HashMap<>();
        params.put("username",store.getUSERNAME());
        try {
            networkHelper.getExc(clientel,"http://165g123.e2.mars-hosting.com/api/userinfo/getUserTrainings",params);
        } catch (IOException e) {
            e.printStackTrace();
        }


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
