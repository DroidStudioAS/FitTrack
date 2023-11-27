package com.aa.fittracker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aa.fittracker.R;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.Training;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;


public class InputDialog extends Dialog {
    Spinner spinner;
    EditText weightInputEt;
    EditText calorieInputEt;

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

        switch (store.getUserMode()){

        }


    }

    @Override
    public void setOnCancelListener(@Nullable OnCancelListener listener) {
        super.setOnCancelListener(listener);
    }
}
