package com.aa.fittracker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;

import com.aa.fittracker.R;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.SharedTraining;
import com.aa.fittracker.models.Training;
import com.aa.fittracker.network.networkHelper;
import com.aa.fittracker.presentation.AnimationHelper;
import com.aa.fittracker.presentation.SfxHelper;
import com.aa.fittracker.trainingservice.TrainingActivity;
import com.aa.fittracker.trainingservice.TrainingAddedCallback;
import com.aa.fittracker.trainingservice.onItemClickListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.OkHttpClient;

public class promptDialog extends Dialog {
    TrainingAddedCallback callback;

    TextView userPromptTv;
    TextView trainingRenameTv;

    EditText trainingRenameEt;

    Button yesBut;
    Button noBut;
    Button confirmRenameBut;

    Group promptGroup;
    Group renameGroup;

    ImageView pdLogo;

    int status;


    public promptDialog(@NonNull Context context) {
        super(context);
    }

    public promptDialog(@NonNull Context context, TrainingAddedCallback callback) {
        super(context);
        this.callback=(TrainingAddedCallback) callback;
    }
    public void setTrainingCallback(TrainingAddedCallback listener) {
        this.callback = listener;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prompt_dialog);

        userPromptTv=(TextView) findViewById(R.id.userPromptTv);
        trainingRenameTv=(TextView) findViewById(R.id.nameTakenTv);

        trainingRenameEt=(EditText)findViewById(R.id.trainingRenameEt);

        pdLogo=(ImageView)findViewById(R.id.pdLogo);

        yesBut=(Button) findViewById(R.id.yesBut);
        noBut=(Button) findViewById(R.id.noBut);
        confirmRenameBut=(Button) findViewById(R.id.confirmRename);

        renameGroup=(Group) findViewById(R.id.trainingRenameGroup);


        if(renameGroup.getVisibility()==View.VISIBLE){
            renameGroup.setVisibility(View.GONE);
           yesBut.setVisibility(View.VISIBLE);
           noBut.setVisibility(View.VISIBLE);
           userPromptTv.setVisibility(View.VISIBLE);
        }

        pdLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationHelper.centerpieceClick(pdLogo);
                SfxHelper.playBloop(getContext());
            }
        });






    }

    //0- called from the TrainingActivity
    //1- called from elsewhere
    public void noTrainingsPrompt(int status){
        userPromptTv.setText(R.string.no_training_prompt);
        yesBut.setText("Lets Do It!");
        yesBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status==0) {
                    TrainingActivity.setCurrentPosition(0);
                }else{
                    Intent intent = new Intent(getContext(),TrainingActivity.class);
                    intent.putExtra("status","1");
                    getContext().startActivity(intent);
                }
                dismiss();
            }
        });
        noBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    public void trainingAddedPrompt(){
        userPromptTv.setText("Success. Check Trainings?");
        yesBut.setText("Lets Do It!");
        store.refreshUserTrainings();
        yesBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrainingActivity.setCurrentPosition(1);
                callback.onTrainingInput();
                dismiss();
            }
        });
        noBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    public void noDateInFoucsPrompt(){
        noBut.setVisibility(View.GONE);
        userPromptTv.setText("You Need To Have A Date Selected To See More Data!");
        yesBut.setText("Ok!");
        yesBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    public void cantDeleteTrainingPrompt(){
        userPromptTv.setText("You Can Not Delete A Training You Have Logged In Your Calendar! " +
                                "Please Delete The Training From Your Log First! It A");
        noBut.setVisibility(View.GONE);
        yesBut.setText("Ok!");
        yesBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
    public void shareTrainingPrompt(Training toAdd, OkHttpClient client){
        userPromptTv.setText("Are You Sure You Want To Add: " + toAdd.getTraining_name() + " To Community Trainings? This Means Other Users Will Be Able To View And Download Your Training.");
        yesBut.setText("Lets Do It!");
        noBut.setText("Maybe Later");
        noBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        yesBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkHelper.addToSharedTrainings(client,toAdd);
                while (store.getServerResponseAddedSharedTraining().equals("")){
                    Log.i("Waiting", "...");
                }
                if(store.getServerResponseAddedSharedTraining().contains("ok") && !store.getServerResponseAddedSharedTraining().contains("!")){
                    //sucess
                    userPromptTv.setText("Thanks For Sharing " + toAdd.getTraining_name()+  " With The FitTracker Community");
                    store.setServerResponseAddedSharedTraining("");
                    noBut.setVisibility(View.GONE);
                    yesBut.setText("Ok!");
                    store.addToSharedTrainings(new SharedTraining(toAdd.getTraining_difficulty(),toAdd.getTraining_name(),toAdd.getTraining_desc()));
                    yesBut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                        }
                    });
                }else if(store.getServerResponseAddedSharedTraining().contains("name is taken")){
                    userPromptTv.setText("Oops.. This Name Is Already Taken. Please Rename Your Training To A Unique Name!");
                    noBut.setVisibility(View.GONE);
                    yesBut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            store.setServerResponseAddedSharedTraining("");
                            dismiss();
                        }
                    });
                }
            }
        });
    }
    public void downloadTrainingPrompt(OkHttpClient client, Training training){
        userPromptTv.setText("Are You Sure You Want To Download: \"" + training.getTraining_name() + "\" To Your Trainings?");
        noBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        yesBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /******Name Validation******/
                if(isNameTaken(training.getTraining_name())){
                    //name taken
                    renameGroup.setVisibility(View.VISIBLE);
                    yesBut.setVisibility(View.GONE);
                    noBut.setVisibility(View.GONE);
                    userPromptTv.setVisibility(View.GONE);

                    trainingRenameTv.setText("You Already Have A Training With This Name! Please Select Another One");
                    setCancelable(true);
                    confirmRenameBut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(isNameTaken(trainingRenameEt.getText().toString())){
                                trainingRenameTv.setText("You Already Have A Training With This Name! Please Select Another One");
                                return;
                            }
                            /******Name Validation End******/
                            else{
                                //Name Is Ok
                                String newName = trainingRenameEt.getText().toString().toUpperCase(Locale.ROOT);
                                Map<String,String> params = new HashMap<>();
                                params.put("username",store.getUSERNAME());
                                params.put("diff",String.valueOf(training.getTraining_difficulty()));
                                params.put("name",newName);
                                params.put("desc",training.getTraining_desc());
                                /*********Network block*********/
                                try {
                                    networkHelper.postExc(client,"http://165g123.e2.mars-hosting.com/api/exc/addExercise",params);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                while(store.getServerResponseTrainingAdded().equals("")){
                                    Log.i("Waiting...", "...");
                                }
                                if(store.getServerResponseTrainingAdded().contains("ok") && !store.getServerResponseTrainingAdded().contains("!")){
                                    training.setTraining_name(newName);
                                    renameGroup.setVisibility(View.GONE);
                                    promptGroup.setVisibility(View.VISIBLE);
                                    noBut.setVisibility(View.GONE);
                                    userPromptTv.setText("Added :\"" + newName + "\" To You Trainings");
                                    store.addToUserTrainings(training);
                                    store.setServerResponseTrainingAdded("");
                                    yesBut.setText("Ok!");
                                    yesBut.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dismiss();
                                        }
                                    });
                                }
                            }
                        }
                    });
                }else{
                    //Name Is Ok
                    Map<String,String> params = new HashMap<>();
                    params.put("username",store.getUSERNAME());
                    params.put("diff",String.valueOf(training.getTraining_difficulty()));
                    params.put("name",training.getTraining_name());
                    params.put("desc",training.getTraining_desc());
                    /*********Network block*********/
                    try {
                        networkHelper.postExc(client,"http://165g123.e2.mars-hosting.com/api/exc/addExercise",params);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    while(store.getServerResponseTrainingAdded().equals("")){
                        Log.i("Waiting...", "...");
                    }
                    if(store.getServerResponseTrainingAdded().contains("ok") && !store.getServerResponseTrainingAdded().contains("!")){
                        userPromptTv.setText("Added :\"" + training.getTraining_name() + "\" To You Trainings");
                        store.addToUserTrainings(training);
                        store.setServerResponseTrainingAdded("");
                        noBut.setVisibility(View.GONE);
                        yesBut.setText("Ok!");
                        yesBut.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismiss();
                            }
                        });
                    }

                }
            }
        });
    }
    public boolean isNameTaken(String name){
        String upperCaseName = name.toUpperCase(Locale.ROOT).trim();
        for(Training x : store.getUserTrainings()){
            if(x.getTraining_name().toUpperCase(Locale.ROOT).trim().equals(upperCaseName)){
                return true;
            }
        }
        return false;
    }
}
