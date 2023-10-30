package com.aa.fittracker.network;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class networkHelper {

    public static String makeGetRequest(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (response.isSuccessful() && responseBody != null) {
                return responseBody.string(); // Get the response body as a string
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static String makePostRequest(String url, JSONObject jsonPayload) {
        OkHttpClient client = new OkHttpClient();

        // Create a JSON request body
        RequestBody requestBody = RequestBody.create(JSON, jsonPayload.toString());

        // Build the POST request
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (response.isSuccessful() && responseBody != null) {
                String result = responseBody.string();
                // Handle the response
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle network errors
        }

        return null;
    }
}

