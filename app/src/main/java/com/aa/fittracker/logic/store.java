package com.aa.fittracker.logic;

import android.util.Log;

import com.aa.fittracker.models.Training;
import com.aa.fittracker.models.TrainingEntry;
import com.aa.fittracker.models.WeightEntry;
import com.applandeo.materialcalendarview.EventDay;
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
    private static String SERVER_RESPONSE_ADDED_FREESTYLE_TRAINING_ENTRY = "";
    private static String SERVER_RESPONSE_ADDED_SHARED_TRAINING = "";

    public static String getServerResponseAddedSharedTraining() {
        return SERVER_RESPONSE_ADDED_SHARED_TRAINING;
    }

    public static void setServerResponseAddedSharedTraining(String serverResponseAddedSharedTraining) {
        SERVER_RESPONSE_ADDED_SHARED_TRAINING = serverResponseAddedSharedTraining;
    }

    public static String getServerResponseAddedFreestyleTrainingEntry() {
        return SERVER_RESPONSE_ADDED_FREESTYLE_TRAINING_ENTRY;
    }

    public static void setServerResponseAddedFreestyleTrainingEntry(String serverResponseAddedFreestyleTrainingEntry) {
        SERVER_RESPONSE_ADDED_FREESTYLE_TRAINING_ENTRY = serverResponseAddedFreestyleTrainingEntry;
    }

    private static String SERVER_RESPONSE_ADDER_WEIGHT_ENTRY = "";

    private static String SERVER_RESPONSE_DELETED_ENTRY="";
    private static String SERVER_RESPONSE_DELETED_WEIGHT_ENTRY = "";
    private static String SERVER_RESPONSE_DELETED_TRAINING_ENTRY = "";

    private static String SERVER_RESPONSE_WEIGHT_GOAL_PATCHED = "";





    //USER DATA
    private static String USERNAME = "";
    private static String CURRENT_USER_WEIGHT="";
    private static String USER_START_WEIGHT = "";

    public static String getUserWeightGoal() {
        return USER_WEIGHT_GOAL;
    }

    public static void setUserWeightGoal(String userWeightGoal) {
        USER_WEIGHT_GOAL = userWeightGoal;
    }

    private static String USER_WEIGHT_GOAL = "";

    public static String getUserStartWeight() {
        return USER_START_WEIGHT;
    }

    public static void setUserStartWeight(String userStartWeight) {
        USER_START_WEIGHT = userStartWeight;
    }

    public static String getCurrentUserWeight() {
        return CURRENT_USER_WEIGHT;
    }

    public static void setCurrentUserWeight(String currentUserWeight) {
        CURRENT_USER_WEIGHT = currentUserWeight;
    }

    private static String USER_WEIGHT_KG="";
    private static String DATE_IN_FOCUS = "";
    private static String USER_MODE = "";
    private static List<EventDay> USER_EVENT_DAYS_ACTIVE=new ArrayList<>();





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

    //WEIGHT SERVICE
    private static String DATE_STRINGS = "";
    private static ArrayList<String> DATES_WITH_LOGS = new ArrayList<>();
    private static ArrayList<WeightEntry> WEIGHT_ENTRIES = new ArrayList<>();





    /*******************CUSTOM FUNCTIONS***********************/
    public static void removeFromTrainingEntries(String date){
        TrainingEntry toDelete = new TrainingEntry();
        for(TrainingEntry x : TRAINING_ENTRIES){
            if(x.getTraining_date().equals(date)){
                toDelete=x;
            }
        }
        if(!toDelete.getTraining_date().equals("")|| toDelete.getTraining_date()!=null) {
            TRAINING_ENTRIES.remove(toDelete);
        }else{
            Log.i("Remove failed for: " , toDelete.toString());
        }

    }
    public static void removeFromWeightEntries(String date){
        WeightEntry toDelete = new WeightEntry();
        for(WeightEntry x : WEIGHT_ENTRIES){
            if(x.getWeight_date().equals(date)){
                toDelete=x;
                Log.i("removed", x.toString());
            }
        }
        if(!toDelete.getWeight_date().equals("") || toDelete.getWeight_date()!=null) {
            WEIGHT_ENTRIES.remove(toDelete);
        }else{
            Log.i("remove failed", toDelete.toString());
        }

    }
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

    public static void removeFromUserTrainings(Training toDelete){
        Training toRemove = new Training();
        for(Training x : USER_TRAININGS){
            if(x.getTraining_name().equals(toDelete.getTraining_name())){
                toRemove=x;
            }
        }
        USER_TRAININGS.remove(toRemove);
    }
    public static double findWeightOnCurrentDate(){
        double weight = -1;
        for(WeightEntry x : store.getWeightEntries()){
            if(x.getWeight_date().equals(store.getDateInFocus())){
                weight=Double.parseDouble(x.getWeight_value());
            }
        }
        return weight;
    }

    /*******************GETTERS***********************/

    public static String getServerResponseWeightGoalPatched() {
        return SERVER_RESPONSE_WEIGHT_GOAL_PATCHED;
    }
    public static String getServerResponseTrainingDeleted() {
        return SERVER_RESPONSE_TRAINING_DELETED;
    }
    public static String getServerResponseDeletedEntry() {
        return SERVER_RESPONSE_DELETED_ENTRY;
    }
    public static List<TrainingEntry> getTrainingEntries() {
        return TRAINING_ENTRIES;
    }

    public static String getTrainingEntriesString() {
        return TRAINING_ENTRIES_STRING;
    }
    public static String getServerResponseDeletedWeightEntry() {
        return SERVER_RESPONSE_DELETED_WEIGHT_ENTRY;
    }

    public static String getServerResponseDeletedTrainingEntry() {
        return SERVER_RESPONSE_DELETED_TRAINING_ENTRY;
    }

    public static String getServerResponseExercisePatched() {
        return SERVER_RESPONSE_EXERCISE_PATCHED;
    }
    public static List<EventDay> getUserEventDaysActive() {
        return USER_EVENT_DAYS_ACTIVE;
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
    public static String getServerResponseAdderWeightEntry() {
        return SERVER_RESPONSE_ADDER_WEIGHT_ENTRY;
    }
    public static String getUserMode() {
        return USER_MODE;
    }
    public static String getDateInFocus() {
        return DATE_IN_FOCUS;
    }





    /*******************SETTERS***********************/
    public static void setServerResponseWeightGoalPatched(String serverResponseWeightGoalPatched) {
        SERVER_RESPONSE_WEIGHT_GOAL_PATCHED = serverResponseWeightGoalPatched;
    }
    public static void setServerResponseTrainingDeleted(String serverResponseTrainingDeleted) {
        SERVER_RESPONSE_TRAINING_DELETED = serverResponseTrainingDeleted;
    }

    public static void setServerResponseDeletedEntry(String serverResponseDeletedEntry) {
        SERVER_RESPONSE_DELETED_ENTRY = serverResponseDeletedEntry;
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
        if(trainings!=null) {
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
    public static void setServerResponseAdderWeightEntry(String serverResponseAdderWeightEntry) {
        SERVER_RESPONSE_ADDER_WEIGHT_ENTRY = serverResponseAdderWeightEntry;
    }
    public static void setUserMode(String userMode) {
        USER_MODE = userMode;
    }
    public static void setDateInFocus(String dateInFocus) {
        DATE_IN_FOCUS = dateInFocus;
    }
    public static void setTrainingEntries(List<TrainingEntry> trainingEntries) {
        TRAINING_ENTRIES = trainingEntries;
    }
    public static void setUserEventDaysActive(List<EventDay> userEventDaysActive) {
        USER_EVENT_DAYS_ACTIVE = userEventDaysActive;
    }
    public static void setTrainingEntries(String trainingEntries) {
        TRAINING_ENTRIES_STRING = trainingEntries;
        Gson gson = new Gson();
        TrainingEntry[] trainingEntries1 = gson.fromJson(JsonParser.extractJsonArray(store.getTrainingEntriesString()),TrainingEntry[].class);
        if(trainingEntries1!=null) {
            for (TrainingEntry x : trainingEntries1) {
                TRAINING_ENTRIES.add(x);
            }
        }
    }
    public static void setServerResponseDeletedWeightEntry(String serverResponseDeletedWeightEntry) {
        SERVER_RESPONSE_DELETED_WEIGHT_ENTRY = serverResponseDeletedWeightEntry;
    }

    public static void setServerResponseDeletedTrainingEntry(String serverResponseDeletedTrainingEntry) {
        SERVER_RESPONSE_DELETED_TRAINING_ENTRY = serverResponseDeletedTrainingEntry;
    }

}
