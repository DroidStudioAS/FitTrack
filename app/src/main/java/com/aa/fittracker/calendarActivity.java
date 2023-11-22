package com.aa.fittracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class calendarActivity extends AppCompatActivity {

    TextView dateTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        dateTV = (TextView) findViewById(R.id.dateTV);
        
    }
}