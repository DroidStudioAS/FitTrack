package com.aa.fittracker.logic;

import com.aa.fittracker.models.Training;

import java.util.ArrayList;

public class JsonParser {

    public static String parsemsg(String msg){
        String resp = "";

        String [] split = msg.split(":");
        String rawAwnser = split[1];
        String formated = rawAwnser.substring(0,rawAwnser.length()-1);

        return formated;
    }

    public static ArrayList<Training> parseExc(String serverResponseAllExc){
        ArrayList<Training> trainings = new ArrayList<>();

        return trainings;
    }
    public static String extractJsonArray(String jsonString) {
        // Find the index of "[" and "]"
        int startIndex = jsonString.indexOf('[');
        int endIndex = jsonString.lastIndexOf(']');

        // Extract the JSON array string inside "[" and "]"
        if (startIndex != -1 && endIndex != -1) {
            return jsonString.substring(startIndex, endIndex + 1);
        }

        return "";
    }
}

