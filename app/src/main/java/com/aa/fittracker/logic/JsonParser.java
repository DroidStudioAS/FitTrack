package com.aa.fittracker.logic;
public class JsonParser {

    public static String parsemsg(String msg){
        String resp = "";
        String [] split = msg.split(":");
        String rawAwnser = split[1];
        String formated = rawAwnser.substring(0,rawAwnser.length()-1);
        return formated;
    }


}
