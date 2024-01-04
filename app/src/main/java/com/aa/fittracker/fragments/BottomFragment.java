package com.aa.fittracker.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.aa.fittracker.FragmentCommunicator;
import com.aa.fittracker.OnDateClickListener;
import com.aa.fittracker.R;
import com.aa.fittracker.logic.DateParser;
import com.aa.fittracker.logic.Debuger;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.Training;
import com.aa.fittracker.models.TrainingEntry;
import com.aa.fittracker.models.WeightEntry;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BottomFragment extends Fragment implements FragmentCommunicator {

    int totalRestCount, goodRestCount, trainingCount, missingDataCount = 0;


    TextView extraLabel;
    TextView dayOneLabel;
    TextView dayTwoLabel;
    TextView dayThreeLabel;
    TextView dayFourLabel;
    TextView dayFiveLabel;
    TextView daySixLabel;
    TextView todayLabel;
    TextView breakdownLabel;

    TextView contentTv;
    TextView breakdownTv;
    TextView trainingCountTv;
    TextView totalRestCountTv;
    TextView goodRestTv;
    TextView missingDataTv;

    TextView day1WeightDeltaTv;
    TextView day2WeightDeltaTv;
    TextView day3WeightDeltaTv;
    TextView day4WeightDeltaTv;
    TextView day5WeightDeltaTv;
    TextView day6WeightDeltaTv;
    TextView todayWeightDeltaTv;

    ImageView dayOneIv;
    ImageView dayTwoIv;
    ImageView dayThreeIv;
    ImageView dayFourIv;
    ImageView dayFiveIv;
    ImageView daySixIv;
    ImageView todayIv;


    private View view;

    private int expanded;

    public int getExpanded() {
        return expanded;
    }

    public void setExpanded(int expanded) {
        this.expanded = expanded;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bottom, container, false);
        //move the view Out of sight
        view.animate().translationY(1600);
        expanded = -1;
        /***********Ui Initializations************/
        extraLabel = (TextView) view.findViewById(R.id.extraLabel);
        contentTv = (TextView) view.findViewById(R.id.bfContentTv);
        dayOneLabel = (TextView) view.findViewById(R.id.dayOneLabel);
        dayTwoLabel = (TextView) view.findViewById(R.id.dayTwoLabel);
        dayThreeLabel = (TextView) view.findViewById(R.id.dayThreeLabel);
        dayFourLabel = (TextView) view.findViewById(R.id.dayFourLabel);
        dayFiveLabel = (TextView) view.findViewById(R.id.dayFiveLabel);
        daySixLabel = (TextView) view.findViewById(R.id.daySixLabel);
        todayLabel = (TextView) view.findViewById(R.id.todayLabel);
        ArrayList<TextView> labelList = new ArrayList<>(Arrays.asList(extraLabel, dayOneLabel, dayTwoLabel, dayThreeLabel, dayFourLabel, dayFiveLabel, daySixLabel, todayLabel));
        breakdownLabel = (TextView) view.findViewById(R.id.breakdownLabel);
        breakdownTv = (TextView) view.findViewById(R.id.breakdownTv);
        trainingCountTv = (TextView) view.findViewById(R.id.trainingCountTv);
        totalRestCountTv = (TextView) view.findViewById(R.id.totalRestCountTv);
        goodRestTv = (TextView) view.findViewById(R.id.plannedRestTv);
        missingDataTv = (TextView) view.findViewById(R.id.missingDataCountTv);

        day1WeightDeltaTv = (TextView) view.findViewById(R.id.dayOneWeightDeltaTv);
        day2WeightDeltaTv = (TextView) view.findViewById(R.id.dayTwoWeightDeltaTv);
        day3WeightDeltaTv = (TextView) view.findViewById(R.id.dayThreeWeightDeltaTv);
        day4WeightDeltaTv = (TextView) view.findViewById(R.id.dayFourWeightDeltaTv);
        day5WeightDeltaTv = (TextView) view.findViewById(R.id.dayFiveWeightDeltaTv);
        day6WeightDeltaTv = (TextView) view.findViewById(R.id.daySixWeightDeltaTv);
        todayWeightDeltaTv = (TextView) view.findViewById(R.id.todayWeightDeltaTv);
        ArrayList<TextView> deltas = new ArrayList<>(Arrays.asList(day1WeightDeltaTv,day2WeightDeltaTv,day3WeightDeltaTv,day4WeightDeltaTv,day5WeightDeltaTv,day6WeightDeltaTv,todayWeightDeltaTv));

        

        dayOneIv = (ImageView) view.findViewById(R.id.dayOneIv);
        dayTwoIv = (ImageView) view.findViewById(R.id.dayTwoIv);
        dayThreeIv = (ImageView) view.findViewById(R.id.dayThreeIv);
        dayFourIv = (ImageView) view.findViewById(R.id.dayFourIv);
        dayFiveIv = (ImageView) view.findViewById(R.id.dayFiveIv);
        daySixIv = (ImageView) view.findViewById(R.id.daySixIv);
        todayIv = (ImageView) view.findViewById(R.id.todayIv);
        ArrayList<ImageView> imageViewList = new ArrayList<>(Arrays.asList(dayOneIv, dayTwoIv, dayThreeIv, dayFourIv, dayFiveIv, daySixIv, todayIv));

      /*  for(TextView x : labelList){
            x.setText("");
        }
        for(ImageView x : imageViewList){
            x.setImageDrawable(null);
        }   */


        // DateTv=(TextView)view.findViewById(R.id.dateTVf);
        // valueTv=(TextView)view.findViewById(R.id.dataTv);
        // optimalValueTv=(TextView)view.findViewById(R.id.optimalDataTv);
        // firstLabel=(TextView)view.findViewById(R.id.firstLabel);
        // secondLabel=(TextView)view.findViewById(R.id.secondLabel);


        /***************onClicklisteners***************/
        labelSeter();
        uiToggle();

        return view;
    }


    public void labelSeter() {
        switch (store.getUserMode()) {
            case "journal":
                extraLabel.setText("Fitness Journal");
                break;
            case "weight":
                extraLabel.setText("Weight Journal");
                break;
        }
    }

    public void translate() {
        //Close
        if (getExpanded() == 1) {
            view.animate().translationY(1600);
            switchExpanded();
        } else {    //Open
            view.animate().translationY(0);
            switchExpanded();

        }
    }

    public int diffFinder(String date) {
        int toReturn = -1;
        String trainingName = "";

        // Search for the training date
        for (TrainingEntry entry : store.getTrainingEntries()) {
            if (entry.getTraining_date().equals(date)) {
                trainingName = entry.getTraining_name(); // Assuming the date corresponds to the training name

                if (trainingName.toLowerCase(Locale.ROOT).equals("skipped day")) {
                    return 5;
                }
                if (trainingName.toLowerCase(Locale.ROOT).equals("rest day")) {
                    return 4;
                }
                break;
            }
        }

        if (!trainingName.isEmpty()) {
            // If trainingName is not empty, search for the difficulty in user trainings
            for (Training userTraining : store.getUserTrainings()) {
                Log.i("name", trainingName);
                if (userTraining.getTraining_name().equals(trainingName)) {
                    toReturn = userTraining.getTraining_difficulty();
                    break; // Once the difficulty is found, exit the loop
                }
            }

        } else {
            Log.i("difficulty: ", "No matching training name found for the given date" + trainingName);
        }

        Log.i("difficulty: ", String.valueOf(toReturn));
        return toReturn;
    }

    public int weightFinder(String date) {
        int toReturn = -1;
        String weightValue = "";
        Log.i("switch date", date);

        // Search for the training date
        for (WeightEntry entry : store.getWeightEntries()) {
            if (entry.getWeight_date().equals(date)) {
                int weight = Integer.parseInt(entry.getWeight_value()); // Assuming the date corresponds to the training name
                return weight;
            }
        }


       /* if (!trainingName.isEmpty()) {
            // If trainingName is not empty, search for the difficulty in user trainings
            for (Training userTraining : store.getUserTrainings()) {
                Log.i("name", trainingName);
                if (userTraining.getTraining_name().equals(trainingName)) {
                    toReturn = userTraining.getTraining_difficulty();
                    break; // Once the difficulty is found, exit the loop
                }
            }

        } else {
            Log.i("difficulty: ", "No matching training name found for the given date" + trainingName);
        }*/

        Log.i("difficulty: ", String.valueOf(toReturn));
        return toReturn;
    }


    public void countReseter() {
        totalRestCount = 0;
        goodRestCount = 0;
        trainingCount = 0;
        missingDataCount = 0;
    }


    public void switchExpanded() {
        if (getExpanded() == 1) {
            setExpanded(-1);
        } else if (getExpanded() == -1) {
            setExpanded(1);
        }
    }


    public static void setDateTvText(String txt) {
        //DateTv.setText(txt);
    }

    public static void setDataTvText(String txt) {
        // DataTv.setText(txt);
    }

    /**********Callbacks************/
    @Override
    public void onDateClicked(String date) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.icon_question);

        String dayInFocus = date.split("-")[2];


        ArrayList<String> dates = new ArrayList<>();
        ArrayList<TextView> deltas = new ArrayList<>(Arrays.asList(day1WeightDeltaTv,day2WeightDeltaTv,day3WeightDeltaTv,day4WeightDeltaTv,day5WeightDeltaTv,day6WeightDeltaTv,todayWeightDeltaTv));
        ArrayList<TextView> labelList = new ArrayList<>(Arrays.asList(dayOneLabel, dayTwoLabel, dayThreeLabel, dayFourLabel, dayFiveLabel, daySixLabel, todayLabel));
        ArrayList<ImageView> imageViewList = new ArrayList<>(Arrays.asList(dayOneIv, dayTwoIv, dayThreeIv, dayFourIv, dayFiveIv, daySixIv, todayIv));
        Log.i("Fragment Callback: ", date);
        contentTv.setText("No Data For This Date");


        HashMap<String, String> dtf = new HashMap<>();
        switch (store.getUserMode()) {
            case "journal":
                dtf = DateParser.last7DaysTraining();
                break;
            case "weight":
                dtf = DateParser.last7DaysWeight();
                break;
        }
        for (Map.Entry x : dtf.entrySet()) {
            Debuger.dateLog("dtf key (adding to dates)", (String) x.getKey());
            dates.add((String) x.getKey());
        }

        //Get Dates
        //dateFinder() gets the integer values of the last 7 days for the labels
        ArrayList<Integer> ta = DateParser.dateFinder();
        //sort dates in ascending order;
        ArrayList<String> sortedDates = DateParser.dateSorter(dates);
        Debuger.dateListLogger(sortedDates, "sorted dates");
        ArrayList<String> last7 = DateParser.listMaker(sortedDates, store.getDateInFocus());
        Debuger.dateListLogger(last7, "last 7");

        breakdownLabel.setText("From: " + sortedDates.get(0).replace("?", "") + " To: " + store.getDateInFocus());


        //Parse Icons And Make Count;
        for (String x : last7) {
            Log.i("x", x);
            int index = last7.indexOf(x);
            if (x.contains("?") || x.contains("dtf")) {
                Log.i("index", String.valueOf(index));
                imageViewList.get(index).setImageResource(R.drawable.icon_question);
                deltas.get(index).setBackgroundResource(R.drawable.icon_question);
                deltas.get(index).setText("");
                missingDataCount += 1;
                Debuger.dateLog("missingDataCount", String.valueOf(missingDataCount));

            } else {
                switch (store.getUserMode()) {
                    case "journal":
                        int diff = diffFinder(x);
                        switch (diff) {
                            case 1:
                                imageViewList.get(index).setImageResource(R.drawable.icon_easy_training);
                                trainingCount++;
                                Debuger.dateLog("trainingCount", String.valueOf(trainingCount));
                                break;
                            case 2:
                                imageViewList.get(index).setImageResource(R.drawable.icon_medium_training);
                                trainingCount++;
                                Debuger.dateLog("trainingCount", String.valueOf(trainingCount));
                                break;
                            case 3:
                                imageViewList.get(index).setImageResource(R.drawable.icon_hard_training);
                                trainingCount++;
                                Debuger.dateLog("trainingCount", String.valueOf(trainingCount));
                                break;
                            case 4:
                                imageViewList.get(index).setImageResource(R.drawable.icon_good_restt);
                                totalRestCount++;
                                goodRestCount++;
                                Debuger.dateLog("totalRestCount", String.valueOf(totalRestCount));
                                Debuger.dateLog("goodRestCount", String.valueOf(goodRestCount));
                                break;
                            case 5:
                                imageViewList.get(index).setImageResource(R.drawable.icon_bad_restt);
                                totalRestCount++;
                                Debuger.dateLog("totalRestCount", String.valueOf(totalRestCount));

                                break;
                        }


                        break;
                    case "weight":
                        double weight = weightFinder(x);
                        Log.i("Weight on date: ", x + " : " + weight);
                        double idealWeight = Double.parseDouble(store.getUserWeightKg());
                        double startWeight = Double.parseDouble(store.getUserStartWeight());

                        double delta = startWeight-weight;

                        boolean isIdealWeight = weight == idealWeight;
                        boolean lostWeight = weight - startWeight <= 0;
                        boolean gainedWeight = weight - startWeight >= 0;





                        /* Log.i("switch weightandideal", weight + " " + idealWeight);
                        Log.i("switch weight>startwright",  String.valueOf(weight>startWeight));
                        Log.i("switch weight<startweight",  String.valueOf(weight>startWeight));
                        Log.i("switch weightgoal", store.getUserWeightGoal());*/


                        if (isIdealWeight) {
                        deltas.get(index).setText("$") ;
                        deltas.get(index).setBackgroundResource(0);
                        deltas.get(index).setTextColor(getResources().getColor(R.color.mid_yellow));
                        } else {
                            Log.i("switch activated: ", "x");
                            switch (store.getUserWeightGoal()) {
                                case "+":
                                    if (gainedWeight) {
                                        if(delta<0){
                                           deltas.get(index).setText("+"+String.format("%.1f", Math.abs(delta))) ;
                                        }else {
                                            deltas.get(index).setText("-"+String.format("%.1f", delta));
                                        }
                                        deltas.get(index).setBackgroundResource(0);
                                        deltas.get(index).setTextColor(getResources().getColor(R.color.easy_green));
                                    } else if (lostWeight) {
                                        if(delta<0){
                                            deltas.get(index).setText("+"+String.format("%.1f", Math.abs(delta))) ;
                                        }else {
                                            deltas.get(index).setText("-"+String.format("%.1f", delta));
                                        }
                                        deltas.get(index).setBackgroundResource(0);
                                        deltas.get(index).setTextColor(getResources().getColor(R.color.hard_red));
                                    }
                                    break;
                                case "-":
                                    if (gainedWeight) {
                                        if(delta<0){
                                            deltas.get(index).setText("+"+String.format("%.1f", Math.abs(delta))) ;
                                        }else {
                                            deltas.get(index).setText("-"+String.format("%.1f", delta));
                                        }
                                        deltas.get(index).setBackgroundResource(0);
                                        deltas.get(index).setTextColor(getResources().getColor(R.color.hard_red));
                                    } else if (lostWeight) {
                                        if(delta<0){
                                            deltas.get(index).setText("+"+String.format("%.1f", Math.abs(delta))) ;
                                        }else {
                                            deltas.get(index).setText("-"+String.format("%.1f", delta));
                                        }
                                        deltas.get(index).setBackgroundResource(0);
                                        deltas.get(index).setTextColor(getResources().getColor(R.color.easy_green));
                                    }
                                    break;
                            }
                        }


                }
            }

        }


        //set label text
        if (ta.size() > 1) {
            int EndIndex = 6;
            for (TextView x : labelList) {
                x.setText(String.valueOf(ta.get(EndIndex)) + ".");
                EndIndex--;
            }
        }
        if (store.getUserMode().equals("journal")) {
            //set the ui elements to the coutner values
            breakdownSetter();
            //reset the week counters
            countReseter();
        }


    }

    @Override
    public void onMatchFound(WeightEntry x) {
        Log.i("Fragment Callback: ", x.toString());

    }

    @Override
    public void onTrainingMatchFound(TrainingEntry x) {
        Log.i("Fragment Callback: ", x.getTraining_name());
        for (Training y : store.getUserTrainings()) {
            if (y.getTraining_name().equals(x.getTraining_name())) {
                contentTv.setText(y.getTraining_desc());
            }
        }
        if (x.getTraining_name().toLowerCase(Locale.ROOT).equals("rest day")) {
            contentTv.setText("Rest Day");
        } else if (x.getTraining_name().toLowerCase(Locale.ROOT).equals("skipped day")) {
            contentTv.setText("Skipped Day");
        }
    }

    public void breakdownSetter() {
        if (store.getUserMode().equals("journal")) {
            trainingCountTv.setText("Trained: " + trainingCount + " Times.");
            totalRestCountTv.setText("Rested: " + totalRestCount + " Times");
            goodRestTv.setText(goodRestCount + " Of Which Were Planned");
            missingDataTv.setText(missingDataCount + " Days.");
        }
    }

    public void uiToggle() {
        switch (store.getUserMode()) {
            case "journal":
                //toggle visibility
                dayOneIv.setVisibility(View.VISIBLE);
                dayTwoIv.setVisibility(View.VISIBLE);
                dayThreeIv.setVisibility(View.VISIBLE);
                dayFourIv.setVisibility(View.VISIBLE);
                dayFiveIv.setVisibility(View.VISIBLE);
                daySixIv.setVisibility(View.VISIBLE);
                todayIv.setVisibility(View.VISIBLE);

                day1WeightDeltaTv.setVisibility(View.GONE);
                day2WeightDeltaTv.setVisibility(View.GONE);
                day3WeightDeltaTv.setVisibility(View.GONE);
                day4WeightDeltaTv.setVisibility(View.GONE);
                day5WeightDeltaTv.setVisibility(View.GONE);
                day6WeightDeltaTv.setVisibility(View.GONE);
                todayWeightDeltaTv.setVisibility(View.GONE);

                //set the text
                trainingCountTv.setText("Trained: " + trainingCount + " Times.");
                totalRestCountTv.setText("Rested: " + totalRestCount + " Times");
                goodRestTv.setText(goodRestCount + " Of Which Were Planned");
                missingDataTv.setText(missingDataCount + " Days.");
                break;
            case "weight":
                //toggle visibility
                dayOneIv.setVisibility(View.INVISIBLE);
                dayTwoIv.setVisibility(View.INVISIBLE);
                dayThreeIv.setVisibility(View.INVISIBLE);
                dayFourIv.setVisibility(View.INVISIBLE);
                dayFiveIv.setVisibility(View.INVISIBLE);
                daySixIv.setVisibility(View.INVISIBLE);
                todayIv.setVisibility(View.INVISIBLE);

                day1WeightDeltaTv.setVisibility(View.VISIBLE);
                day2WeightDeltaTv.setVisibility(View.VISIBLE);
                day3WeightDeltaTv.setVisibility(View.VISIBLE);
                day4WeightDeltaTv.setVisibility(View.VISIBLE);
                day5WeightDeltaTv.setVisibility(View.VISIBLE);
                day6WeightDeltaTv.setVisibility(View.VISIBLE);
                todayWeightDeltaTv.setVisibility(View.VISIBLE);


                break;
        }
    }

}