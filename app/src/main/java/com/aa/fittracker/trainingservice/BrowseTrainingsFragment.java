package com.aa.fittracker.trainingservice;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aa.fittracker.R;
import com.aa.fittracker.TrainingDeletedCallback;
import com.aa.fittracker.dialog.DeleteTrainingDialog;
import com.aa.fittracker.dialog.promptDialog;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.SharedTraining;
import com.aa.fittracker.models.Training;
import com.aa.fittracker.network.networkHelper;
import com.aa.fittracker.presentation.trainingAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;

import okhttp3.OkHttpClient;

public class BrowseTrainingsFragment extends Fragment implements onItemClickListener, DeleteTrainingDialog.DeleteTrainingCallback, TrainingAddedCallback {
    trainingAdapter adapter;
    RecyclerView rv;

    ConstraintLayout browseRoot;

    TextView nameTv;
    TextView descTv;

    TextView sb;


    EditText searchEt;
    EditText editDescET;
    EditText editNameET;


    Button easyFilter;
    Button mediumFilter;
    Button hardFilter;

    ImageView filter;
    ImageView reset;
    ImageView deleteBut;
    ImageView editBut;
    ImageView shareBut;
    ImageView patchTrigger;

    OkHttpClient clientel;
    List<Training> localList;

    Timer timer;
    ScrollView browseView;
    DeleteTrainingDialog dtd;


    Context context;

    public BrowseTrainingsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_browse_trainings, container, false);
        TrainingActivity ta = (TrainingActivity)getActivity();
        /******UI Ref's******/
        Intent incoming= getActivity().getIntent();






        browseRoot=(ConstraintLayout)view.findViewById(R.id.browseRoot);
        searchEt = (EditText)view.findViewById(R.id.searchET);
        editDescET=(EditText)view.findViewById(R.id.editDescET);
        editNameET=(EditText)view.findViewById(R.id.editTitleET);

        filter = (ImageView) view.findViewById(R.id.filter);
        reset = (ImageView) view.findViewById(R.id.reset);
        deleteBut=(ImageView)view.findViewById(R.id.deleteTrigger);
        editBut=(ImageView)view.findViewById(R.id.editTrigger);
        easyFilter = (Button)view.findViewById(R.id.easyFilter);
        mediumFilter = (Button)view.findViewById(R.id.mediumFilter);
        hardFilter=(Button)view.findViewById(R.id.hardFilter);
        patchTrigger=(ImageView)view.findViewById(R.id.patchTrigger);
        shareBut=(ImageView)view.findViewById(R.id.shareTrigger);

        browseView=(ScrollView)view.findViewById(R.id.scrollView2);

        nameTv = (TextView) view.findViewById(R.id.showNameTv);
        descTv = (TextView) view.findViewById(R.id.descTV);

        rv = (RecyclerView) view.findViewById(R.id.trainingList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(layoutManager);
        context=getContext();

        editBut.setVisibility(View.GONE);
        shareBut.setVisibility(View.GONE);
        deleteBut.setVisibility(View.GONE);
        //Communication mechanism between fragment and dialog
        dtd=new DeleteTrainingDialog(context);
        dtd.setDeleteTrainingCallback(this);


        //fetch user exercises
        clientel = new OkHttpClient();
        localList = store.getUserTrainings();
        //CAUSES RUNTIME ERRORS
        if (localList.isEmpty()) {
                if (!incoming.hasExtra("status")) {
                    promptDialog pd = new promptDialog(ta);
                    pd.show();
                    pd.noTrainingsPrompt(0);
                }

        }
        //sort the list in alphabetical order
        if(localList!=null && !localList.isEmpty()) {
            Collections.sort(localList, Comparator.comparing(Training::getTraining_name));
        }
        /**************RV Configuration*******************/
        adapter = new trainingAdapter(localList, getContext());
        adapter.setOnItemClickListener(this);
        rv.setAdapter(adapter);
        /*******OnClickListeners********/
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
                store.setActiveDifficultyFilter(-1);
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
                    Toast.makeText(getContext(),"No training selected",Toast.LENGTH_SHORT).show();
                }else{
                    dtd.setCancelable(false);
                    dtd.show();
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

                Training toEdit = new Training();
                for(Training x : store.getUserTrainings()){
                    if(x.getTraining_name().equals(store.getTrainingToDeleteName())){
                        toEdit=x;
                    }
                }
                //make sure diff is not -1
                if(newDiff.equals("-1")){
                    newDiff=String.valueOf(store.getTrainingInFocus().getTraining_difficulty());
                }





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
             //  Log.i("Validation", "Validation failed, returning...");
             //  Log.i("Validation, newName", newName);
             //  Log.i("Validation, oldName", store.getTrainingInFocus().getTraining_name());
             //  Log.i("Validation, newDesc", newDesc);
             //  Log.i("Validation, oldDesc", store.getTrainingInFocus().getTraining_desc());
             //  Log.i("Validation, newDiff", newDiff);
             //  Log.i("Validation, oldDiff", String.valueOf(store.getTrainingInFocus().getTraining_difficulty()));
             //  Log.i("Validation, TrainingInFocusName", store.getTrainingInFocus().getTraining_name());
             //  Log.i("Validation, TrainingInFocusDesc", store.getTrainingInFocus().getTraining_desc());
             //  Log.i("Validation, TrainingInFocusDiff", String.valueOf(store.getTrainingInFocus().getTraining_difficulty()));
             //  Log.i("Validation, Contains name", String.valueOf(store.containsName(newName)));
                if(     //Check if any changes are made
                        (newName.toLowerCase(Locale.ROOT).equals(store.getTrainingInFocus().getTraining_name().toLowerCase(Locale.ROOT))
                                && newDesc.toLowerCase(Locale.ROOT).equals(store.getTrainingInFocus().getTraining_desc().toLowerCase(Locale.ROOT))
                                && newDiff.equals(String.valueOf(store.getTrainingInFocus().getTraining_difficulty())))
                                ||
                                //Check If training name is already in use by other trainings
                                store.containsName(newName) && !newName.equals(store.getTrainingInFocus().getTraining_name())
                                ||
                                //Check for empty strings
                                (newDesc.equals("") || newName.equals("")))
                {
                    /*********Notify user of mistake************/
                    Toast.makeText(getContext(),"No Changes were made or name already in Use",Toast.LENGTH_SHORT).show();
                    return;
                }


                /***********Validation passed*************/
                Snackbar editWarning = Snackbar.make(v,"Are you sure you want to change this?",Snackbar.LENGTH_INDEFINITE);
                /********edit logc********/
                Training finalToEdit = toEdit;
                String finalNewDiff = newDiff;
                String finalNewDiff1 = newDiff;
                editWarning.setAction("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.i("VALIDATION", "Condition 1 - newName: " + newName +
                                ", trainingName: " + store.getTrainingInFocus().getTraining_name() +
                                ", newDesc: " + newDesc +
                                ", trainingDesc: " + store.getTrainingInFocus().getTraining_desc() +
                                ", newDiff: " + finalNewDiff +
                                ", trainingDiff: " + store.getTrainingInFocus().getTraining_difficulty() +
                                " Result: " +
                                (newName.toLowerCase(Locale.ROOT).equals(store.getTrainingInFocus().getTraining_name().toLowerCase(Locale.ROOT))
                                        && newDesc.toLowerCase(Locale.ROOT).equals(store.getTrainingInFocus().getTraining_desc().toLowerCase(Locale.ROOT))
                                        && finalNewDiff1.equals(String.valueOf(store.getTrainingInFocus().getTraining_difficulty()))));

                        //is ok
                        Log.i("VALIDATION", "Condition 2 - newName: " + newName +
                                ", trainingName: " + store.getTrainingInFocus().getTraining_name() +
                                " Result: " +
                                (store.containsName(newName) && !newName.toLowerCase(Locale.ROOT).equals(store.getTrainingInFocus().getTraining_name().toLowerCase(Locale.ROOT))));

                        Log.i("VALIDATION", "Condition 3 - newDesc: " + newDesc +
                                ", newName: " + newName +
                                " Result: " +
                                (newDesc.equals("") || newName.equals("")));

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
                            if(!finalNewDiff.equals("-1")) {
                                store.getTrainingInFocus().setTraining_difficulty(Integer.parseInt(finalNewDiff));
                            }

                            /*************reset the adapters datalist, refresh the adapter, exit edit mode and focus on the new Training*************/
                            adapter.setDataList(store.getUserTrainings());
                            rv.setAdapter(adapter);
                            edit_browse_toggler();
                            onTrainingFocus(store.getTrainingInFocus());
                            /***********Inform user of success*************/
                            Toast.makeText(getContext(),"success",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                /*******Show user warning*********/
                editWarning.show();
            }
        });

        shareBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptDialog pd = new promptDialog(getContext());
                pd.show();
                pd.shareTrainingPrompt(store.getTrainingInFocus(), clientel);
                pd.setCancelable(false);
            }
        });



        return view;
    }
    /*********Helpers**********/
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
    public void refreshList(){
        Log.i("refresh triggered", "true");
        adapter.setDataList(store.getUserTrainings());
        store.clearFilteredUserTrainings();
        rv.setAdapter(adapter);

        deleteBut.setVisibility(View.GONE);
        editBut.setVisibility(View.GONE);
        shareBut.setVisibility(View.GONE);

        nameTv.setText("No Training Selected");
        descTv.setText("");
    }
    public void edit_browse_toggler(){
        store.setEditModeActive();

        Log.i("active?:",String.valueOf(store.isEditModeActive()));
        if(store.isEditModeActive()){
            //so the keyboard pans
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

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
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            editDescET.setVisibility(View.GONE);
            editNameET.setVisibility(View.GONE);
            patchTrigger.setVisibility(View.GONE);

            browseView.setVisibility(View.VISIBLE);
            nameTv.setVisibility(View.VISIBLE);


        }
    }

    /*********Callbacks*********/
    @Override
    public void onTrainingFocus(Training training) {
            nameTv.setText(training.getTraining_name());
            descTv.setText(training.getTraining_desc());
            deleteBut.setVisibility(View.VISIBLE);
            editBut.setVisibility(View.VISIBLE);
            shareBut.setVisibility(View.VISIBLE);
            //store the name of the training in focus as a parameter in case user wants to delete
            store.setTrainingToDeleteName(training.getTraining_name());

    }

    @Override
    public void onSharedTrainingFocus(SharedTraining st) {

    }


    @Override
    public void onDeleteTraining() {
        Log.i("callback successful", "YES");
        refreshList();
    }

    @Override
    public void onTrainingInput() {
        refreshList();
    }
}