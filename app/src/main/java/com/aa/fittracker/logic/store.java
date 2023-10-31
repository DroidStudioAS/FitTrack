package com.aa.fittracker.logic;
public class store {
    private static String SERVER_RESPONSE = "";

    public static String getSERVER_RESPONSE() {
        return SERVER_RESPONSE;
    }
    public static void setServerResponse(String serverResponse) {
        SERVER_RESPONSE = serverResponse;
    }
}
