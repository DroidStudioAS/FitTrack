package com.aa.fittracker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;

import com.aa.fittracker.OnInfoInputListener;
import com.aa.fittracker.R;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.TrainingEntry;
import com.aa.fittracker.network.networkHelper;
import com.aa.fittracker.presentation.AnimationHelper;
import com.aa.fittracker.presentation.SfxHelper;

import okhttp3.OkHttpClient;


public class FreestyleInputDialog extends Dialog {

    final String trainingName = "FREESTYLE";
    OkHttpClient client;

    ImageView closeFid;
    ImageView fiConfirmTrigger;
    ImageView refreshTrigger;
    ImageView fiLogo;

    Button fiEazyBut;
    Button fiMediumBut;
    Button fiHardBut;

    TextView succesTv;

    EditText fiTrainingDescriptionEt;

    Group input;
    Group refresh;

    OnInfoInputListener listener;

    int diff = -1;


    public FreestyleInputDialog(@NonNull Context context, OnInfoInputListener listener) {
        super(context);
        this.listener=listener;
        setContentView(R.layout.freestyle_input_layout);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        /*********Ui Initializations**********/
        closeFid=(ImageView) findViewById(R.id.closeFid);
        fiConfirmTrigger=(ImageView)findViewById(R.id.fInputTrigger);
        refreshTrigger=(ImageView)findViewById(R.id.refresh);
        fiLogo=(ImageView)findViewById(R.id.fiLogo);

        fiEazyBut=(Button)findViewById(R.id.fiEazyBut);
        fiMediumBut=(Button)findViewById(R.id.fiMidBut);
        fiHardBut=(Button)findViewById(R.id.fiHardBut);

        fiTrainingDescriptionEt=(EditText)findViewById(R.id.fiTrainingDescEt);
        succesTv=(TextView)findViewById(R.id.successTv);

        input=(Group)findViewById(R.id.inputGroup);
        refresh=(Group) findViewById(R.id.refreshGroup);

        if(input.getVisibility()==View.INVISIBLE){
            input.setVisibility(View.VISIBLE);
        }
        if(refresh.getVisibility()==View.VISIBLE){
            refresh.setVisibility(View.INVISIBLE);
        }


        client=new OkHttpClient();
        /*********End Of Ui Initializations**********/


        /*********OnClickListeners**********/

        fiLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationHelper.centerpieceClick(fiLogo);
                SfxHelper.playBloop(getContext());
            }
        });
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
                    input.setVisibility(View.INVISIBLE);
                    refresh.setVisibility(View.VISIBLE);

                    refreshTrigger.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            store.addToTrainingEntries(toAdd);
                            listener.onTrainingInput(toAdd);
                            dismiss();
                        }
                    });

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
