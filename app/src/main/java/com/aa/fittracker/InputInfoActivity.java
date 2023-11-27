package com.aa.fittracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.Training;
import com.aa.fittracker.models.TrainingEntry;
import com.aa.fittracker.network.networkHelper;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class InputInfoActivity extends AppCompatActivity {
    Spinner spinner;
    Button confirmTrigger;

    OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_info);

        client = new OkHttpClient();

        spinner=(Spinner) findViewById(R.id.trainingSelector);
        confirmTrigger=(Button)findViewById(R.id.confirmTrigger);
        List<String> stringList = new ArrayList<>();
        for(Training x : store.getUserTrainings()){
            stringList.add(x.getTraining_name());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,stringList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        confirmTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trainingSelected = spinner.getSelectedItem().toString();
                Log.i("selected",trainingSelected);
                networkHelper.postExcEntry(client,trainingSelected);
                while(store.getServerResponseAdderTrainingEntry().equals("")){
                    Log.i("waiting",store.getServerResponseAdderTrainingEntry());
                }
                store.addToTrainingEntries(new TrainingEntry(trainingSelected,store.getDateInFocus()));
            }
        });
    }
}