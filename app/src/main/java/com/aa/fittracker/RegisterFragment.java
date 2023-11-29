package com.aa.fittracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.WeightEntry;
import com.aa.fittracker.network.networkHelper;
import com.aa.fittracker.presentation.CalendarAdapter;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;


public class RegisterFragment extends Fragment {
    EditText UsernameET;
    EditText PasswordET;
    EditText KgET;
    EditText currentKgEt;

    Button trigger;

    OkHttpClient client;



    public RegisterFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        //UI INITIALIZATIONS
        UsernameET=view.findViewById(R.id.UsernameET);
        PasswordET = view.findViewById(R.id.PasswordET);
        KgET=view.findViewById(R.id.KgET);
        currentKgEt=view.findViewById(R.id.kgET2);

        trigger = view.findViewById(R.id.registerTrigger);


        client=new OkHttpClient();

        Calendar calendar = Calendar.getInstance();
        String correctedMonth = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String date = calendar.get(Calendar.YEAR) + "-" + correctedMonth + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        Log.i("date",date);

        trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get values
                String username = UsernameET.getText().toString().trim();
                String password = PasswordET.getText().toString().trim();
                String userKg = KgET.getText().toString().trim();
                String currentKg = currentKgEt.getText().toString().trim();

                if(username.equals("") || password.equals("") || userKg.equals("") || currentKg.equals("")){
                    Toast.makeText(getContext(),"Missing Necessary Fields",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length()<6){
                    Toast.makeText(getContext(),"Password Must Be At Least Then 6 Characters",Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String,String> params = new HashMap<>();
                params.put("user_kg",userKg);
                params.put("user_username",username);
                params.put("user_start_weight",currentKg);
                params.put("password",password);
                networkHelper.registerUser(client,params);

                while (store.getServerResponseRegister().equals("")){
                    Log.i("Sending Data", "...");
                }
                if(store.getServerResponseRegister().contains("!ok")){
                    //Fail
                    Toast.makeText(getContext(),"500",Toast.LENGTH_SHORT);
                    return;
                }
                if(store.getServerResponseRegister().contains("ok") && !store.getServerResponseRegister().contains("!")){
                    store.setUSERNAME(username);
                    store.setUserWeightKg(userKg);
                    store.setUserStartWeight(currentKg);
                    store.setCurrentUserWeight(currentKg);


                    networkHelper.postWeightTrackEntry(client,store.getUserStartWeight(),date);
                    store.addToWeightEntries(new WeightEntry(date,store.getCurrentUserWeight()));

                    CalendarAdapter.listReturner();

                    startActivity(new Intent(getActivity(),IndexActivity.class).putExtra("new_user",true));
                }


            }
        });


        return view;

    }
}