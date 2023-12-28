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
    public static ArrayList<String> listMaker (ArrayList<String> sorted, String date){
        //get day dates from sorted
        ArrayList<String> checkList = new ArrayList<>();
        for(String x : sorted){
            checkList.add(x.split("-")[2]);
        }

        int yearInt = Integer.parseInt(date.split("-")[0]);
        int dayInt = Integer.parseInt(date.split("-")[2]);
        int monthInt = Integer.parseInt(date.split("-")[1]);
        int lastmonth;
        if(monthInt!=1){
            lastmonth = monthInt-1;
        }else{
            lastmonth=12;
        }

        ArrayList<String> finalSorted = new ArrayList<>();

        if(dayInt>9) {
            finalSorted.add(String.valueOf(dayInt));
        }else{
            finalSorted.add("0" + String.valueOf(dayInt));
        }

        if(dayInt>=7){
            for(int i =1; i <7; i++){
                if(dayInt-i>9) {
                    finalSorted.add(String.valueOf(dayInt - i));
                }else{
                    finalSorted.add("0" + String.valueOf(dayInt - i));
                }
            }
        }else{
            int daysInLastMonth = getDaysInMonth(lastmonth,yearInt);
            int toSubtract = -1;
            for(int i =1; i <7; i++){
                if(dayInt-i>0){
                    finalSorted.add("0"+String.valueOf(dayInt-i));
                }else{
                    toSubtract=Math.abs(dayInt-i);
                }
            }
            finalSorted.add(String.valueOf(daysInLastMonth));
            for(int i = 1; i<=toSubtract;i++){
                finalSorted.add(String.valueOf(daysInLastMonth-i));
            }
        }

        for (int i = 0; i < finalSorted.size(); i++) {
            String dayString = finalSorted.get(i);

            int day = Integer.parseInt(dayString);

            int year = yearInt;
            int month = monthInt;

            if (day > 9) {
                finalSorted.set(i, String.format("%04d-%02d-%02d", year, month, day));
            } else {
                finalSorted.set(i, String.format("%04d-%02d-0%d", year, month, day));
            }

            if (!checkList.contains(dayString)) {
                finalSorted.set(i, "?" + finalSorted.get(i) + "?");
            }
            Log.i("sorted in dp", finalSorted.get(i));
        }



        return finalSorted;
    }
    public static int getDaysInMonth(int month, int year) {
        int days;

        switch (month) {
            case 1: // January
            case 3: // March
            case 5: // May
            case 7: // July
            case 8: // August
            case 10: // October
            case 12: // December
                days = 31;
                break;
            case 4: // April
            case 6: // June
            case 9: // September
            case 11: // November
                days = 30;
                break;
            case 2: // February
                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                    // Leap year logic: divisible by 4 and not 100 unless divisible by 400
                    days = 29;
                } else {
                    days = 28;
                }
                break;
            default:
                days = -1; // Invalid month input
                break;
        }

        return days;
    }
}
