package com.aa.fittracker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.aa.fittracker.OnInfoInputListener;
import com.aa.fittracker.R;
import com.aa.fittracker.TrainingDeletedCallback;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.Training;
import com.aa.fittracker.network.networkHelper;
import com.aa.fittracker.trainingservice.BrowseTrainingsFragment;
import com.aa.fittracker.trainingservice.onItemClickListener;

import okhttp3.OkHttpClient;

public class DeleteTrainingDialog extends Dialog {
    Button confirmDelete;
    Button dismiss;
    OkHttpClient client;

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

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        confirmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTraining();
            }
        });

    }
    public void deleteTraining (){
        networkHelper.deleteExc(client);
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
