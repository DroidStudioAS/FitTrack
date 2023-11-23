package com.aa.fittracker.logic;

import android.util.Log;

import com.aa.fittracker.models.Training;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class store {
    //VARIABLES
    private static String SERVER_RESPONSE_LOGIN = "";
    private static String SERVER_RESPONSE_REGISTER = "";
    private static String SERVER_RESPONSE_ALL_EXC = "";

    //USER DATA
    private static String USERNAME = "";
    private static String USER_WEIGHT_KG="";
    //TRAINING SERVICE
    private static List<Training> USER_TRAININGS = new ArrayList<>();
    private static String TRAINING_IN_FOCUS_NAME =  "";
    private static Training TRAINING_IN_FOCUS = new Training();

    public static Training findInFocus(String name){
        Training toReturn = new Training();
        for(Training x : USER_TRAININGS){
            if(x.getTraining_name().equals(name)){
               toReturn=x;
            }
        }
        return toReturn;
    }
    public static String getServerResponseAllExc() {
        return SERVER_RESPONSE_ALL_EXC;
    }

    public static void setServerResponseAllExc(String serverResponseAllExc) {
        USER_TRAININGS.clear();
        SERVER_RESPONSE_ALL_EXC = serverResponseAllExc;
        String parsed = JsonParser.extractJsonArray(getServerResponseAllExc());
        Log.i("Parsed", parsed);

        Gson gson = new Gson();
        Training[] trainings = gson.fromJson(parsed,Training[].class);
        if(trainings.length!=0) {
            USER_TRAININGS.addAll(Arrays.asList(trainings));
        }
        Log.i("Final",USER_TRAININGS.toString());
        for(Training x : USER_TRAININGS){
            Log.i("Parsed Training: ", x.getTraining_name() + " desc " + x.getTraining_desc() + " Diff " + x.getTraining_difficulty());
        }
    }
    public static String getTrainingInFocusName() {
        return TRAINING_IN_FOCUS_NAME;
    }

    public static void setTrainingInFocusName(String trainingInFocusName) {
        TRAINING_IN_FOCUS_NAME = trainingInFocusName;
        //set the training by that name
    }


    public static void setUserTrainings(List<Training> userTrainings) {
        USER_TRAININGS = userTrainings;
    }

    public static Training getTrainingInFocus() {
        return TRAINING_IN_FOCUS;
    }

    public static void setTrainingInFocus(Training trainingInFocus) {
        TRAINING_IN_FOCUS = trainingInFocus;
    }

    public static List<Training> getUserTrainings() {
        return USER_TRAININGS;
    }

    public static void addToUserTrainings(Training training) {
        USER_TRAININGS.add(training);
    }

    public static String getUserWeightKg() {
        return USER_WEIGHT_KG;
    }

    public static void setUserWeightKg(String userWeightKg) {
        USER_WEIGHT_KG = userWeightKg;
    }

    public static String getServerResponseLogin() {
        return SERVER_RESPONSE_LOGIN;
    }

    public static void setServerResponseLogin(String serverResponseLogin) {
        SERVER_RESPONSE_LOGIN = serverResponseLogin;
    }

    public static String getServerResponseRegister() {
        return SERVER_RESPONSE_REGISTER;
    }

    public static void setServerResponseRegister(String serverResponseRegister) {
        SERVER_RESPONSE_REGISTER = serverResponseRegister;
    }

    public static String getUSERNAME() {
        return USERNAME;
    }

    public static void setUSERNAME(String USERNAME) {
        store.USERNAME = USERNAME;
    }
}
