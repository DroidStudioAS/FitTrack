package com.aa.fittracker.community;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aa.fittracker.R;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.network.networkHelper;
import com.aa.fittracker.presentation.SharedTrainingAdapter;
import com.aa.fittracker.presentation.trainingAdapter;

import okhttp3.OkHttpClient;

public class CommunityActivity extends AppCompatActivity {
    OkHttpClient client;
    SharedTrainingAdapter rvAdapter;

    RecyclerView sharedTrainingView;

    TextView cTrainingNameTv;
    TextView cTrainingDescTv;

    EditText cSearchEt;

    Button   cEazyFilterButton;
    Button   cMidFilterButton ;
    Button   cHardFilterButton;

    ImageView cSearchTrigger;
    ImageView cRefreshTrigger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        client=new OkHttpClient();

        networkHelper.getSharedTrainings(client);

        ActionBar ab = getSupportActionBar();
        if(ab!=null){
            ab.setTitle("Community Trainings");
        }

        while (store.getServerResponseSharedTrainings().equals("")){
            Log.i("Fetching Data","...");
        }

        /******Ui Initializations********/
        sharedTrainingView=(RecyclerView) findViewById(R.id.stView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL, false);
        sharedTrainingView.setLayoutManager(layoutManager);

        cTrainingNameTv  =(TextView)findViewById(R.id.communityTrainingNameTv);
        cTrainingDescTv  =(TextView)findViewById(R.id.communityTrainingDescTv);

        cSearchEt = (EditText) findViewById(R.id.communitySearch);

        cEazyFilterButton= (Button)findViewById(R.id.communityEazyFilterBut);
        cMidFilterButton = (Button)findViewById(R.id.communityMediumFilterBut);
        cHardFilterButton= (Button)findViewById(R.id.communityHardFilterBut);

         cSearchTrigger  = (ImageView)findViewById(R.id.communitySearchTrigger);
         cRefreshTrigger = (ImageView)findViewById(R.id.communityRefreshTrigger);


        /***********Fetch All The Shared Trainings***********/

        /*******Shared Trainings Fetched******/

        //first parameter: the list of trainings that will be displayed:
        rvAdapter=new SharedTrainingAdapter(store.getSharedTrainings(),getApplicationContext());
        sharedTrainingView.setAdapter(rvAdapter);

    }
}