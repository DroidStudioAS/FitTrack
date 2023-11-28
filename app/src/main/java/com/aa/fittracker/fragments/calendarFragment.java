package com.aa.fittracker.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aa.fittracker.OnDateClickListener;
import com.aa.fittracker.R;
import com.aa.fittracker.logic.DateParser;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.TrainingEntry;
import com.aa.fittracker.models.WeightEntry;
import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class calendarFragment extends Fragment {
OnDateClickListener onDateClickListener;
List<String> dates;

CalendarView calendarView;
    public calendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onDateClickListener = (OnDateClickListener)context;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        //fragment logic start
        List<EventDay> list = new ArrayList<>();

        //in u want to set the 11. month, u have to set 10.
        //december is 11
        //january is 0
        //must adjust with -1

        //store.setDateStrings("2023 10 10");
        //store.setDateStrings("2023 11 11");
        //store.setDateStrings("2023 12 12");


       // calendarDay.setBackgroundResource(R.drawable.fit_track_icon); // Replace R.drawable.your_background_resource with your drawable resource
        //   calendarDay.setBackgroundDrawable(yourDrawable); // Set your drawable here

        //   calendarDay.setSelectedLabelColor(getResources().getColor(R.color.selectedColor)); // Replace R.color.selectedColor with your selected color resource
        //  calendarDay.setSelectedBackgroundResource(R.drawable.selected_background_resource); // Replace R.drawable.selected_background_resource with your selected drawable resource
        //  calendarDay.setSelectedBackgroundDrawable(yourSelectedDrawable); // Set your selected drawable here

        // Add the configured CalendarDay object to the list


        // Set the list of CalendarDay objects to your calendarView
        calendarView=(CalendarView)view.findViewById(R.id.calendarView);
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(@NonNull EventDay eventDay) {
                Calendar calendar = eventDay.getCalendar();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH)+1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String date = year + "-" + month + "-"+ day;
                store.setDateInFocus(date);



                /************callback to activity**********/
                Log.i("date in focus", store.getDateInFocus());
                if (onDateClickListener != null) {
                    onDateClickListener.onDateClicked(date);
                }

                for(WeightEntry x : store.getWeightEntries()){
                    if(x.getWeight_date().equals(store.getDateInFocus())){
                        if(onDateClickListener!=null){
                            onDateClickListener.onMatchFound(x);
                        }
                    }
                }
                for(TrainingEntry x : store.getTrainingEntries()){
                    if(x.getTraining_date().equals(store.getDateInFocus())){
                        if(onDateClickListener!=null){
                            onDateClickListener.onTrainingMatchFound(x);
                        }
                    }
                }
            }
        });
       //fragment logic end
        while (store.getTrainingEntries().isEmpty()){
            Log.i("NO ENTRIES FETCHED YER","...");
        }
        switch (store.getUserMode()){
            case "journal":
                list.clear();
                for(TrainingEntry x : store.getTrainingEntries()){
                    Log.i("ACTIVE","ACTIVE");
                    Calendar calToSet = Calendar.getInstance();
                    String [] dateBits = x.getTraining_date().split("-");

                    int monthWithoutCorrection = Integer.parseInt(dateBits[1]);
                    int month = monthWithoutCorrection-1;

                    calToSet.set(Calendar.YEAR, Integer.parseInt(dateBits[0]));
                    calToSet.set(Calendar.MONTH, month);
                    calToSet.set(Calendar.DAY_OF_MONTH,Integer.parseInt(dateBits[2]));

                    list.add(new EventDay(calToSet,R.drawable.training_service_drawable));
                    Log.i("event day size",String.valueOf(list.size()));

                }
                break;
            case "weight":
                for(WeightEntry x : store.getWeightEntries()){
                    list.clear();
                    Log.i("ACTIVE","ACTIVE");
                    Calendar calToSet = Calendar.getInstance();
                    String [] dateBits = x.getWeight_date().split("-");

                    int monthWithoutCorrection = Integer.parseInt(dateBits[1]);
                    int month = monthWithoutCorrection-1;

                    calToSet.set(Calendar.YEAR, Integer.parseInt(dateBits[0]));
                    calToSet.set(Calendar.MONTH, month);
                    calToSet.set(Calendar.DAY_OF_MONTH,Integer.parseInt(dateBits[2]));

                    list.add(new EventDay(calToSet,R.drawable.weight_service_drawable));
                    Log.i("event day size",String.valueOf(list.size()));

                }

                break;
        }


        calendarView.setEvents(list);
        return view;
    }
}