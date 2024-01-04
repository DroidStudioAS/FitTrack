package com.aa.fittracker.logic;

import android.util.Log;

import java.util.List;

public class Debuger {
    public static void dateLog(String var, String value){
        Log.i("EXCDATE", var + " : " + value);
    }
    public static void dateListLogger(List<String> list, String listName){
        for(String x : list){
            Log.i("EXCDATE LIST  " + listName , x);
        }
    }
}
