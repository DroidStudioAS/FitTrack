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
import com.aa.fittracker.dialog.promptDialog;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.SharedTraining;
import com.aa.fittracker.models.Training;
import com.aa.fittracker.network.networkHelper;
import com.aa.fittracker.presentation.SharedTrainingAdapter;
import com.aa.fittracker.presentation.trainingAdapter;
import com.aa.fittracker.trainingservice.onItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    ImageView downloadTrigger;

    SharedTraining toDownload;

    int cDiffFilter = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        client = new OkHttpClient();

        toDownload=new SharedTraining();

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
        downloadTrigger=(ImageView)findViewById(R.id.downloadTrigger);


        //first parameter: the list of trainings that will be displayed:

        if(store.getSharedTrainings()!=null && !store.getSharedTrainings().isEmpty()){
            store.sortSharedTrainings();
        }
        rvAdapter = new SharedTrainingAdapter(store.getSharedTrainings(), getApplicationContext());
        rvAdapter.setOnItemClickListener(this);
        sharedTrainingView.setAdapter(rvAdapter);

        downloadTrigger.setVisibility(View.INVISIBLE);

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
        downloadTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Training toAdd = new Training();
                toAdd.setTraining_name(toDownload.getShared_training_name());
                toAdd.setTraining_desc(toDownload.getShared_training_desc());
                toAdd.setTraining_difficulty(toDownload.getShared_training_difficulty());

                promptDialog pd = new promptDialog(CommunityActivity.this);
                pd.show();
                pd.downloadTrainingPrompt(client, toAdd);
                pd.setCancelable(false);
            }
        });
        cRefreshTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCList();
            }
        });
        cSearchTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<SharedTraining> filtered = filterByDifficulty(store.getSharedTrainings());
                filtered=filterByName(filtered,cSearchEt.getText().toString());
                rvAdapter.setDataList(filtered);
                sharedTrainingView.setAdapter(rvAdapter);
            }
        });

    }
    public void cDifficultyFilterClickReaction(int filter_to_activate){
        switch (filter_to_activate){
            case -1:
                cDiffFilter=-1;
                cEazyFilterButton.setAlpha((float) 0.4);
                cMidFilterButton.setAlpha((float)0.4);
                cHardFilterButton.setAlpha((float)0.4);
                break;
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




    public List<SharedTraining> filterByDifficulty(List<SharedTraining> list){
        //if the diff filter is not active it returns all trainins
        //so, the text filter can be applied, to the returned list (should work in all circumstances);
        if(cDiffFilter==-1){
            return store.getSharedTrainings();
        }
        List<SharedTraining> filtered = new ArrayList<>();
        for(SharedTraining x : list){
            if(x.getShared_training_difficulty()==cDiffFilter){
                filtered.add(x);
            }
        }
        return filtered;
    }
    public List<SharedTraining> filterByName(List<SharedTraining> list, String input){
        if(input.equals("")){
            return list;
        }
        List<SharedTraining> toReturn = new ArrayList<>();
        for(SharedTraining x : list){
            if(x.getShared_training_name().toLowerCase(Locale.ROOT).contains(input.toLowerCase(Locale.ROOT))){
                toReturn.add(x);
            }
        }
        return toReturn;
    }
    public void resetCList(){
        cDifficultyFilterClickReaction(-1);
        cSearchEt.setText("");
        List<SharedTraining> general = store.getSharedTrainings();
        rvAdapter.setDataList(general);
        sharedTrainingView.setAdapter(rvAdapter);

        cTrainingDescTv.setText("");
        cTrainingNameTv.setText("No Training Selected");
        downloadTrigger.setVisibility(View.GONE);
    }

    @Override
    public void onTrainingFocus(Training training) {

    }

    @Override
    public void onSharedTrainingFocus(SharedTraining st) {
        cTrainingNameTv.setText(st.getShared_training_name());
        cTrainingDescTv.setText(st.getShared_training_desc());
        downloadTrigger.setVisibility(View.VISIBLE);

        toDownload=st;
        Log.i("td", toDownload.toString());
    }


}