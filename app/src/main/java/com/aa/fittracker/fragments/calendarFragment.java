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

                if(store.getDatesWithLogs().contains(store.getDateInFocus())){
                    /*************Callback to activity****************/

                }
                for(WeightEntry x : store.getWeightEntries()){
                    if(x.getWeight_date().equals(store.getDateInFocus())){
                        if(onDateClickListener!=null){
                            onDateClickListener.onMatchFound(x);
                        }
                    }
                }

                /************callback to activity**********/
                Log.i("date in focus", store.getDateInFocus());
                if (onDateClickListener != null) {
                    onDateClickListener.onDateClicked(date);
                }
            }
        });
       //fragment logic end
        return view;
    }
}