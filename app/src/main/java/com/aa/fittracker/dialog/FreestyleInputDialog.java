package com.aa.fittracker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.aa.fittracker.R;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.TrainingEntry;
import com.aa.fittracker.network.networkHelper;

import okhttp3.OkHttpClient;


public class FreestyleInputDialog extends Dialog {

    final String trainingName = "FREESTYLE";
    OkHttpClient client;

    ImageView closeFid;
    ImageView fiConfirmTrigger;

    Button fiEazyBut;
    Button fiMediumBut;
    Button fiHardBut;

    EditText fiTrainingDescriptionEt;

    int diff = -1;


    public FreestyleInputDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.freestyle_input_layout);

        /*********Ui Initializations**********/
        closeFid=(ImageView) findViewById(R.id.closeFid);
        fiConfirmTrigger=(ImageView)findViewById(R.id.fInputTrigger);

        fiEazyBut=(Button)findViewById(R.id.fiEazyBut);
        fiMediumBut=(Button)findViewById(R.id.fiMidBut);
        fiHardBut=(Button)findViewById(R.id.fiHardBut);

        fiTrainingDescriptionEt=(EditText)findViewById(R.id.fiTrainingDescEt);

        client=new OkHttpClient();
        /*********End Of Ui Initializations**********/


        /*********OnClickListeners**********/
        closeFid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        fiEazyBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fiDifficultyChangeReaction(0);
            }
        });
        fiMediumBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fiDifficultyChangeReaction(1);
            }
        });
        fiHardBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fiDifficultyChangeReaction(2);
            }
        });
        fiConfirmTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(diff==-1 || fiTrainingDescriptionEt.getText().toString().equals("")){
                    Toast.makeText(getContext(), "You Must Enter A Difficulty And Description", Toast.LENGTH_SHORT).show();
                    return;
                }
                String trainingDescription = fiTrainingDescriptionEt.getText().toString();
                String difficulty = String.valueOf(diff);
                Log.i("desc and diff" , store.getUSERNAME()+" "+ trainingDescription  + " " + difficulty + " " + store.getDateInFocus());

                TrainingEntry toAdd = new TrainingEntry(store.getDateInFocus(),trainingName,difficulty,trainingDescription);

                networkHelper.postFreestyleEntry(client,toAdd);
                while (store.getServerResponseAddedFreestyleTrainingEntry().equals("")){
                    Log.i("Waiting for response","...");
                }
                if(store.getServerResponseAddedFreestyleTrainingEntry().contains("!") && store.getServerResponseAddedFreestyleTrainingEntry().contains("ok")){
                    Toast.makeText(getContext(),"fail",Toast.LENGTH_SHORT).show();
                }else if(store.getServerResponseAddedFreestyleTrainingEntry().contains("ok") && !store.getServerResponseAddedFreestyleTrainingEntry().contains("!")){
                    Toast.makeText(getContext(),"added freestyle entry", Toast.LENGTH_SHORT).show();
                    dismiss();
                }

            }
        });


    }
    public void fiDifficultyChangeReaction(int index){
        switch (index){
            case 0:
                fiEazyBut.setAlpha(1);
                fiMediumBut.setAlpha((float) 0.4);
                fiHardBut.setAlpha((float) 0.4);
                diff=1;
                break;
            case 1:
                fiEazyBut.setAlpha((float) 0.4);
                fiMediumBut.setAlpha(1);
                fiHardBut.setAlpha((float) 0.4);
                diff=2;
                break;
            case 2:
                fiEazyBut.setAlpha((float) 0.4);
                fiMediumBut.setAlpha((float) 0.4);
                fiHardBut.setAlpha(1);
                diff=3;
                break;
        }
        Log.i("diff from fid", String.valueOf(diff));
    }
}
