package com.aa.fittracker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aa.fittracker.R;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.Training;
import com.aa.fittracker.models.TrainingEntry;
import com.aa.fittracker.models.WeightEntry;
import com.aa.fittracker.network.networkHelper;
import com.aa.fittracker.trainingservice.AddTrainingActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;



public class InputDialog extends Dialog {
    Spinner spinner;
    EditText weightInputEt;
    EditText calorieInputEt;

    TextView dialogLabel;

    Button confirmTrigger;

    OkHttpClient client;

    public InputDialog(@NonNull Context context) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_dialog);
        client=new OkHttpClient();

        spinner=(Spinner) findViewById(R.id.trainingSpinner);
        confirmTrigger=(Button) findViewById(R.id.inputTrigger);

        dialogLabel=(TextView)findViewById(R.id.inputLabel);

        weightInputEt =(EditText)findViewById(R.id.weigthInputEt);
        calorieInputEt=(EditText)findViewById(R.id.calorieInputEt);
        /******************Populate Spinner**********************/
        List<String> stringList = new ArrayList<>();
        for(Training x : store.getUserTrainings()){
            stringList.add(x.getTraining_name());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,stringList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        /*************************Set the appropriate ET/SPINNERS*****************************/
        inputDialogUserModeReaction();

        confirmTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (store.getUserMode()){
                    case "journal":
                        String trainingToEnter = spinner.getSelectedItem().toString();
                        /**********Validation***********/
                        if(trainingToEnter.equals("") || spinner.getCount()==0){
                           Snackbar noTrainingsSb = Snackbar.make(v,"You Currently Do Not Have Any Trainings? Want To Add Some?", Snackbar.LENGTH_INDEFINITE);
                            noTrainingsSb.setAction("Yes!", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getContext(), AddTrainingActivity.class);
                                   getContext().startActivity(intent);
                                }
                            });
                            noTrainingsSb.show();
                            return;
                        }
                        /**************Add Entry***************/
                        networkHelper.postExcEntry(client,trainingToEnter);
                        while(store.getServerResponseAdderTrainingEntry().equals("")){
                            Log.i("waiting",store.getServerResponseAdderTrainingEntry());
                        }
                        store.addToTrainingEntries(new TrainingEntry(store.getDateInFocus(),trainingToEnter));
                        for(TrainingEntry x : store.getTrainingEntries()){
                            Log.i("TE", x.getTraining_name() +" "+x.getTraining_date());
                        }
                        //SUCCESS
                        if(store.getServerResponseAdderTrainingEntry().contains("ok") && !store.getServerResponseAdderTrainingEntry().contains("!")){
                            Toast.makeText(getContext(),"Added: " + trainingToEnter + "To: " + store.getDateInFocus(),Toast.LENGTH_SHORT).show();
                        }
                        break;

                        case "weight" :
                            String weight_value = weightInputEt.getText().toString();
                            if(weight_value.equals("")){
                                Toast.makeText(getContext(),"fail",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            networkHelper.postWeightTrackEntry(client,weight_value);
                            while(store.getServerResponseAdderWeightEntry().equals("")){
                                Log.i("Waiting","...");
                            }
                            Log.i("resp", store.getServerResponseAdderWeightEntry());
                            store.addToWeightEntries(new WeightEntry(store.getDateInFocus(),weight_value));

                            if(store.getServerResponseAdderWeightEntry().contains("ok") && !store.getServerResponseAdderWeightEntry().contains("!")){
                                Toast.makeText(getContext()," Added: " + weight_value + " To: " + store.getDateInFocus(),Toast.LENGTH_SHORT).show();
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
                break;
            case "weight":
                dialogLabel.setText("How Much Did You Weigh On This Date?");
                spinner.setVisibility(View.INVISIBLE);
                weightInputEt.setVisibility(View.VISIBLE);
                calorieInputEt.setVisibility(View.GONE);
                break;
            case "cals":
                dialogLabel.setText("What Did You Eat On This Date");
                spinner.setVisibility(View.INVISIBLE);
                weightInputEt.setVisibility(View.GONE);
                calorieInputEt.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void setOnCancelListener(@Nullable OnCancelListener listener) {
        super.setOnCancelListener(listener);
    }
}
