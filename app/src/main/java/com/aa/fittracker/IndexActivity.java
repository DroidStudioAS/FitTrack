package com.aa.fittracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aa.fittracker.logic.store;
import com.aa.fittracker.network.networkHelper;
import com.aa.fittracker.trainingservice.TrainingsIndex;

import okhttp3.OkHttpClient;

public class IndexActivity extends AppCompatActivity {
OkHttpClient client;

TextView desiredWeightView;
TextView welcomeTv;

ImageView journalButton;
ImageView trainingsButton;
ImageView noutritionButton;
ImageView weightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        Intent intent = new Intent(this,calendarActivity.class);

        desiredWeightView = (TextView)findViewById(R.id.desiredWeightTv);
        welcomeTv = (TextView)findViewById(R.id.welcomeTv);

        journalButton =   (ImageView) findViewById(R.id.button);
        trainingsButton = (ImageView) findViewById(R.id.button2);
        weightButton =    (ImageView) findViewById(R.id.button3);
        noutritionButton =(ImageView) findViewById(R.id.button4);

        ImageView[] buts = new ImageView[]{journalButton,weightButton,noutritionButton};


        client=new OkHttpClient();
        //fetch user desiredWeight
        networkHelper.getWeight(client);
        while (store.getUserWeightKg().equals("")){
            Log.i("Waiting",store.getUserWeightKg());
        }

        welcomeTv.setText("Welcome: " + store.getUSERNAME());
        desiredWeightView.setText(store.getUserWeightKg());
        journalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("key","journal");
                startActivity(intent);
            }
        });
       weightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("key","weight");
                startActivity(intent);
            }
        });
        noutritionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("key","cals");
                startActivity(intent);
            }
        });


        trainingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrainingsIndex.class);
                startActivity(intent);
            }
        });

    }
}