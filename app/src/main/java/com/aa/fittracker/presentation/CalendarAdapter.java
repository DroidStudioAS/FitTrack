package com.aa.fittracker.presentation;
import android.service.autofill.FillEventHistory;
import android.util.Log;

import com.aa.fittracker.*;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.Training;
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
                    /***************Build the calendar****************/
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
                    /***************Icon Determiner****************/
                    /***************Rest Days****************/
                    if(x.getTraining_name().equals("REST DAY")){
                        output.add(new EventDay(calendar,R.drawable.icon_good_restt));
                    }else if(x.getTraining_name().equals("SKIPPED DAY")){
                        output.add(new EventDay(calendar,R.drawable.icon_bad_restt));
                    }
                    /***************Active Days****************/
                    Log.i("DIFFICULTY FOUND:", String.valueOf(trainingDifficultyfFinder(x.getTraining_name())));
                    switch (trainingDifficultyfFinder(x.getTraining_name())){
                        case 1:
                            output.add(new EventDay(calendar,R.drawable.icon_easy_tr));
                            break;
                        case 2:
                            output.add(new EventDay(calendar,R.drawable.icon_medium_training));
                            break;
                        case 3:
                            output.add(new EventDay(calendar,R.drawable.icon_hard_training));
                            break;
                    }
                }

                for(EventDay x : output){
                    Log.i("USER MODE", store.getUserMode());
                    Log.i("LIST AFTER FOR-EACH", String.valueOf(x.getCalendar().get(Calendar.YEAR)) + " " + x.getCalendar().get(Calendar.MONTH) + " " + x.getCalendar().get(Calendar.DAY_OF_MONTH));
                }
                break;
            case "weight":
                double startWeight = Double.parseDouble(store.getUserStartWeight());
                double optimalWeight = Double.parseDouble(store.getUserWeightKg());
                /***************Determine if user wants to lose or gain weight****************/
                if(startWeight-optimalWeight>0){
                    store.setUserWeightGoal("-");
                }else {
                    store.setUserWeightGoal("+");
                }
                for(WeightEntry x : store.getWeightEntries()){
                    double weightOnActiveDate = Double.parseDouble(x.getWeight_value());  //fetches Weight on day of Entry
                    /***************Build The Calendar****************/
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

                    /***************Icon Determiner****************/

                    /***************User Achieved Weight Goal On This Date****************/
                    if(optimalWeight==weightOnActiveDate){
                        output.add(new EventDay(calendar,R.drawable.icon_perfect_weight));
                    }
                    /******************Does user want to gain (+) or lose(-) name****************/
                    switch (store.getUserWeightGoal()){
                        case "+":
                            if(weightOnActiveDate>startWeight){
                                output.add(new EventDay(calendar,R.drawable.icon_good_sc));
                            }else{
                                output.add(new EventDay(calendar,R.drawable.icon_bad_sc));
                            }
                            break;
                        case "-":  //opposite logic to +
                            if(weightOnActiveDate<startWeight){
                                output.add(new EventDay(calendar,R.drawable.icon_good_sc));
                            }else{
                                output.add(new EventDay(calendar,R.drawable.icon_bad_sc));
                            }
                            break;
                    }
                }

                for(EventDay x : output){
                    Log.i("USER MODE", store.getUserMode());
                    Log.i("LIST BEFORE FOR-EACH", String.valueOf(x.getCalendar().get(Calendar.YEAR)) + " " + x.getCalendar().get(Calendar.MONTH) + " " + x.getCalendar().get(Calendar.DAY_OF_MONTH));
                }
                break;
        }

        store.setUserEventDaysActive(output);
    }
    public static int trainingDifficultyfFinder(String Name){
        int diff = -1;
        if(store.getUserTrainings().size()!=0){
           for(Training x : store.getUserTrainings()){
               Log.i("Inside the tdf", x.toString());
               if(x.getTraining_name().equals(Name)){
                   diff=x.getTraining_difficulty();
               }
           }
        }
        return diff;
    }


}
