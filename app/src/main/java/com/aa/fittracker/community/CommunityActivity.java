package com.aa.fittracker.community;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.aa.fittracker.R;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.presentation.trainingAdapter;

public class CommunityActivity extends AppCompatActivity {
    RecyclerView sharedTrainingView;
    trainingAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        /******Ui Initializations********/
        sharedTrainingView=(RecyclerView) findViewById(R.id.stView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL, false);
        sharedTrainingView.setLayoutManager(layoutManager);
        /***********Fetch All The Shared Trainings***********/

        /*******Shared Trainings Fetched******/

        //first parameter: the list of trainings that will be displayed:
        rvAdapter=new trainingAdapter(store.getUserTrainings(),getApplicationContext());
        sharedTrainingView.setAdapter(rvAdapter);
    }
}