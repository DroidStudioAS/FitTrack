package com.aa.fittracker.presentation;
import android.service.autofill.FillEventHistory;
import android.util.Log;

import com.aa.fittracker.*;
import com.aa.fittracker.logic.store;
import com.applandeo.materialcalendarview.EventDay;

import java.util.ArrayList;
import java.util.List;
//Log.i("EVENTDAY :", store.getUserEventDaysActive)
public class CalendarAdapter {
    public static List<EventDay> listReturner(){
        Log.i("EVENT-DAY BEFORE CLR :", String.valueOf(store.getUserEventDaysActive().size()));
        store.getUserEventDaysActive().clear();
        Log.i("EVENT-DAY AFTER CLR :", String.valueOf(store.getUserEventDaysActive().size()));
        List<EventDay> output = new ArrayList<>();
        switch (store.getUserMode()){

        }


        return output;
    }


}
