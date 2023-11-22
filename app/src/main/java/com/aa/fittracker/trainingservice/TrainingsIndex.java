package com.aa.fittracker.trainingservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aa.fittracker.R;

public class TrainingsIndex extends AppCompatActivity {
    Button addButton;
    Button browseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainings_index);

        Intent intentAdd = new Intent(this,AddTrainingActivity.class);
        Intent intentBrowse = new Intent(this,BrowseTrainingsActivity.class);

        addButton=(Button) findViewById(R.id.button5);
        browseButton=(Button) findViewById(R.id.button6);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentAdd);
            }
        });

        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentBrowse);
            }
        });

    }
}