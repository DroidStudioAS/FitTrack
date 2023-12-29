package com.aa.fittracker;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aa.fittracker.logic.JsonParser;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.network.networkHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;


public class loginFragment extends Fragment implements networkHelper.NetworkCallback {


    TextView failedTv;
    EditText usernameEt;
    EditText passwordEt;
    Button trigger;
    OkHttpClient client;

    Vibrator vibrator;

    View view;

    boolean failed = false;

    /********************BACKEND PARAMETER TITLES*********************/
    private final String USERNAME_PARAMETER = "username";
    private final String PASSWORD_PARAMETER = "password";

    private String SERVER_RESPONSE;

    public loginFragment() {
        // Required empty public constructor
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);

        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        failedTv=(TextView)view.findViewById(R.id.FailedTv);
        usernameEt=(EditText) view.findViewById(R.id.usernameEt);
        passwordEt =(EditText) view.findViewById(R.id.passwordEt);
        trigger = (Button) view.findViewById(R.id.trigger);
        client = new OkHttpClient();
        trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEt.getText().toString();
                String password = passwordEt.getText().toString();


                Map<String,String> params = new HashMap<>();
                params.put("username",username);
                params.put("password",password);

                if(username.equals("")||password.equals("")){
                    Toast.makeText(getContext(),"Missing info",Toast.LENGTH_SHORT).show();
                }else{
                    //API GET REQUEST (LOGIN)
                    try {
                        networkHelper.get(client,"http://165g123.e2.mars-hosting.com/api/login_register/login",params);
                        Log.i("response from login try", store.getServerResponseLogin());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        //if store is not set await
                       while (store.getServerResponseLogin().length()==0) {
                           Log.i("Waiting", store.getServerResponseLogin());
                       }
                    }
                    Log.i("Finally broken", store.getServerResponseLogin());
                    //check if all is ok

                    if(store.getServerResponseLogin().contains("ok") && !store.getServerResponseLogin().contains("!")){
                        store.setUSERNAME(username);
                        Intent intent =  new Intent(requireContext(),IndexActivity.class);
                        startActivity(intent);
                    }else{
                        shakeView(view);
                        vibrateDevice();
                        failedTv.setVisibility(View.VISIBLE);
                        usernameEt.setTextColor(Color.RED);
                    }
                }
                //IMPORTANT
                //Reset server response after failed attempt for next authentication
                if(store.getServerResponseLogin().contains("!") || store.getServerResponseLogin().contains("No")) {
                    store.setServerResponseLogin("");
                }
            }
        });
        return view;
    }


    @Override
    public void onSuccess(String response) {
        SERVER_RESPONSE = response;
        Log.i("SERVER SAYS", SERVER_RESPONSE);
    }

    @Override
    public void onFailure(IOException e) {

    }
    public void vibrateDevice(){
        if(vibrator!=null){
            vibrator.vibrate(500);
        }
    }
    public static void shakeView(View view) {
        // Create ObjectAnimators for translationX
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "translationX", -20);
        anim1.setDuration(100);

        ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "translationX", 20);
        anim2.setDuration(100);

        ObjectAnimator anim3 = ObjectAnimator.ofFloat(view, "translationX", -10);
        anim3.setDuration(100);

        // Create an AnimatorSet to play the animations sequentially
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(anim1, anim2, anim3);
        animatorSet.start();
    }

}