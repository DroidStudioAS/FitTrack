package com.aa.fittracker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aa.fittracker.OnInfoInputListener;
import com.aa.fittracker.R;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.Training;
import com.aa.fittracker.models.TrainingEntry;
import com.aa.fittracker.models.WeightEntry;
import com.aa.fittracker.network.networkHelper;
import com.aa.fittracker.presentation.CalendarAdapter;
//import com.aa.fittracker.trainingservice.AddTrainingActivity;
import com.aa.fittracker.trainingservice.TrainingActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;



public class InputDialog extends Dialog {
    Spinner spinner;

    EditText weightInputEt;
    EditText calorieInputEt;

    TextView dialogLabel;
    TextView restDayLabel;
    TextView plannedLabel;
    TextView unplannedLabel ;

    ImageView confirmTrigger;

    Button plannedRestSwitch;
    Button unplannedRestSwitch;

    int plannedClickCounter, unplannedClickCounter = 1;

    ImageView resfreshTrigger;

    OnInfoInputListener listener;
    OkHttpClient client;


    boolean planedRest,unplannedRest = false;
    boolean clickedMaybeLater=false;

    String REST_DAY_PLANNED = "REST DAY";
    String REST_DAY_UNPLANNED = "SKIPPED DAY";

    View.OnClickListener buttonToggle = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.equals(plannedRestSwitch)){
                if(plannedClickCounter%2==0){
                    planedRest=false;
                    plannedRestSwitch.setAlpha((float) 0.35);
                }else{
                    planedRest=true;
                    plannedRestSwitch.setAlpha(1);
                }
                unplannedRest=false;
                unplannedRestSwitch.setAlpha((float) 0.35);

                plannedClickCounter++;
            }
            if(v.equals(unplannedRestSwitch)){
                if(unplannedClickCounter%2==0){
                    unplannedRest=false;
                    unplannedRestSwitch.setAlpha((float) 0.35);
                }else{
                    unplannedRest=true;
                    unplannedRestSwitch.setAlpha(1);
                }
                planedRest=false;
                plannedRestSwitch.setAlpha((float) 0.35);

                unplannedClickCounter++;

            }
            Log.i("Planned rest: ", String.valueOf(planedRest));
            Log.i("Unplanned rest: ", String.valueOf(unplannedRest));
        }
    };


    public InputDialog(@NonNull Context context, OnInfoInputListener listener) {
        super(context);
        this.listener = (OnInfoInputListener) context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_dialog);
        /*********************Ui initializations***************************/
        resfreshTrigger=(ImageView)findViewById(R.id.refreshTrigger);

        spinner=(Spinner) findViewById(R.id.trainingSpinner);

        confirmTrigger=(ImageView) findViewById(R.id.inputTrigger);
        confirmTrigger.setVisibility(View.VISIBLE);
        plannedRestSwitch=(Button)findViewById(R.id.planedRest);
        unplannedRestSwitch=(Button)findViewById(R.id.unplanedRest);

        dialogLabel=(TextView)findViewById(R.id.inputLabel);
        restDayLabel=(TextView)findViewById(R.id.restDayLabel);
        plannedLabel  =(TextView)findViewById(R.id.plannedLabel);
        unplannedLabel=(TextView)findViewById(R.id.unplannedLabel);

        weightInputEt =(EditText)findViewById(R.id.weigthInputEt);
        calorieInputEt=(EditText)findViewById(R.id.calorieInputEt);

        if(resfreshTrigger.getVisibility()==View.VISIBLE){
            resfreshTrigger.setVisibility(View.GONE);
        }

        client=new OkHttpClient();
        /******************Populate Spinner**********************/
        List<String> stringList = new ArrayList<>();
        for(Training x : store.getUserTrainings()){
            stringList.add(x.getTraining_name());
        }
        stringList.add("FREESTYLE");
        if(stringList.size()!=0){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,stringList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        }

        /*************************Set the appropriate ET/SPINNERS*****************************/
        inputDialogUserModeReaction();

        plannedRestSwitch.setOnClickListener(buttonToggle);
        unplannedRestSwitch.setOnClickListener(buttonToggle);

        confirmTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (store.getUserMode()){
                    case "journal":
                        /**********Validation***********/
                        if(stringList.size()==0 && !planedRest && !unplannedRest){
                            if(!clickedMaybeLater) {
                                promptDialog pd = new promptDialog(getContext());
                                pd.show();
                                pd.noTrainingsPrompt(1);
                                clickedMaybeLater=true;
                                return;
                            }
                        }
                        /*****Fetch parameter******/
                        String trainingToEnter = "";
                        if(spinner.getCount()!=0) {
                            trainingToEnter = spinner.getSelectedItem().toString();
                        }
                        if(planedRest==true){
                            trainingToEnter=REST_DAY_PLANNED;
                        }else if(unplannedRest==true) {
                            trainingToEnter=REST_DAY_UNPLANNED;
                        }


                        /**************Add Entry***************/
                        networkHelper.postExcEntry(client,trainingToEnter);
                        while(store.getServerResponseAdderTrainingEntry().equals("")){
                            Log.i("waiting",store.getServerResponseAdderTrainingEntry());
                        }
                        //SUCCESS
                        if(store.getServerResponseAdderTrainingEntry().contains("ok") && !store.getServerResponseAdderTrainingEntry().contains("!")){
                            TrainingEntry toAdd = new TrainingEntry(store.getDateInFocus(),trainingToEnter);

                            store.addToTrainingEntries(toAdd);
                            resfreshTrigger.setVisibility(View.VISIBLE);

                            Toast.makeText(getContext(),"Added: " + trainingToEnter + "To: " + store.getDateInFocus(),Toast.LENGTH_SHORT).show();
                            resetLayoutPreparation(trainingToEnter);

                            resfreshTrigger.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    listener.onTrainingInput(toAdd);
                                }
                            });
                        }



                        break;

                        case "weight" :
                            String weight_value = weightInputEt.getText().toString();
                            if(weight_value.equals("")){
                                Toast.makeText(getContext(),"fail",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            networkHelper.postWeightTrackEntry(client,weight_value,"");
                            while(store.getServerResponseAdderWeightEntry().equals("")){
                                Log.i("Waiting","...");
                            }
                            Log.i("resp", store.getServerResponseAdderWeightEntry());

                            if(store.getServerResponseAdderWeightEntry().contains("ok") && !store.getServerResponseAdderWeightEntry().contains("!")){

                                WeightEntry toAdd = new WeightEntry(store.getDateInFocus(),weight_value);
                                store.addToWeightEntries(toAdd);

                                Toast.makeText(getContext()," Added: " + weight_value + " To: " + store.getDateInFocus(),Toast.LENGTH_SHORT).show();

                                resetLayoutPreparation(weight_value);

                                resfreshTrigger.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        listener.onWeightInput(toAdd);
                                    }
                                });

                            }

                        break;

                        case "cals":
                        Toast.makeText(getContext(),"Calorie Service Currently Not Supported",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });





    }
    public void inputDialogUserModeReaction(){
        switch (store.getUserMode()){
            case "journal":
                dialogLabel.setText("What Training You Do On This Date?");
                spinner.setVisibility(View.VISIBLE);
                weightInputEt.setVisibility(View.GONE);
                calorieInputEt.setVisibility(View.GONE);

                restDayLabel.setVisibility(View.VISIBLE);
                plannedLabel.setVisibility(View.VISIBLE);
                unplannedLabel.setVisibility(View.VISIBLE);
                plannedRestSwitch.setVisibility(View.VISIBLE);
                unplannedRestSwitch.setVisibility(View.VISIBLE);

                break;
            case "weight":
                dialogLabel.setText("How Much Did You Weigh On This Date?");
                spinner.setVisibility(View.INVISIBLE);
                weightInputEt.setVisibility(View.VISIBLE);
                calorieInputEt.setVisibility(View.GONE);

                restDayLabel.setVisibility(View.INVISIBLE);
                plannedLabel.setVisibility(View.INVISIBLE);
                unplannedLabel.setVisibility(View.INVISIBLE);
                plannedRestSwitch.setVisibility(View.INVISIBLE);
                unplannedRestSwitch.setVisibility(View.INVISIBLE);
                break;
            case "cals":
                dialogLabel.setText("What Did You Eat On This Date");
                spinner.setVisibility(View.INVISIBLE);
                weightInputEt.setVisibility(View.GONE);
                calorieInputEt.setVisibility(View.VISIBLE);

                restDayLabel.setVisibility(View.INVISIBLE);
                plannedLabel.setVisibility(View.INVISIBLE);
                unplannedLabel.setVisibility(View.INVISIBLE);
                plannedRestSwitch.setVisibility(View.INVISIBLE);
                unplannedRestSwitch.setVisibility(View.INVISIBLE);
                break;
        }
    }
    public void resetLayoutPreparation(String name){
        this.setCancelable(false);
        dialogLabel.setText("Added: " + name + " To: " + store.getDateInFocus() + "\n" + "Click to Refresh");
        weightInputEt.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.INVISIBLE);
        calorieInputEt.setVisibility(View.INVISIBLE);
        resfreshTrigger.setVisibility(View.VISIBLE);
        confirmTrigger.setVisibility(View.INVISIBLE);
        restDayLabel.setVisibility(View.INVISIBLE);
        plannedRestSwitch.setVisibility(View.INVISIBLE);
        unplannedRestSwitch.setVisibility(View.INVISIBLE);
        plannedLabel.setVisibility(View.INVISIBLE);
        unplannedLabel.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setOnCancelListener(@Nullable OnCancelListener listener) {
        super.setOnCancelListener(listener);
    }
}
