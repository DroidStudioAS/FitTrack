package com.aa.fittracker.trainingservice;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aa.fittracker.R;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.Training;
import com.aa.fittracker.presentation.trainingAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BrowseTrainingsActivity extends Activity {
    trainingAdapter adapter;
    RecyclerView rv;
    List<Training> dataList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_trainings);

        dataList= store.getUserTrainings();

        rv=(RecyclerView) findViewById(R.id.trainingList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rv.setLayoutManager(layoutManager);

        adapter = new trainingAdapter(dataList,this);
        rv.setAdapter(adapter);

    }
}
