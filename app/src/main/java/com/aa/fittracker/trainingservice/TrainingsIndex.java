package com.aa.fittracker.trainingservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.aa.fittracker.R;

public class TrainingsIndex extends AppCompatActivity {
    ImageView addButton;
    ImageView browseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainings_index);

        addButton=(ImageView) findViewById(R.id.button5);
        browseButton=(ImageView) findViewById(R.id.button6);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAdd = new Intent(getApplicationContext(),AddTrainingActivity.class);
                startActivity(intentAdd);
            }
        });

        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBrowse = new Intent(getApplicationContext(),BrowseTrainingsActivity.class);
                startActivity(intentBrowse);
            }
        });

    }
}