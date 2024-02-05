package com.aa.fittracker.dialog;

import static com.aa.fittracker.calendarActivity.listRemover;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.aa.fittracker.OnInfoInputListener;
import com.aa.fittracker.R;
import com.aa.fittracker.calendarActivity;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.TrainingEntry;
import com.aa.fittracker.models.WeightEntry;
import com.aa.fittracker.network.networkHelper;
import com.aa.fittracker.presentation.AnimationHelper;
import com.aa.fittracker.presentation.SfxHelper;

import okhttp3.OkHttpClient;

public class DeleteDialog extends Dialog {
    TextView deleteLabel;

    Button confirmDelete;

    ImageView dLogo;
    ImageView refreshIv;



    OkHttpClient client;
    OnInfoInputListener listener;

    public DeleteDialog(@NonNull Context context) {
        super(context);
        listener=(OnInfoInputListener) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_dialog);

        client=new OkHttpClient();

        deleteLabel=(TextView) findViewById(R.id.deleteWarning);
        confirmDelete=(Button) findViewById(R.id.deleteConfirmTrigger);

        refreshIv=(ImageView) findViewById(R.id.refreshTrig);
        dLogo=(ImageView)findViewById(R.id.dLogo);

        refreshIv.setVisibility(View.INVISIBLE);


        deleteLabel.setText("Are You Sure You Want To Delete Entry For: " + store.getDateInFocus());

        dLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationHelper.centerpieceClick(dLogo);
                SfxHelper.playBloop(getContext());
            }
        });
        confirmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (store.getUserMode()){
                    case "journal":
                        networkHelper.deleteExcEntry(client);
                        /**********Result Validation***************/
                        while (store.getServerResponseDeletedTrainingEntry().equals("")){
                            Log.i("Waiting...","...");
                        }
                        if(store.getServerResponseDeletedTrainingEntry().contains("ok") &&
                                !store.getServerResponseDeletedTrainingEntry().contains("!")){
                            Toast.makeText(getContext(), "Deleted Training Entry For: " + store.getDateInFocus(),Toast.LENGTH_SHORT).show();
                            listRemover(1);//method in calenadrActivity todo replace to store
                            enableRefresh();
                        }
                        break;
                    case "weight":
                        networkHelper.deleteWeightEntry(client);
                        while (store.getServerResponseDeletedWeightEntry().equals("")){
                            Log.i("Waiting...","...");
                        }
                        if(store.getServerResponseDeletedWeightEntry().contains("ok") &&
                                !store.getServerResponseDeletedTrainingEntry().contains("!")){
                            Toast.makeText(getContext(), "Deleted Weight Entry For: " + store.getDateInFocus(),Toast.LENGTH_SHORT).show();
                            listRemover(2);//method in calenadrActivity todo replace to store
                            enableRefresh();
                        }
                        /**************clear the button so no malicous deletes happed*******************/
                }
            }
        });
    }
    public void enableRefresh(){
        this.setCancelable(false);
        deleteLabel.setText("Entry Deleted."+ "\n"+  "Refresh");
        refreshIv.setVisibility(View.VISIBLE);
        confirmDelete.setVisibility(View.INVISIBLE);
        refreshIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleted();
            }
        });

    }
}
