package com.aa.fittracker.logic;

import android.util.Log;

import com.aa.fittracker.models.Training;
import com.aa.fittracker.models.TrainingEntry;
import com.aa.fittracker.models.WeightEntry;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class store {
/*******************VARIABLES**********************/
    //SERVER RESPONSES
    private static String SERVER_RESPONSE_LOGIN = "";
    private static String SERVER_RESPONSE_REGISTER = "";
    private static String SERVER_RESPONSE_ALL_EXC = "";
    private static String SERVER_RESPONSE_TRAINING_DELETED = "";
    private static String SERVER_RESPONSE_EXERCISE_PATCHED = "";
    private static String SERVER_RESPONSE_ADDER_TRAINING_ENTRY = "";

    //USER DATA
    private static String USERNAME = "";
    private static String USER_WEIGHT_KG="";
    private static String DATE_IN_FOCUS = "";

    public static String getUserMode() {
        return USER_MODE;
    }

    public static void setUserMode(String userMode) {
        USER_MODE = userMode;
    }

    private static String USER_MODE = "";

    public static String getDateInFocus() {
        return DATE_IN_FOCUS;
    }

    public static void setDateInFocus(String dateInFocus) {
        DATE_IN_FOCUS = dateInFocus;
    }

    //TRAINING SERVICE
    private static List<Training> USER_TRAININGS = new ArrayList<>();
    private static List<Training> FILTERED_USER_TRAININGS = new ArrayList<>();
    private static String TRAINING_IN_FOCUS_NAME =  "";
    private static Training TRAINING_IN_FOCUS = new Training();
    private static String TRAINING_TO_DELETE_NAME ="";
    private static int ACTIVE_DIFFICULTY_FILTER=-1;
    private static boolean EDIT_MODE_ACTIVE=false;

    private static String TRAINING_ENTRIES_STRING = "";
    private static List<TrainingEntry> TRAINING_ENTRIES = new ArrayList<>();

    public static void setTrainingEntries(List<TrainingEntry> trainingEntries) {
        TRAINING_ENTRIES = trainingEntries;
    }

    public static List<TrainingEntry> getTrainingEntries() {
        return TRAINING_ENTRIES;
    }

    public static String getTrainingEntriesString() {
        return TRAINING_ENTRIES_STRING;
    }

    public static void setTrainingEntries(String trainingEntries) {
        TRAINING_ENTRIES_STRING = trainingEntries;
        Gson gson = new Gson();
        TrainingEntry[] trainingEntries1 = gson.fromJson(JsonParser.extractJsonArray(store.getTrainingEntriesString()),TrainingEntry[].class);
        for(TrainingEntry x : trainingEntries1){
          TRAINING_ENTRIES.add(x);
        }



    }

    //WEIGHT SERVICE
    private static String DATE_STRINGS = "";
    private static ArrayList<String> DATES_WITH_LOGS = new ArrayList<>();

    private static ArrayList<WeightEntry> WEIGHT_ENTRIES = new ArrayList<>();

    /*******************CUSTOM FUNCTIONS***********************/
    public static void addToTrainingEntries(TrainingEntry value){
        TRAINING_ENTRIES.add(value);
    }
    public static void addToWeightEntries(WeightEntry value){
        WEIGHT_ENTRIES.add(value);
    }

    public static void addToDatesWithLogs(String value){
        DATES_WITH_LOGS.add(value);
    }
    public static Training findInFocus(String name){
        Training toReturn = new Training();
        for(Training x : USER_TRAININGS){
            if(x.getTraining_name().equals(name)){
                toReturn=x;
            }
        }
        return toReturn;
    }
    public static boolean containsName(String name){
        for(Training x: USER_TRAININGS){
            if(x.getTraining_name().equals(name)){
                Log.i("MatchFound", x.getTraining_name() + " " + name);
                return true;
            }
        }


        return false;
    }

    public static void clearFilteredUserTrainings(){
        FILTERED_USER_TRAININGS.clear();
    }
    public static void addToUserTrainings(Training training) {
        USER_TRAININGS.add(training);
    }


    public static String getServerResponseTrainingDeleted() {
        return SERVER_RESPONSE_TRAINING_DELETED;
    }
    public static void removeFromUserTrainings(Training toDelete){
        Training toRemove = new Training();
        for(Training x : USER_TRAININGS){
            if(x.getTraining_name().equals(toDelete.getTraining_name())){
                toRemove=x;
            }
        }
        USER_TRAININGS.remove(toRemove);
    }

    /*******************GETTERS***********************/
    public static String getServerResponseExercisePatched() {
        return SERVER_RESPONSE_EXERCISE_PATCHED;
    }
    public static ArrayList<String> getDatesWithLogs() {
        return DATES_WITH_LOGS;
    }
    public static ArrayList<WeightEntry> getWeightEntries() {
        return WEIGHT_ENTRIES;
    }
    public static int getActiveDifficultyFilter() {
        return ACTIVE_DIFFICULTY_FILTER;
    }
    public static String getTrainingToDeleteName() {
        return TRAINING_TO_DELETE_NAME;
    }
    public static String getServerResponseAllExc() {
        return SERVER_RESPONSE_ALL_EXC;
    }
    public static String getTrainingInFocusName() {
        return TRAINING_IN_FOCUS_NAME;
    }
    public static boolean isEditModeActive() {
        return EDIT_MODE_ACTIVE;
    }
    public static Training getTrainingInFocus() {
        return TRAINING_IN_FOCUS;
    }
    public static List<Training> getUserTrainings() {
        return USER_TRAININGS;
    }
    public static String getUserWeightKg() {
        return USER_WEIGHT_KG;
    }
    public static String getServerResponseRegister() {
        return SERVER_RESPONSE_REGISTER;
    }
    public static String getServerResponseLogin() {
        return SERVER_RESPONSE_LOGIN;
    }
    public static String getUSERNAME() {
        return USERNAME;
    }
    public static String getServerResponseAdderTrainingEntry() {
        return SERVER_RESPONSE_ADDER_TRAINING_ENTRY;
    }
    public static String getDateStrings() {
        return DATE_STRINGS;
    }





    /*******************SETTERS***********************/
    public static void setServerResponseTrainingDeleted(String serverResponseTrainingDeleted) {
        SERVER_RESPONSE_TRAINING_DELETED = serverResponseTrainingDeleted;
    }

    public static void setActiveDifficultyFilter(int activeDifficultyFilter) {
        ACTIVE_DIFFICULTY_FILTER = activeDifficultyFilter;
    }
    public static void setWeightEntries(ArrayList<WeightEntry> weightEntries) {
        WEIGHT_ENTRIES = weightEntries;
    }
    public static void setServerResponseAllExc(String serverResponseAllExc) {
        //clear the list so it does not concat
        USER_TRAININGS.clear();

        SERVER_RESPONSE_ALL_EXC = serverResponseAllExc;
        //this logic happens here because it is the first place the server awnser gets to in the front end (network helper getExc()).
        //Parse the server message so it is an array not an object (Gson wouldnt be able to map it otherwise)
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

    public static void setTrainingToDeleteName(String trainingToDeleteName) {
        TRAINING_TO_DELETE_NAME = trainingToDeleteName;
    }

    public static void setTrainingInFocusName(String trainingInFocusName) {
        TRAINING_IN_FOCUS_NAME = trainingInFocusName;
        //set the training by that name
    }

    public static void setUserTrainings(List<Training> userTrainings) {
        USER_TRAININGS = userTrainings;
    }
    public static void setTrainingInFocus(Training trainingInFocus) {
        TRAINING_IN_FOCUS = trainingInFocus;
    }
    public static void setUserWeightKg(String userWeightKg) {
        USER_WEIGHT_KG = userWeightKg;
    }
    public static void setServerResponseLogin(String serverResponseLogin) {
        SERVER_RESPONSE_LOGIN = serverResponseLogin;
    }

    public static void setServerResponseRegister(String serverResponseRegister) {
        SERVER_RESPONSE_REGISTER = serverResponseRegister;
    }

    public static void setUSERNAME(String USERNAME) {
        store.USERNAME = USERNAME;
    }

    public static void setEditModeActive() {
        EDIT_MODE_ACTIVE = !EDIT_MODE_ACTIVE;
    }


    public static void setServerResponseExercisePatched(String serverResponseExercisePatched) {
        SERVER_RESPONSE_EXERCISE_PATCHED = serverResponseExercisePatched;
    }
    public static void setDatesWithLogs(ArrayList<String> datesWithLogs) {
        DATES_WITH_LOGS = datesWithLogs;
    }


    public static void setDateStrings(String date){
        DATE_STRINGS=date;
        Log.i("DATE ENTRIES", DATE_STRINGS);
    }

    public static void setServerResponseAdderTrainingEntry(String serverResponseAdderTrainingEntry) {
        SERVER_RESPONSE_ADDER_TRAINING_ENTRY = serverResponseAdderTrainingEntry;
    }

}
