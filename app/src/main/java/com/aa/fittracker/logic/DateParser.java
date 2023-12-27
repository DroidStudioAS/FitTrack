package com.aa.fittracker.logic;

import android.util.Log;

import java.util.ArrayList;

public class DateParser {
    public static String[] parseDate(String date){
        String[] parsed = date.split(" ");
        Log.i("year", parsed[0]);
        Log.i("Month", parsed[1]);
        Log.i("Day", parsed[2]);
        return parsed;
    }

    public static ArrayList<Integer> dateFinder(){
        ArrayList <Integer> last7Days = new ArrayList<>();
        String date = store.getDateInFocus();
        String Day = date.split("-")[2];
        int dayValue = Integer.parseInt(Day);
        last7Days.add(dayValue);
        for(int i = 1; i <7; i++){
            if(dayValue>=7) {
                int toAdd = dayValue - i;
                last7Days.add(toAdd);
                Log.i("toAdd", String.valueOf(toAdd));
            }
        }

        return last7Days;

    }
}
