package com.aa.fittracker.network;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class networkHelper {

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
                Log.i("IMPORTANT", response.body().string());

            }
        });


    //Request request = new Request.Builder().url(url)
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void makePostRequest(String url, JSONObject jsonPayload, final NetworkCallback callback) {
        OkHttpClient client = new OkHttpClient();

        // Create a JSON request body
        RequestBody requestBody = RequestBody.create(JSON, jsonPayload.toString());

        // Build the POST request
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        String responseData = responseBody.string();
                        callback.onSuccess(responseData);
                    }
                } else {
                    callback.onFailure(new IOException("Request failed with code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }
        });
    }
}
