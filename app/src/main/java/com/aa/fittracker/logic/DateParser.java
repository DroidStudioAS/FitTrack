package com.aa.fittracker.logic;

import android.util.Log;

import com.aa.fittracker.models.TrainingEntry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

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
    public static HashMap<String,String> last7DaysTraining() {
       HashMap<String,String> last7Days = new HashMap<>();
        String date = store.getDateInFocus();
        String[] parts = date.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);

        for (int i = 0; i < 7; i++) {
            String dateToFind = "";
            int dayToFind = day - i;

            if (dayToFind > 0) {
                // Construct the date normally
                dateToFind = year + "-" + (month > 9 ? month : "0" + month) + "-" + (dayToFind > 9 ? dayToFind : "0" + dayToFind);
            } else {
                // Adjust the date for the previous month
                int lastDayOfPrevMonth = LocalDate.of(year, month, 1).minusDays(1).getDayOfMonth();
                int remainingDays = Math.abs(dayToFind); // Remaining days needed from the previous month

                // Construct the date for the previous month, including the last day if necessary
                if (lastDayOfPrevMonth - remainingDays + 1 == 1) {
                    dateToFind = year + "-" + (month > 1 ? month - 1 : 12) + "-" + (lastDayOfPrevMonth - remainingDays + 1);
                } else {
                    dateToFind = year + "-" + (month > 1 ? month - 1 : 12) + "-" + (lastDayOfPrevMonth - remainingDays);
                }
            }

            boolean matchFound = false;

            Log.i("dtf", dateToFind);
            for (TrainingEntry x : store.getTrainingEntries()) {
                Log.i("dtm", x.getTraining_date());

                if (x.getTraining_date().equals(dateToFind)) {
                    Log.i("Match found in fr: ", dateToFind + "-" + x.getTraining_date());
                    last7Days.put(x.getTraining_date(),x.getTraining_name());
                    matchFound = true;
                    break;
                }
            }


        }

        return last7Days;
    }



    public static ArrayList<String> dateSorter(ArrayList<String> unsorted) {
        // Create a list to store parsed dates
        ArrayList<LocalDate> parsedDates = new ArrayList<>();

        // Parse strings into LocalDate objects
        for (String dateString : unsorted) {
            LocalDate date = LocalDate.parse(dateString);
            parsedDates.add(date);
        }

        // Sort the dates
        parsedDates.sort(Comparator.naturalOrder());

        // Convert sorted dates back to strings in the original format
        ArrayList<String> sorted = new ArrayList<>();
        for (LocalDate date : parsedDates) {
            sorted.add(date.toString());
        }

        return sorted;
    }
}
