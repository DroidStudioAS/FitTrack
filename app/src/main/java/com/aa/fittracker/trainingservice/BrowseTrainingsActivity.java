package com.aa.fittracker.trainingservice;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aa.fittracker.R;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.Training;
import com.aa.fittracker.network.networkHelper;
import com.aa.fittracker.presentation.trainingAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;

public class BrowseTrainingsActivity extends Activity implements onItemClickListener {
    trainingAdapter adapter;
    RecyclerView rv;


    TextView nameTv;
    TextView descTv;
    EditText searchEt;

    Button filter;
    Button reset;
    Button deleteBut;
    Button editBut;

    Button easyFilter;
    Button mediumFilter;
    Button hardFilter;

    OkHttpClient clientel;
    List<Training> localList;

    Timer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_trainings);

        searchEt = (EditText) findViewById(R.id.searchET);

        filter = (Button) findViewById(R.id.filter);
        reset = (Button) findViewById(R.id.reset);
        deleteBut=(Button)findViewById(R.id.deleteTrigger);
        editBut=(Button)findViewById(R.id.editTrigger);

        easyFilter = (Button)findViewById(R.id.easyFilter);
        mediumFilter = (Button)findViewById(R.id.mediumFilter);
        hardFilter=(Button)findViewById(R.id.hardFilter);



        clientel = new OkHttpClient();
        Map<String, String> params = new HashMap<>();
        params.put("username", store.getUSERNAME());
        try {
            networkHelper.getExc(clientel, "http://165g123.e2.mars-hosting.com/api/userinfo/getUserTrainings", params);
        } catch (IOException e) {
            e.printStackTrace();
        }


        timer = new Timer();
        timer.schedule(timerTask, 5000);


        nameTv = (TextView) findViewById(R.id.showNameTv);
        descTv = (TextView) findViewById(R.id.descTV);

        rv = (RecyclerView) findViewById(R.id.trainingList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(layoutManager);


        localList = store.getUserTrainings();
        if (localList.isEmpty()) {
                while (store.getServerResponseAllExc().equals("")) {
                    localList = store.getUserTrainings();
                    Log.i("Fetching data: ", localList.toString());
                }
            }

        adapter = new trainingAdapter(localList, this);
        adapter.setOnItemClickListener(this);
        rv.setAdapter(adapter);


        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("filter triggered", "true");
                List<Training> filtered = new ArrayList<>();
                String input = searchEt.getText().toString();
                //check if diff filter is applied

                for(Training x : store.getUserTrainings()){
                    if(x.getTraining_name().toLowerCase(Locale.ROOT).contains(input.toLowerCase(Locale.ROOT))){
                        Log.i("match found on click",x.toString());
                        filtered.add(x);
                    }
                }
                if(store.getActiveDifficultyFilter()!=-1){
                            filtered=filterByDifficulty(filtered);
                    }
               adapter.setDataList(filtered);
               rv.setAdapter(adapter);
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEt.setText("");
                easyFilter.setAlpha((float)0.4);
                mediumFilter.setAlpha((float)0.4);
                hardFilter.setAlpha((float)0.4);
                refreshList();

            }
        });
        easyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difficultyFilterClickReaction(1);
            }
        });
        mediumFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difficultyFilterClickReaction(2);
            }
        });
        hardFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               difficultyFilterClickReaction(3);
            }
        });
        deleteBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(store.getTrainingToDeleteName().equals("")){
                    Toast.makeText(getApplicationContext(),"No training selected",Toast.LENGTH_SHORT).show();
                }else{
                    Snackbar deleteWarning = Snackbar.make(v,"Are you sure you want to delete "+store.getTrainingToDeleteName()+" ?",Snackbar.LENGTH_INDEFINITE);
                    deleteWarning.setAction("Yes", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            networkHelper.deleteExc(clientel);
                            Training toDelete = new Training();
                            for(Training x : store.getUserTrainings()){
                                if(x.getTraining_name().equals(store.getTrainingToDeleteName())){
                                    toDelete=x;
                                }
                            }
                            store.removeFromUserTrainings(toDelete);
                            refreshList();
                        }
                    });
                    deleteWarning.show();
                }

            }
        });
        editBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store.setEditModeActive();
                Log.i("active?:",String.valueOf(store.isEditModeActive()));
            }
        });
    }
    public void difficultyFilterClickReaction(int filter_to_activate){
        switch (filter_to_activate){
            case 1:
                store.setActiveDifficultyFilter(filter_to_activate);
                easyFilter.setAlpha(1);
                mediumFilter.setAlpha((float)0.4);
                hardFilter.setAlpha((float)0.4);
                break;
            case 2:
                store.setActiveDifficultyFilter(filter_to_activate);
                easyFilter.setAlpha((float)0.4);
                mediumFilter.setAlpha(1);
                hardFilter.setAlpha((float)0.4);
                break;
            case 3:
                store.setActiveDifficultyFilter(filter_to_activate);
                easyFilter.setAlpha((float)0.4);
                mediumFilter.setAlpha((float)0.4);
                hardFilter.setAlpha(1);
                break;
        }
        Log.i("Activity Filter Change:", String.valueOf(store.getActiveDifficultyFilter()));
    }
    public List<Training> filterByDifficulty(List<Training> list){
        List<Training> filtered = new ArrayList<>();
        for(Training x : list){
            if(x.getTraining_difficulty()==store.getActiveDifficultyFilter()){
                filtered.add(x);
            }
        }
        return filtered;
    }
    //sets the list to the original list fetched by the networkHelper
    public void refreshList(){
        Log.i("refresh triggered", "true");
        adapter.setDataList(store.getUserTrainings());
        store.clearFilteredUserTrainings();
        rv.setAdapter(adapter);

        deleteBut.setVisibility(View.GONE);
        editBut.setVisibility(View.GONE);

        nameTv.setText("No Training Selected");
        descTv.setText("");
    }

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            localList=store.getUserTrainings();
        }
    };
    /*****************onItemClickListener Callback****************/
    //callback to set training description to display
    @Override
    public void onTrainingFocus(Training training) {
        nameTv.setText(training.getTraining_name());
        descTv.setText(training.getTraining_desc());
        deleteBut.setVisibility(View.VISIBLE);
        editBut.setVisibility(View.VISIBLE);
        //store the name of the training in focus as a parameter in case user wants to delete
        store.setTrainingToDeleteName(training.getTraining_name());
    }
    public void difficultyFilter(List<Training> toMutate){

    }
}


