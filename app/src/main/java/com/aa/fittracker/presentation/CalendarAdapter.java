package com.aa.fittracker.presentation;
import android.service.autofill.FillEventHistory;
import android.util.Log;

import com.aa.fittracker.*;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.TrainingEntry;
import com.aa.fittracker.models.WeightEntry;
import com.applandeo.materialcalendarview.EventDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
//Log.i("EVENTDAY :", store.getUserEventDaysActive)
public class CalendarAdapter {
    public static void listReturner(){
        Log.i("EVENT-DAY BEFORE CLR :", String.valueOf(store.getUserEventDaysActive().size()));
           Log.i("EVENT-DAY AFTER CLR :", String.valueOf(store.getUserEventDaysActive().size()));

        List<EventDay> output = new ArrayList<>();

        switch (store.getUserMode()){
            case"journal":
                for(TrainingEntry x : store.getTrainingEntries()){

                    Calendar calendar = Calendar.getInstance();

                   String[] parsed = x.getTraining_date().split("-");
                   int year=Integer.parseInt(parsed[0]);
                   int monthWithoutCorrection = Integer.parseInt(parsed[1]);
                   int month = monthWithoutCorrection-1;
                   int day= Integer.parseInt(parsed[2]);

                   Log.i(" NCR DATE TO INSERT",String.valueOf(year + " " + monthWithoutCorrection + " " + day));
                   Log.i(" CR DATE TO INSERT",String.valueOf(year + " " + month + " " + day));

                   calendar.set(year,month,day);
                   Log.i("CAL YEAR", String.valueOf(calendar.get(Calendar.YEAR)));
                    Log.i("CAL MONTH", String.valueOf(calendar.get(Calendar.MONTH)));
                    Log.i("CAL DAY", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));

                    if(x.getTraining_name().equals("REST DAY")){
                        output.add(new EventDay(calendar,R.drawable.icon_good_rest));
                    }else if(x.getTraining_name().equals("SKIPPED DAY")){
                        output.add(new EventDay(calendar,R.drawable.icon_bad_rest));

                    }else{
                        output.add(new EventDay(calendar,R.drawable.icon_weight));
                    }

                }
                for(EventDay x : output){
                    Log.i("USER MODE", store.getUserMode());
                    Log.i("LIST AFTER FOR-EACH", String.valueOf(x.getCalendar().get(Calendar.YEAR)) + " " + x.getCalendar().get(Calendar.MONTH) + " " + x.getCalendar().get(Calendar.DAY_OF_MONTH));
                }
                break;
            case "weight":
                for(WeightEntry x : store.getWeightEntries()){

                    Calendar calendar = Calendar.getInstance();

                    String[] parsed = x.getWeight_date().split("-");
                    int year=Integer.parseInt(parsed[0]);
                    int monthWithoutCorrection = Integer.parseInt(parsed[1]);
                    int month = monthWithoutCorrection-1;
                    int day= Integer.parseInt(parsed[2]);

                    Log.i(" NCR DATE TO INSERT",String.valueOf(year + " " + monthWithoutCorrection + " " + day));
                    Log.i(" CR DATE TO INSERT",String.valueOf(year + " " + month + " " + day));

                    calendar.set(year,month,day);
                    Log.i("CAL YEAR", String.valueOf(calendar.get(Calendar.YEAR)));
                    Log.i("CAL MONTH", String.valueOf(calendar.get(Calendar.MONTH)));
                    Log.i("CAL DAY", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));

                    output.add(new EventDay(calendar,R.drawable.training_service_drawable));

                }

                for(EventDay x : output){
                    Log.i("USER MODE", store.getUserMode());
                    Log.i("LIST BEFORE FOR-EACH", String.valueOf(x.getCalendar().get(Calendar.YEAR)) + " " + x.getCalendar().get(Calendar.MONTH) + " " + x.getCalendar().get(Calendar.DAY_OF_MONTH));
                }
                break;
        }

        store.setUserEventDaysActive(output);
    }


}
