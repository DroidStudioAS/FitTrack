package com.aa.fittracker.community;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.aa.fittracker.R;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.network.networkHelper;
import com.aa.fittracker.presentation.SharedTrainingAdapter;
import com.aa.fittracker.presentation.trainingAdapter;

import okhttp3.OkHttpClient;

public class CommunityActivity extends AppCompatActivity {
    OkHttpClient client;

    RecyclerView sharedTrainingView;
    SharedTrainingAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        client=new OkHttpClient();

        networkHelper.getSharedTrainings(client);

        while (store.getServerResponseSharedTrainings().equals("")){
            Log.i("Fetching Data","...");
        }

        /******Ui Initializations********/
        sharedTrainingView=(RecyclerView) findViewById(R.id.stView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL, false);
        sharedTrainingView.setLayoutManager(layoutManager);
        /***********Fetch All The Shared Trainings***********/

        /*******Shared Trainings Fetched******/

        //first parameter: the list of trainings that will be displayed:
        rvAdapter=new SharedTrainingAdapter(store.getSharedTrainings(),getApplicationContext());
        sharedTrainingView.setAdapter(rvAdapter);

    }
}