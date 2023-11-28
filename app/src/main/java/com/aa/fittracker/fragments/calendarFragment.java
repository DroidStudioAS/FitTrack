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
import com.aa.fittracker.OnInfoInputListener;
import com.aa.fittracker.R;
import com.aa.fittracker.logic.DateParser;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.TrainingEntry;
import com.aa.fittracker.models.WeightEntry;
import com.aa.fittracker.presentation.CalendarAdapter;
import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class calendarFragment extends Fragment implements OnInfoInputListener {
OnDateClickListener onDateClickListener;
List<String> dates;

public static List<EventDay> eventDays = new ArrayList<>();

protected static CalendarView calendarView;
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
        // Set the list of CalendarDay objects to your calendarView
        calendarView=(CalendarView)view.findViewById(R.id.calendarView);
        if (store.getTrainingEntries().isEmpty()){
            Log.i("NO ENTRIES FETCHED YER","...");
        }
        CalendarAdapter.listReturner();
        eventDays=store.getUserEventDaysActive();

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
        calendarView.setEvents(eventDays);
        return view;
    }
    public static void calendarRefresh(){
        eventDays=store.getUserEventDaysActive();
        calendarView.setEvents(eventDays);

        calendarView.invalidate();
    }

    @Override
    public void onWeightInput(WeightEntry x) {
        Log.i("Weight Added", x.getWeight_value() + " on date: " + x.getWeight_date());
        calendarRefresh();

    }

    @Override
    public void onTrainingInput(TrainingEntry x) {
        Log.i("Training Added", x.getTraining_name() +" on date " + x.getTraining_date());
        calendarRefresh();


    }

    @Override
    public void onDeleted() {

    }
}