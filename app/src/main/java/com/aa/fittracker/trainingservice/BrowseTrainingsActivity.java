package com.aa.fittracker.trainingservice;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
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
    EditText editDescET;
    EditText editNameET;

    Button filter;
    Button reset;
    Button deleteBut;
    Button editBut;
    Button easyFilter;
    Button mediumFilter;
    Button hardFilter;
    Button patchTrigger;

    OkHttpClient clientel;
    List<Training> localList;

    Timer timer;

    ScrollView browseView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_trainings);
         /************Variable Initialization**************/
        searchEt = (EditText) findViewById(R.id.searchET);
        editDescET=(EditText)findViewById(R.id.editDescET);
        editNameET=(EditText)findViewById(R.id.editTitleET);

        filter = (Button) findViewById(R.id.filter);
        reset = (Button) findViewById(R.id.reset);
        deleteBut=(Button)findViewById(R.id.deleteTrigger);
        editBut=(Button)findViewById(R.id.editTrigger);
        easyFilter = (Button)findViewById(R.id.easyFilter);
        mediumFilter = (Button)findViewById(R.id.mediumFilter);
        hardFilter=(Button)findViewById(R.id.hardFilter);
        patchTrigger=(Button)findViewById(R.id.patchTrigger);

        browseView=(ScrollView)findViewById(R.id.scrollView2);



        //fetch user exercises
        clientel = new OkHttpClient();
        Map<String, String> params = new HashMap<>();
        params.put("username", store.getUSERNAME());

        try {
            networkHelper.getExc(clientel, "http://165g123.e2.mars-hosting.com/api/userinfo/getUserTrainings", params);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        /**************RV Configuration*******************/
        adapter = new trainingAdapter(localList, this);
        adapter.setOnItemClickListener(this);
        rv.setAdapter(adapter);

        /**************OnClickListeners*******************/
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
                edit_browse_toggler();
            }
        });
        patchTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //POTENTIAL PARAMETERS FOR BACKEND (MUST BE VALIDATED AND SANITIZED) TODO:sanitize inputs
                String newName = editNameET.getText().toString().toUpperCase(Locale.ROOT);
                String newDesc = editDescET.getText().toString().toUpperCase(Locale.ROOT);
                String newDiff = String.valueOf(store.getActiveDifficultyFilter());

                Map<String,String> params = new HashMap<>();
                //CHECK IF INPUTS ARE EMPTY BEFORE PUTTING THEM AS PARAMETERS IN THE PATCH MAP
                if(!store.getUSERNAME().equals("")){
                    params.put("username",store.getUSERNAME());
                }
                if(!store.getTrainingInFocus().getTraining_name().equals("")){
                    params.put("old_name",store.getTrainingInFocus().getTraining_name());
                }
                if(!newName.equals("")){
                    params.put("training_name",newName);
                }
                if(!newDesc.equals("")){
                    params.put("training_description",newDesc);
                }
                if(!newDiff.equals("-1")){
                    params.put("training_difficulty", newDiff);
                }else{
                    params.put("training_difficulty", String.valueOf(store.getTrainingInFocus().getTraining_difficulty()));
                }

                /** VALIDATION TO MAKE BEFORE REQUEST:
                1)ARE THERE ANY CHANGES MADE
                2)IS THE NAME ALREADY IN USE
                3) IS USER SURE HE WANTS TO MAKE CHANGES? **/

                /*******************Check if any data has been changed or if name is in use**********************/
                Log.i("Validation", "Validation failed, returning...");
                Log.i("newName", newName);
                Log.i("newDesc", newDesc);
                Log.i("newDiff", newDiff);
                Log.i("TrainingInFocusName", store.getTrainingInFocus().getTraining_name());
                Log.i("TrainingInFocusDesc", store.getTrainingInFocus().getTraining_desc());
                Log.i("TrainingInFocusDiff", String.valueOf(store.getTrainingInFocus().getTraining_difficulty()));
                Log.i("Contains name", String.valueOf(store.containsName(newName)));
                if((newName.toLowerCase(Locale.ROOT).equals(store.getTrainingInFocus().getTraining_name().toLowerCase(Locale.ROOT))
                        && newDesc.toLowerCase(Locale.ROOT).equals(store.getTrainingInFocus().getTraining_desc().toLowerCase(Locale.ROOT))
                        && newDiff.equals(String.valueOf(store.getTrainingInFocus().getTraining_difficulty())))
                        ||
                   store.containsName(newName)
                        ||
                        (newDesc.equals("") && newName.equals("")))
                {
                    /*********Notify user of mistake************/
                    Toast.makeText(getApplicationContext(),"No Changes were made or name already in Use",Toast.LENGTH_SHORT).show();
                    return;
                }

                /***********Validation passed*************/
                Snackbar editWarning = Snackbar.make(v,"Are you sure you want to change this?",Snackbar.LENGTH_INDEFINITE);
                /********edit logc********/
                editWarning.setAction("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        networkHelper.patchExc(clientel,params);
                        while(store.getServerResponseExercisePatched().equals("")){
                            Log.i("Waiting for response", store.getServerResponseExercisePatched());
                        }
                        if(store.getServerResponseExercisePatched().contains("!")){
                            /*******Failed to Patch*********/
                        }else{
                            /*******Patch Successful, check for empty variables again in case 1 got
                             * throgh before altering the locallist*********/
                            if(!newName.equals("")) {
                                store.getTrainingInFocus().setTraining_name(newName);
                            }
                            if(!newDesc.equals("")) {
                                store.getTrainingInFocus().setTraining_desc(newDesc);
                            }
                            if(!newDiff.equals("-1")) {
                                store.getTrainingInFocus().setTraining_difficulty(Integer.parseInt(newDiff));
                            }

                            /*************reset the adapters datalist, refresh the adapter, exit edit mode and focus on the new Training*************/
                            adapter.setDataList(store.getUserTrainings());
                            rv.setAdapter(adapter);
                            edit_browse_toggler();
                            onTrainingFocus(store.getTrainingInFocus());
                            /***********Inform user of success*************/
                            Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                /*******Show user warning*********/
                editWarning.show();
            }
        });
    }




    /***********Function to set transparency of filters onClick***********/
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
    /************Filters a list by fiddiculty****************/
    public List<Training> filterByDifficulty(List<Training> list){
        List<Training> filtered = new ArrayList<>();
        for(Training x : list){
            if(x.getTraining_difficulty()==store.getActiveDifficultyFilter()){
                filtered.add(x);
            }
        }
        return filtered;
    }
    /**************sets the list to the original list fetched by the networkHelper *******************/
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
    public void edit_browse_toggler(){
        store.setEditModeActive();

        Log.i("active?:",String.valueOf(store.isEditModeActive()));
        if(store.isEditModeActive()){
            //so the keyboard pans
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            browseView.setVisibility(View.GONE);
            nameTv.setVisibility(View.INVISIBLE);

            patchTrigger.setVisibility(View.VISIBLE);
            editDescET.setVisibility(View.VISIBLE);
            editDescET.setText(store.getTrainingInFocus().getTraining_desc());


            editNameET.setVisibility(View.VISIBLE);
            editNameET.setText(store.getTrainingInFocus().getTraining_name());

        }else{
            //so the keyboard pans
            editBut.setEnabled(true);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            editDescET.setVisibility(View.GONE);
            editNameET.setVisibility(View.GONE);
            patchTrigger.setVisibility(View.GONE);

            browseView.setVisibility(View.VISIBLE);
            nameTv.setVisibility(View.VISIBLE);


        }
    }
}


