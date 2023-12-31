package com.aa.fittracker.trainingservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.aa.fittracker.R;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.Training;
import com.aa.fittracker.network.networkHelper;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.OkHttpClient;

public class AddTrainingActivity extends Activity {

    OkHttpClient client;

    EditText nameET;
    EditText descET;

    Button trigger;

    Button easyBut;
    Button mediumBut;
    Button hardBut;
    int difficulty;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_training);

        client= new OkHttpClient();
        difficulty=-1; //SAFETY VALUE TO ENSURE THAT BUTTON WAS PRESSED;

        nameET=(EditText) findViewById(R.id.trainingNameET);
        descET=(EditText) findViewById(R.id.descriptionET);

        easyBut=(Button)findViewById(R.id.easyBut);
        mediumBut=(Button)findViewById(R.id.mediumBut);
        hardBut=(Button)findViewById(R.id.hardBut);

        Button[] diffButs = new Button[]{easyBut,mediumBut,hardBut};
        easyBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difficulty=1;
                difficultyChangeReaction(difficulty);
            }
        });
        mediumBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difficulty=2;
                difficultyChangeReaction(difficulty);

            }
        });
        hardBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difficulty=3;
                difficultyChangeReaction(difficulty);

            }
        });
        



        trigger=(Button) findViewById(R.id.submitButton);

        trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trainingName = nameET.getText().toString().trim().toUpperCase(Locale.ROOT);
                String trainingDesc = descET.getText().toString().trim().toUpperCase(Locale.ROOT);

                //Missing info check
                if(trainingName.equals("") || trainingDesc.equals("") || difficulty==-1){
                    Toast.makeText(AddTrainingActivity.this, "Please fill out all the info", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!store.getUserTrainings().isEmpty()){
                    List<Training> list = store.getUserTrainings();
                    for(Training x : list){
                        if(x.getTraining_name().equals(trainingName)){
                            //NAME MUST BE UNIQUE
                            Toast.makeText(AddTrainingActivity.this,"Name already exists",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                Training trainingToAdd = new Training();
                trainingToAdd.setTraining_difficulty(difficulty);
                trainingToAdd.setTraining_name(trainingName);
                trainingToAdd.setTraining_desc(trainingDesc);
                //Add training
                store.addToUserTrainings(trainingToAdd);
                //tell user of succes
                Map<String,String> params = new HashMap<>();
                params.put("username",store.getUSERNAME());
                params.put("diff",String.valueOf(trainingToAdd.getTraining_difficulty()));
                params.put("name",trainingName);
                params.put("desc",trainingDesc);

                try {
                    networkHelper.postExc(client,"http://165g123.e2.mars-hosting.com/api/exc/addExercise",params);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Snackbar snackbar = Snackbar.make(v,"Success. Check Trainings?",Snackbar.LENGTH_LONG);
                snackbar.setAction("Go", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),BrowseTrainingsActivity.class);
                        startActivity(intent);
                    }
                });

                snackbar.show();

                Log.i("desc", trainingDesc.trim());
            }
        });
    }

    public void difficultyChangeReaction(int difficulty){
        int index = difficulty-1;
        switch (index){
            case 0:
                easyBut.setAlpha(1);
                mediumBut.setAlpha((float) 0.4);
                hardBut.setAlpha((float) 0.4);
                break;
            case 1:
                easyBut.setAlpha((float) 0.4);
                mediumBut.setAlpha(1);
                hardBut.setAlpha((float) 0.4);   ;
                break;
            case 2:
                easyBut.setAlpha((float) 0.4);
                mediumBut.setAlpha((float) 0.4);
                hardBut.setAlpha(1);
                break;
        }
    }
}
