package com.aa.fittracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aa.fittracker.logic.JsonParser;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.network.networkHelper;
import com.aa.fittracker.trainingservice.TrainingsIndex;

import okhttp3.OkHttpClient;

public class IndexActivity extends AppCompatActivity {
OkHttpClient client;

TextView desiredWeightView;
TextView welcomeTv;

Button journalButton;
Button trainingsButton;
Button noutritionButton;
Button weightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        desiredWeightView = (TextView)findViewById(R.id.desiredWeightTv);
        welcomeTv = (TextView)findViewById(R.id.welcomeTv);

        journalButton = (Button) findViewById(R.id.button);
        trainingsButton = (Button) findViewById(R.id.button2);
        weightButton = (Button) findViewById(R.id.button3);
        noutritionButton = (Button) findViewById(R.id.button4);

        Button[] buts = new Button[]{journalButton,weightButton,noutritionButton};


        client=new OkHttpClient();
        //fetch user desiredWeight
        networkHelper.getWeight(client);
        while (store.getUserWeightKg().equals("")){
            Log.i("Waiting",store.getUserWeightKg());
        }

        welcomeTv.setText("Welcome: " + store.getUSERNAME());
        desiredWeightView.setText(JsonParser.parsemsg(store.getUserWeightKg()));

        for(Button x : buts){
            x.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),calendarActivity.class);
                    startActivity(intent);
                }
            });
        }
        trainingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrainingsIndex.class);
                startActivity(intent);
            }
        });

    }
}