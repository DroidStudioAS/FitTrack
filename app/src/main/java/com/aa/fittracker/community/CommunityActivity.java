package com.aa.fittracker.community;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aa.fittracker.R;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.SharedTraining;
import com.aa.fittracker.models.Training;
import com.aa.fittracker.network.networkHelper;
import com.aa.fittracker.presentation.SharedTrainingAdapter;
import com.aa.fittracker.presentation.trainingAdapter;
import com.aa.fittracker.trainingservice.onItemClickListener;

import okhttp3.OkHttpClient;

public class CommunityActivity extends AppCompatActivity implements onItemClickListener {
    OkHttpClient client;
    SharedTrainingAdapter rvAdapter;

    RecyclerView sharedTrainingView;

    TextView cTrainingNameTv;
    TextView cTrainingDescTv;

    EditText cSearchEt;

    Button cEazyFilterButton;
    Button cMidFilterButton;
    Button cHardFilterButton;

    ImageView cSearchTrigger;
    ImageView cRefreshTrigger;

    int cDiffFilter = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        client = new OkHttpClient();

        /***********Fetch All The Shared Trainings***********/
        networkHelper.getSharedTrainings(client);
        //reset to prevent a filter staying in
        cDiffFilter=-1;

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Community Trainings");
        }

        while (store.getServerResponseSharedTrainings().equals("")) {
            Log.i("Fetching Data", "...");
        }
        /***********SharedTrainingsFetched***********/


        /******Ui Initializations********/
        sharedTrainingView = (RecyclerView) findViewById(R.id.stView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        sharedTrainingView.setLayoutManager(layoutManager);

        cTrainingNameTv = (TextView) findViewById(R.id.communityTrainingNameTv);
        cTrainingDescTv = (TextView) findViewById(R.id.communityTrainingDescTv);

        cSearchEt = (EditText) findViewById(R.id.communitySearch);

        cEazyFilterButton = (Button) findViewById(R.id.communityEazyFilterBut);
        cMidFilterButton = (Button) findViewById(R.id.communityMediumFilterBut);
        cHardFilterButton = (Button) findViewById(R.id.communityHardFilterBut);

        cSearchTrigger = (ImageView) findViewById(R.id.communitySearchTrigger);
        cRefreshTrigger = (ImageView) findViewById(R.id.communityRefreshTrigger);


        //first parameter: the list of trainings that will be displayed:
        rvAdapter = new SharedTrainingAdapter(store.getSharedTrainings(), getApplicationContext());
        rvAdapter.setOnItemClickListener(this);
        sharedTrainingView.setAdapter(rvAdapter);

        /*********OnClickListeners**********/

        cEazyFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cDifficultyFilterClickReaction(1);
            }
        });
        cMidFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cDifficultyFilterClickReaction(2);
            }
        });
        cHardFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cDifficultyFilterClickReaction(3);
            }
        });

    }
    public void cDifficultyFilterClickReaction(int filter_to_activate){
        switch (filter_to_activate){
            case 1:
                cDiffFilter=1;
                cEazyFilterButton.setAlpha(1);
                cMidFilterButton.setAlpha((float)0.4);
                cHardFilterButton.setAlpha((float)0.4);
                break;
            case 2:
                cDiffFilter=2;
                cEazyFilterButton.setAlpha((float)0.4);
                cMidFilterButton.setAlpha(1);
                cHardFilterButton.setAlpha((float)0.4);
                break;
            case 3:
                cDiffFilter=3;
                cEazyFilterButton.setAlpha((float)0.4);
                cMidFilterButton.setAlpha((float)0.4);
                cHardFilterButton.setAlpha(1);
                break;
        }
    }

    public SharedTraining sharedTrainingFactory(Training training){
        SharedTraining toReturn = new SharedTraining();
        toReturn.setShared_training_name(training.getTraining_name());
        toReturn.setShared_training_desc(training.getTraining_desc());
        toReturn.setShared_training_difficulty(training.getTraining_difficulty());
        return toReturn;
    }

    @Override
    public void onTrainingFocus(Training training) {

    }

    @Override
    public void onSharedTrainingFocus(SharedTraining st) {
        cTrainingNameTv.setText(st.getShared_training_name());
        cTrainingDescTv.setText(st.getShared_training_desc());
    }

}