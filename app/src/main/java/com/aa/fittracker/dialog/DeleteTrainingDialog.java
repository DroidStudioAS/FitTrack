package com.aa.fittracker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aa.fittracker.OnInfoInputListener;
import com.aa.fittracker.R;
import com.aa.fittracker.TrainingDeletedCallback;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.Training;
import com.aa.fittracker.models.TrainingEntry;
import com.aa.fittracker.network.networkHelper;
import com.aa.fittracker.presentation.AnimationHelper;
import com.aa.fittracker.presentation.SfxHelper;
import com.aa.fittracker.trainingservice.BrowseTrainingsFragment;
import com.aa.fittracker.trainingservice.onItemClickListener;

import okhttp3.OkHttpClient;

public class DeleteTrainingDialog extends Dialog {
    Button confirmDelete;
    Button dismiss;
    OkHttpClient client;

    ImageView dtLogo;
    TextView deletePrompt;

    BrowseTrainingsFragment fragment;

    private DeleteTrainingCallback deleteTrainingCallback;


    public interface DeleteTrainingCallback {
        void onDeleteTraining();
    }
    public void setDeleteTrainingCallback(DeleteTrainingCallback callback){
        this.deleteTrainingCallback=callback;
    }


    public DeleteTrainingDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delete_training);



        client = new OkHttpClient();

        confirmDelete=(Button) findViewById(R.id.confirmDeleteBut);
        dismiss=(Button) findViewById(R.id.cancelBut);

        dtLogo=(ImageView)findViewById(R.id.dtLogo);
        deletePrompt = (TextView)findViewById(R.id.textView16);


        dtLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationHelper.centerpieceClick(dtLogo);
                SfxHelper.playBloop(getContext());
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePrompt.setText("Are You Sure You Want To Delete This Training?");
                dismiss();
            }
        });
        confirmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store.setServerResponseTrainingDeleted("");
                //check if training is in logs
                int trainingOccurrenceCount = 0;
                for(TrainingEntry x : store.getTrainingEntries()) {
                    if (x.getTraining_name().equals(store.getTrainingToDeleteName())) {
                        trainingOccurrenceCount += 1;
                        Log.i("matches found:", String.valueOf(trainingOccurrenceCount));
                    }
                }
                    if(trainingOccurrenceCount!=0) {
                        deletePrompt.setText("Deleting This Training Will Delete " + trainingOccurrenceCount + " Logs From Your Calendar... Are You Sure You Want To Do This?");
                        confirmDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //delete training removes the trainings from the db
                                deleteTraining();
                                //remove them from the frontend list for instant impl
                                //next time user logs, they will not be fetched from backend
                                while(store.getServerResponseTrainingDeleted().equals("")){
                                    Log.i("Waiting..","...");
                                }
                                //clear the frontend list
                                store.getTrainingEntries().clear();
                                //fetch it from the back
                               networkHelper.getExcEntries(client);
                            }
                        });
                    }else{
                        //no occurences exist
                        deleteTraining();
                    }

            }
        });

    }

    public void deleteTraining (){
        networkHelper.deleteExc(client);

        Log.i("resp", store.getServerResponseTrainingDeleted());

        Training toDelete = new Training();
        for(Training x : store.getUserTrainings()){
            if(x.getTraining_name().equals(store.getTrainingToDeleteName())){
                toDelete=x;
            }
        }

        store.removeFromUserTrainings(toDelete);

        if(deleteTrainingCallback!=null){
            deleteTrainingCallback.onDeleteTraining();
        }
        dismiss();
    }
}
