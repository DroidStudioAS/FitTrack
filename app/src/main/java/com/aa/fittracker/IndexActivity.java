package com.aa.fittracker;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.aa.fittracker.logic.JsonParser;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.network.networkHelper;

import okhttp3.OkHttpClient;

public class IndexActivity extends AppCompatActivity {
OkHttpClient client;

TextView desiredWeightView;

Button journalButton;
Button trainingsButton;
Button noutritionButton;
Button weightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        desiredWeightView = (TextView)findViewById(R.id.desiredWeightTv);

        client=new OkHttpClient();
        networkHelper.getWeight(client);
        while (store.getUserWeightKg().equals("")){
            Log.i("Waiting",store.getUserWeightKg());
        }
        desiredWeightView.setText(JsonParser.parsemsg(store.getUserWeightKg()));



    }
}