package com.aa.fittracker.logic;

import android.util.Log;

import java.time.LocalDate;
import java.util.ArrayList;

public class DateParser {
    public static String[] parseDate(String date){
        String[] parsed = date.split(" ");
        Log.i("year", parsed[0]);
        Log.i("Month", parsed[1]);
        Log.i("Day", parsed[2]);
        return parsed;
    }

        public static ArrayList<Integer> dateFinder() {
            ArrayList<Integer> last7Days = new ArrayList<>();
            String date = store.getDateInFocus();
            String[] parts = date.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);

            // If the current day is less than 7, determine the remaining days of the previous month
            if (day < 7) {
                // Calculate the last day of the previous month
                int lastDayOfPrevMonth = LocalDate.of(year, month, 1).minusDays(1).getDayOfMonth();
                int remainingDays = 7 - day;

                for (int i = day; i > 0; i--) {
                    last7Days.add(i);
                }

                // Add remaining days from the end of the last month in descending order
                for (int i = lastDayOfPrevMonth; i > lastDayOfPrevMonth - remainingDays; i--) {
                    last7Days.add(i);
                }
            }
            if(day>=7) {
                for (int i = day; i > 0; i--) {
                    last7Days.add(i);
                }
            }

            return last7Days;
        }

}
