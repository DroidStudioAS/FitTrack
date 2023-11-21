package com.aa.fittracker.logic;
public class store {
    //VARIABLES
    private static String SERVER_RESPONSE_LOGIN = "";
    private static String SERVER_RESPONSE_REGISTER = "";
    //USER DATA
    private static String USERNAME = "";
    private static String USER_WEIGHT_KG="";

    public static String getUserWeightKg() {
        return USER_WEIGHT_KG;
    }

    public static void setUserWeightKg(String userWeightKg) {
        USER_WEIGHT_KG = userWeightKg;
    }

    public static String getServerResponseLogin() {
        return SERVER_RESPONSE_LOGIN;
    }

    public static void setServerResponseLogin(String serverResponseLogin) {
        SERVER_RESPONSE_LOGIN = serverResponseLogin;
    }

    public static String getServerResponseRegister() {
        return SERVER_RESPONSE_REGISTER;
    }

    public static void setServerResponseRegister(String serverResponseRegister) {
        SERVER_RESPONSE_REGISTER = serverResponseRegister;
    }

    public static String getUSERNAME() {
        return USERNAME;
    }

    public static void setUSERNAME(String USERNAME) {
        store.USERNAME = USERNAME;
    }
}
