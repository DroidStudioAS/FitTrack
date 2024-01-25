package com.aa.fittracker.logic;

import android.util.Log;

import com.aa.fittracker.models.Training;
import com.aa.fittracker.models.TrainingEntry;
import com.aa.fittracker.models.WeightEntry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static ArrayList<String> dateSorter(ArrayList<String> unsorted) {
        // Create a list to store parsed dates
        ArrayList<LocalDate> parsedDates = new ArrayList<>();

        // Parse strings into LocalDate objects
        for (String dateString : unsorted) {
            if (dateString.contains("?")) {
                // Handle the case where the date contains "?"
                parsedDates.add(null);
            } else {
                // Manually parse the date string
                String[] dateComponents = dateString.split("-");
                int year = Integer.parseInt(dateComponents[0]);
                int month = Integer.parseInt(dateComponents[1]);
                int day = Integer.parseInt(dateComponents[2]);

                // Create a LocalDate object and add it to the list
                LocalDate date = LocalDate.of(year, month, day);
                parsedDates.add(date);
            }
        }

        // Sort the dates (null values will be placed at the beginning)
        parsedDates.sort(Comparator.nullsFirst(Comparator.naturalOrder()));

        // Convert sorted dates back to strings in the original format
        ArrayList<String> sorted = new ArrayList<>();
        for (LocalDate date : parsedDates) {
            if (date == null) {
                sorted.add("?");
            } else {
                sorted.add(date.toString());
            }
        }

        return sorted;
    }

    public static ArrayList<String> listMaker(ArrayList<String> sorted, String date) {

        for(String x : sorted){
            Log.i("x in sorted", x);
        }
        ArrayList<String> checkList = new ArrayList<>();
        for (String x : sorted) {
            checkList.add(x.split("-")[2]);
        }

        int dayInt = Integer.parseInt(date.split("-")[2]);
        int monthInt = Integer.parseInt(date.split("-")[1]);
        int yearInt = Integer.parseInt(date.split("-")[0]);

        boolean isYearOverlap = false;

        ArrayList<String> finalSorted = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            int day = dayInt - i;
            if (day > 0) {
                if (day > 9) {
                    finalSorted.add(String.format("%04d-%02d-%02d", yearInt, monthInt, day));
                } else {
                    finalSorted.add(String.format("%04d-%02d-0%d", yearInt, monthInt, day));
                }
            } else {
                if(monthInt==1 && !isYearOverlap){
                    yearInt=yearInt-1;
                    isYearOverlap=true;
                }
                int lastMonth = (monthInt != 1) ? monthInt - 1 : 12;
                int daysInLastMonth = getDaysInMonth(lastMonth, yearInt); // Replace with appropriate year
                finalSorted.add(String.format("%04d-%02d-%02d", yearInt, lastMonth, daysInLastMonth + day));
            }
        }

        for (int i = 0; i < finalSorted.size(); i++) {
            String dayString = finalSorted.get(i);

            if (!checkList.contains(dayString.split("-")[2])) {
                finalSorted.set(i, "?" + dayString + "?");
            }
            Log.i("sorted in dp", finalSorted.get(i));
        }

        Collections.reverse(finalSorted);



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

    public static HashMap<String,String> last7DaysWeight() {
        HashMap<String,String> last7Days = new HashMap<>();
        String date = store.getDateInFocus();
        String[] parts = date.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);
        boolean isYearOverlap = false;



        for (int i = 0; i < 7; i++) {
            String dateToFind = "";
            int dayToFind = day - i;

            if (dayToFind > 0) {
                // Construct the date normally
                dateToFind = year + "-" + (month > 9 ? month : "0" + month) + "-" + (dayToFind > 9 ? dayToFind : "0" + dayToFind);
            } else {
                if(month==1 && !isYearOverlap){
                    year=year-1;
                    isYearOverlap=true;
                }
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

            Debuger.dateLog("dtf in l7dw", dateToFind);
            for (WeightEntry x : store.getWeightEntries()) {
                Log.i("dtw", x.getWeight_date());

                if (x.getWeight_date().equals(dateToFind)) {
                    Log.i("dtw: ", dateToFind + "-" + x.getWeight_date());
                    last7Days.put(x.getWeight_date(),x.getWeight_value());
                    matchFound = true;
                    break;
                }
            }

        }
        for(Map.Entry x : last7Days.entrySet()){
            Log.i("EXCDATE", "Map keys in end of l7dw" + x.getKey() + " : " + x.getValue());
        }


        return last7Days;
    }

    public static HashMap<String,String> last7DaysTraining() {
        HashMap<String,String> last7Days = new HashMap<>();
        String date = store.getDateInFocus();
        String[] parts = date.split("-");

        boolean isYearOverlap = false;
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);

        for (int i = 0; i < 7; i++) {
            String dateToFind = "";
            int dayToFind = day - i;

            if (dayToFind > 0) {
                // Construct the date normally
                dateToFind = year + "-" + (month > 9 ? month : "0" + month) + "-" + (dayToFind > 9 ? dayToFind : "0" + dayToFind);
            }
            else {

                if(month==1 && !isYearOverlap){
                    year=year-1;
                    isYearOverlap=true;
                }

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

            Debuger.dateLog("dtf in l7dt", dateToFind);
            for (TrainingEntry x : store.getTrainingEntries()) {
                Log.i("dtm", x.getTraining_date());

                if (x.getTraining_date().equals(dateToFind)) {
                    Log.i("Match found in fr: ", dateToFind + "-" + x.getTraining_date());
                    last7Days.put(x.getTraining_date(),x.getTraining_name());
                    matchFound = true;
                    break;
                }
            }

           /* if (!matchFound && i==6){
                //dummy date to prevent crashing
                last7Days.put(dateToFind,"");
            }*/
            if(i==6 && !matchFound){
                return last7Days;
            }

        }

        for(Map.Entry x : last7Days.entrySet()){
            Log.i("EXCDATE", "Map keys in end of l7dt" + x.getKey() + " : " + x.getValue());
        }


        return last7Days;
    }
    public static ArrayList<String> monthBreakdown(){
        ArrayList<String> toReturn = new ArrayList<>();
        String lookingFor = "";
        if(!store.getDateInFocus().equals("")) {
            String yearInFocus = store.getDateInFocus().split("-")[0];
            String monthInFocus = store.getDateInFocus().split("-")[1];
            Log.i("Month In Focus: ", monthInFocus);
            Log.i("Year In Focus", yearInFocus);

            switch (store.getUserMode()){
                case "journal":
                    lookingFor=("j");
                    break;
                case "weight":
                    lookingFor="w";
            }
            if(lookingFor.equals("j")){
                for(TrainingEntry trainingEntry : store.getTrainingEntries()){
                   String [] splitDate = trainingEntry.getTraining_date().split("-");
                   if(splitDate[0].equals(yearInFocus) && splitDate[1].equals(monthInFocus)){
                       //FOUND
                       toReturn.add(trainingEntry.getTraining_date() + " : " + trainingEntry.getTraining_name());
                       }

                   }
                } else if(lookingFor.equals("w")){
                for(WeightEntry weightEntry : store.getWeightEntries()){
                    String [] splitDate = weightEntry.getWeight_date().split("-");
                    if(splitDate[0].equals(yearInFocus) && splitDate[1].equals(monthInFocus)){
                        //FOUND
                        toReturn.add(weightEntry.getWeight_date() + " : " + weightEntry.getWeight_value());
                    }
                }
            }

        }

        for(String x : toReturn){
            Log.i("mb : " , x);
        }
        return toReturn;
    }
    public static HashMap<String, String> monthBreakdownCounter(ArrayList<String> listToCount){
        /*******Training vars*****/
        int eazyCount = 0;
        int midCount = 0;
        int hardCount = 0;
        int totalRestCount = 0;
        int plannedRestCount = 0;
        /*******Weight Vars********/
        int goodChangeCount = 0;
        int badChangeCount =0;
        int perfectCount=0;

        HashMap<String, String> toReturn = new HashMap<>();
        if(store.getDateInFocus().equals("")){
            return toReturn;
        }

        int daysInMonth = getDaysInMonth(Integer.parseInt(store.getDateInFocus().split("-")[1]), Integer.parseInt(store.getDateInFocus().split("-")[0]));
        int missingDays = daysInMonth-listToCount.size();
        Log.i("Missing data for", String.valueOf(missingDays));
        toReturn.put("missing: " , String.valueOf(missingDays));
        switch (store.getUserMode()){
            case "journal":
                //get num of missing days

                //Count Out The Monthly Preformance
                for(String x : listToCount){
                    if(x.contains("REST DAY") || x.contains("SKIPPED DAY")){
                        totalRestCount+=1;
                        if(x.contains("REST DAY")){
                            plannedRestCount+=1;
                        }
                    }
                    for(Training y : store.getUserTrainings()){
                        if(x.contains(y.getTraining_name())){
                            if(y.getTraining_difficulty()==1){
                                eazyCount+=1;
                            }else if(y.getTraining_difficulty()==2){
                                midCount+=1;
                            }else if(y.getTraining_difficulty()==3){
                                hardCount+=1;
                            }
                        }
                    }
                }
                toReturn.put("eazy: " , String.valueOf(eazyCount));
                toReturn.put("mid: " , String.valueOf(midCount));
                toReturn.put("hard: " , String.valueOf(hardCount));
                toReturn.put("totalR: ", String.valueOf(totalRestCount));
                toReturn.put("plannedR: ", String.valueOf(plannedRestCount));

                break;
            case "weight":
                int startWeight = Integer.parseInt(store.getUserStartWeight());
                for(String x : listToCount) {
                    Double weightOnDate = Double.parseDouble(x.split(":")[1]);
                    for (WeightEntry z : store.getWeightEntries()) {
                        if (x.contains(z.getWeight_date())) {
                            double delta = weightOnDate - startWeight;
                            switch (store.getUserWeightGoal()) {
                                case "+":
                                    if (weightOnDate == Double.parseDouble(store.getUserWeightKg())) {
                                        perfectCount += 1;
                                    } else if (delta > 0) {
                                        goodChangeCount += 1;
                                    } else if (delta <= 0) {
                                        badChangeCount += 1;
                                    }
                                    break;
                                case "-":
                                    if (weightOnDate == Double.parseDouble(store.getUserWeightKg())) {
                                        perfectCount += 1;
                                    } else if (delta >= 0) {
                                        badChangeCount += 1;
                                    } else if (delta < 0) {
                                        goodChangeCount += 1;
                                    }
                                    break;
                            }

                        }
                    }
                }
                toReturn.put("perfect", String.valueOf(perfectCount));
                toReturn.put("good", String.valueOf(goodChangeCount));
                toReturn.put("bad", String.valueOf(badChangeCount));




                break;
        }

        return toReturn;
    }
    public static String getSevenDaysBefore(String inputDate) {
        // Parse the input date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(inputDate, formatter);

        // Subtract 7 days
        LocalDate sevenDaysBefore = date.minusDays(6);

        // Format the result back to the input format
        return sevenDaysBefore.format(formatter);
    }

}
