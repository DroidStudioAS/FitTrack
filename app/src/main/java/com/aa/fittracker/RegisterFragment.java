package com.aa.fittracker;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;


public class RegisterFragment extends Fragment {
    EditText UsernameET;
    EditText PasswordET;
    EditText KgET;

    Button trigger;



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
        trigger = view.findViewById(R.id.registerTrigger);

        trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = UsernameET.getText().toString();
                String pass = PasswordET.getText().toString();
                String Kg = KgET.getText().toString();
                if(username.equals("")||pass.equals("")||Kg.equals("")){
                    //MISSING REGISTRATION INFO
                    Toast.makeText(getContext(),"Missing info",Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String,String> params = new HashMap<>();
                params.put("username",username);
                params.put("password",pass);
                params.put("kg",Kg);
                for(Map.Entry<String,String> x : params.entrySet()){
                    Log.i("DATA", x.getKey() + " " + x.getValue());
                }
            }
        });


        return view;

    }
}