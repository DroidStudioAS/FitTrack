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

import com.aa.fittracker.logic.store;
import com.aa.fittracker.network.networkHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;


public class RegisterFragment extends Fragment {
    EditText UsernameET;
    EditText PasswordET;
    EditText KgET;

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
        trigger = view.findViewById(R.id.registerTrigger);

        client=new OkHttpClient();
        trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get values
                String username = UsernameET.getText().toString();
                String pass = PasswordET.getText().toString();
                String Kg = KgET.getText().toString();
                //FRONT END CHECK TO NOT SEND EMPTY PARAMETERS
                if(username.equals("")||pass.equals("")||Kg.equals("")){
                    //MISSING REGISTRATION INFO
                    Toast.makeText(getContext(),"Missing info",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Double.parseDouble(Kg)<=30){
                    Toast.makeText(getContext(),"U must be 18 to use app", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String,String> params = new HashMap<>();
                params.put("username",username);
                params.put("password",pass);
                params.put("kg",Kg);
                for(Map.Entry<String,String> x : params.entrySet()){
                    Log.i("DATA", x.getKey() + " " + x.getValue());
                }
                try {
                    networkHelper.post(client,"http://165g123.e2.mars-hosting.com/api/login_register/register",params);
                    Log.i("store server resp" , store.getSERVER_RESPONSE());
                }catch (IOException exc){
                    exc.printStackTrace();
                }
            }
        });


        return view;

    }
}