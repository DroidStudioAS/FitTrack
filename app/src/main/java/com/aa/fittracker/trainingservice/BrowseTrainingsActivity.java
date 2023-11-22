package com.aa.fittracker.trainingservice;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aa.fittracker.R;
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

        Training one = new Training(0,1,"easy 1 blablablablalbalblablablab","loremipsumloremipsum");
        Training two = new Training(0,2,"medium 2","loremipsumloremipsum");
        Training three = new Training(0,3,"hard 3","loremipsumloremipsum");
        Training four = new Training(0,1,"easy again 4","loremipsumloremipsum");
        dataList = Arrays.asList(one,two,three,four);

        rv=(RecyclerView) findViewById(R.id.trainingList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rv.setLayoutManager(layoutManager);

        adapter = new trainingAdapter(dataList,this);
        rv.setAdapter(adapter);

    }
}
