package com.aa.fittracker.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import com.aa.fittracker.R;
import com.aa.fittracker.logic.JsonParser;
import com.aa.fittracker.models.TrainingEntry;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.Normalizer;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.Training;
import com.google.gson.Gson;

public class networkHelper {

    /******NetworkUtils*******/
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }

        return false;
    }


    /**************Training Service***************/
    public static void getExcEntries(OkHttpClient client){
        HttpUrl.Builder builder = HttpUrl.parse("http://165g123.e2.mars-hosting.com/api/training_service/get_log").newBuilder();
        builder.addQueryParameter("username",store.getUSERNAME());
        String url = builder.build().toString();

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                store.setTrainingEntries(response.body().string());
                Log.i("exclog from nh: ",store.getTrainingEntriesString());
            }
        });


    }

    public static void getExc(OkHttpClient client, String url, Map<String,String> params) throws IOException{
        //initialize the urlBuilder
        HttpUrl.Builder urlBuilder =  HttpUrl.parse(url).newBuilder();
        //add the parameters to the url
        for(Map.Entry<String,String> entry : params.entrySet()){
            urlBuilder.addQueryParameter(entry.getKey(),entry.getValue());
        }
        //build the url
        String URL = urlBuilder.build().toString();
        //form the request
        Request request = new Request.Builder().url(URL).build();
        //Callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //in case of network error
                Log.i("IMPORTANT", "500");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //success
                store.setServerResponseAllExc(response.body().string());
                Log.i("response from nh",store.getServerResponseAllExc());

            }
        });


    }

    public static void postExc(OkHttpClient client, String url, Map<String, String> params) throws IOException {
        // Create a FormBody.Builder to build the request body
        FormBody.Builder formBuilder = new FormBody.Builder();
        // Add parameters to the request body
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formBuilder.add(entry.getKey(), entry.getValue());
        }
        // Build the request body
        RequestBody requestBody = formBuilder.build();
        // Build the request with the POST method and request body
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        // Enqueue the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i("IMPORTANT", "Failed to make POST request: " + e.getMessage());
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                   store.setServerResponseTrainingAdded(response.body().string());
                   Log.i("training added: " , store.getServerResponseTrainingAdded());
                } else {
                    Log.i("IMPORTANT", "POST request failed with response code: " + response.code());
                }
            }
        });
    }

    public static void deleteExc(OkHttpClient client){
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("username",store.getUSERNAME());
        builder.add("training_name",store.getTrainingToDeleteName());

        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url("http://165g123.e2.mars-hosting.com/api/userinfo/deleteUserTraining")
                .delete(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                store.setServerResponseTrainingDeleted(response.body().string());
                Log.i("response from nh", store.getServerResponseTrainingDeleted());
            }
        });

    }

    public static void patchExc(OkHttpClient client, Map<String,String> params){
        //Build the request Body Data
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        //Build the actual body
        RequestBody body = builder.build();
        //atqach the body to a new request
        Request request = new Request.Builder()
                .url("http://165g123.e2.mars-hosting.com/api/userinfo/changeExercise")
                .patch(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                store.setServerResponseExercisePatched(response.body().string());
                Log.i("RESPONSE FROM NH: ", store.getServerResponseExercisePatched());
            }
        });
    }

    //tracking subservice
    public static void postFreestyleEntry(OkHttpClient client, TrainingEntry entry){
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("username",store.getUSERNAME());
        builder.add("training_name",entry.getTraining_name());
        builder.add("training_desc",entry.getTraining_description());
        builder.add("training_diff",entry.getDiff());
        builder.add("date",store.getDateInFocus());

        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url("http://165g123.e2.mars-hosting.com/api/training_service/add_freestyle_entry")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                store.setServerResponseAddedFreestyleTrainingEntry(response.body().string());
                Log.i("response from server: ", store.getServerResponseAddedFreestyleTrainingEntry());
            }
        });
    }

    public static void postExcEntry(OkHttpClient client, String training_name){
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("username",store.getUSERNAME());
        builder.add("training_name",training_name);
        builder.add("date",store.getDateInFocus());

        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url("http://165g123.e2.mars-hosting.com/api/training_service/add_entry")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                store.setServerResponseAdderTrainingEntry(response.body().string());
                Log.i("response from nh", store.getServerResponseAdderTrainingEntry());
            }
        });
    }

    public static void deleteExcEntry(OkHttpClient client){
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("username",store.getUSERNAME());
        formBuilder.add("date", store.getDateInFocus());
        RequestBody body = formBuilder.build();

        Request request = new Request.Builder()
                .url("http://165g123.e2.mars-hosting.com/api/training_service/delete_entry")
                .delete(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                store.setServerResponseDeletedTrainingEntry(response.body().string());
                Log.i("response from nh", store.getServerResponseDeletedEntry());

            }
        });
    }
    //shared trainings
    public static void addToSharedTrainings(OkHttpClient client, Training toAdd){
        FormBody.Builder formBuilder = new FormBody.Builder();
        //add parameters
        formBuilder.add("username",store.getUSERNAME());
        formBuilder.add("training_name",toAdd.getTraining_name());
        formBuilder.add("training_desc",toAdd.getTraining_desc());
        formBuilder.add("training_diff",String.valueOf(toAdd.getTraining_difficulty()));

        RequestBody body = formBuilder.build();

        Request postRequest = new Request.Builder()
                .url("http://165g123.e2.mars-hosting.com/api/training_service/addToSharedTrainings")
                .post(body)
                .build();
        client.newCall(postRequest).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                store.setServerResponseAddedSharedTraining(response.body().string());
                Log.i("added shared tr: ", store.getServerResponseAddedSharedTraining());
            }
        });

    }

    public static void getSharedTrainings(OkHttpClient client) {
        //initialize the urlBuilder
        HttpUrl.Builder urlBuilder =  HttpUrl.parse("http://165g123.e2.mars-hosting.com/api/training_service/getSharedTrainings").newBuilder();
        //build the url
        String URL = urlBuilder.build().toString();
        //form the request
        Request request = new Request.Builder().url(URL).build();
        //Callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //in case of network error
                Log.i("IMPORTANT", "500");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //success
                store.setServerResponseSharedTrainings(JsonParser.extractJsonArray(response.body().string()));
                Log.i("SharedTrainings",store.getServerResponseSharedTrainings());

            }
        });
    }

    /*******************Weight Service**********************/
    public static void getStartWeight (OkHttpClient client){
        HttpUrl.Builder builder = HttpUrl.parse("http://165g123.e2.mars-hosting.com/api/userinfo/getStartWeight").newBuilder();
        builder.addQueryParameter("username",store.getUSERNAME());

        String url = builder.build().toString();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                store.setUserStartWeight(JsonParser.parsemsg(response.body().string()));
                Log.i("Response from nh", store.getUserStartWeight());
            }
        });

    }

    public static void getWeight(OkHttpClient client){
        if(store.getUSERNAME().equals("")){
            Log.e("nh get weight","username not found");
            return;
        }
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://165g123.e2.mars-hosting.com/api/userinfo/getWeight").newBuilder();
        urlBuilder.addQueryParameter("username",store.getUSERNAME());

        String URL = urlBuilder.build().toString();
        Request request = new Request.Builder().url(URL).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                store.setUserWeightKg(JsonParser.parsemsg(response.body().string()));
                Log.i("response from nh", store.getUserWeightKg());
            }
        });
    }

    public static void getWeightLog(OkHttpClient client){
        HttpUrl.Builder builder =  HttpUrl.parse("http://165g123.e2.mars-hosting.com/api/weight_service/get_log").newBuilder();
        builder.addQueryParameter("username",store.getUSERNAME());

        String url = builder.build().toString();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                store.setDateStrings(JsonParser.extractJsonArray(response.body().string()));
                Log.i("IMP RESPONSE FROM NH", store.getDateStrings());
            }
        });
    }

    public static void patchWeightGoal(OkHttpClient client,String newWeight){
        //Build the request Body Data
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("username",store.getUSERNAME());
        builder.add("weight", newWeight);
        //Build the actual body
        RequestBody body = builder.build();
        //atqach the body to a new request
        Request request = new Request.Builder()
                .url("http://165g123.e2.mars-hosting.com/api/userinfo/patchUserWeightGoal")
                .patch(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                store.setServerResponseWeightGoalPatched(response.body().string());
                Log.i("RESPONSE FROM NH: ", store.getServerResponseWeightGoalPatched());
            }
        });
    }
    //tracking subservice
    public static void postWeightTrackEntry(OkHttpClient client,String weight_value,String date){
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("username",store.getUSERNAME());
        formBuilder.add("weight_value",weight_value);
        if(date.equals("")) {
            formBuilder.add("date", store.getDateInFocus());
        }else{
            formBuilder.add("date",date);
        }

        RequestBody body = formBuilder.build();
        Request request = new Request.Builder()
                .url("http://165g123.e2.mars-hosting.com/api/weight_service/add_entry")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
               store.setServerResponseAdderWeightEntry(response.body().string());
               Log.i("Response from nh",store.getServerResponseAdderWeightEntry());

            }
        });

    }

    public static void deleteWeightEntry(OkHttpClient client){
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("username",store.getUSERNAME());
        formBuilder.add("date", store.getDateInFocus());
        RequestBody body = formBuilder.build();

        Request request = new Request.Builder()
                .url("http://165g123.e2.mars-hosting.com/api/weight_service/delete_entry")
                .delete(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                store.setServerResponseDeletedWeightEntry(response.body().string());
                Log.i("response from nh", store.getServerResponseDeletedEntry());
            }
        });
    }

    /***********************Refactoring for higher level of abstraction********************/
    public static void get(OkHttpClient client, String url, Map<String,String> params) throws IOException{
        //initialize response;
        String stringResponse = "";
        //initialize the urlBuilder
        HttpUrl.Builder urlBuilder =  HttpUrl.parse(url).newBuilder();
        //add the parameters to the url
        for(Map.Entry<String,String> entry : params.entrySet()){
            urlBuilder.addQueryParameter(entry.getKey(),entry.getValue());
        }
        //build the url
        String URL = urlBuilder.build().toString();
        //form the request
        Request request = new Request.Builder().url(URL).build();
        //Callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //in case of network error
                Log.i("IMPORTANT", "500");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //success
                store.setServerResponseLogin(response.body().string());
                Log.i("response from nh", store.getServerResponseLogin());

            }
        });


    //Request request = new Request.Builder().url(url)
    }

    public static void registerUser(OkHttpClient client, Map<String,String> params){
        FormBody.Builder urlBuilder = new FormBody.Builder();
        for(Map.Entry<String,String> param : params.entrySet()){
            urlBuilder.add(param.getKey(),param.getValue());
        }
        RequestBody body = urlBuilder.build();
        Request request = new Request.Builder().url("http://165g123.e2.mars-hosting.com/api/login_register/reg_user").post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                store.setServerResponseRegister(response.body().string());
                Log.i("RESPONSE FROM NH", store.getServerResponseRegister());
            }
        });
    }



}
