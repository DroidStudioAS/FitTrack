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
import com.aa.fittracker.presentation.trainingAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BrowseTrainingsActivity extends Activity implements onItemClickListener {
    trainingAdapter adapter;
    RecyclerView rv;
    List<Training> dataList;

    TextView nameTv;
    TextView descTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_trainings);
        nameTv=(TextView)findViewById(R.id.showNameTv);
        descTv=(TextView)findViewById(R.id.descTV);

        dataList= store.getUserTrainings();

        rv=(RecyclerView) findViewById(R.id.trainingList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rv.setLayoutManager(layoutManager);

        adapter = new trainingAdapter(dataList,this);
        adapter.setOnItemClickListener(this);
        rv.setAdapter(adapter);

    }

    //callback to set training description to display
    @Override
    public void onTrainingFocus(Training training) {
        nameTv.setText(training.getName());
        descTv.setText(training.getDescription());
    }
}
