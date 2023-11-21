package com.aa.fittracker.network;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.IOException;
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

public class networkHelper {
    public static String SERVER_RESPONSE;

    public interface NetworkCallback {
        void onSuccess(String response);

        void onFailure(IOException e);
    }
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
    public static void post(OkHttpClient client, String url, Map<String, String> params) throws IOException {
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
                    store.setServerResponseRegister(response.body().string());
                    Log.i("response from nh", store.getServerResponseRegister());
                } else {
                    Log.i("IMPORTANT", "POST request failed with response code: " + response.code());
                }
            }
        });
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
}
