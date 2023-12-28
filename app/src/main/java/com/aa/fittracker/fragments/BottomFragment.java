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
import androidx.fragment.app.Fragment;

import com.aa.fittracker.FragmentCommunicator;
import com.aa.fittracker.OnDateClickListener;
import com.aa.fittracker.R;
import com.aa.fittracker.logic.DateParser;
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
        view.animate().translationY(1500);
        expanded = -1;
        /***********Ui Initializations************/
        extraLabel = (TextView) view.findViewById(R.id.extraLabel);
        contentTv=(TextView)view.findViewById(R.id.bfContentTv);
        dayOneLabel = (TextView) view.findViewById(R.id.dayOneLabel);
        dayTwoLabel = (TextView) view.findViewById(R.id.dayTwoLabel);
        dayThreeLabel = (TextView) view.findViewById(R.id.dayThreeLabel);
        dayFourLabel = (TextView) view.findViewById(R.id.dayFourLabel);
        dayFiveLabel = (TextView) view.findViewById(R.id.dayFiveLabel);
        daySixLabel = (TextView) view.findViewById(R.id.daySixLabel);
        todayLabel = (TextView) view.findViewById(R.id.todayLabel);
        ArrayList<TextView> labelList = new ArrayList<>(Arrays.asList(extraLabel, dayOneLabel, dayTwoLabel, dayThreeLabel, dayFourLabel, dayFiveLabel, daySixLabel, todayLabel));
        breakdownLabel = (TextView) view.findViewById(R.id.breakdownLabel);
        breakdownTv=(TextView) view.findViewById(R.id.breakdownTv);
                //
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
        labelSeter(store.getUserMode());

        return view;
    }


    public void labelSeter(String infoString) {
        extraLabel.setText(infoString);
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

                if(trainingName.toLowerCase(Locale.ROOT).equals("skipped day")){
                    return 5;
                }
                if(trainingName.toLowerCase(Locale.ROOT).equals("rest day")){
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
            Log.i("difficulty: ", "No matching training name found for the given date");
        }

        Log.i("difficulty: ", String.valueOf(toReturn));
        return toReturn;
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

        String dayInFocus = date.split("-")[2];

        ArrayList<String> dates = new ArrayList<>();
        ArrayList<TextView> labelList = new ArrayList<>(Arrays.asList(dayOneLabel, dayTwoLabel, dayThreeLabel, dayFourLabel, dayFiveLabel, daySixLabel, todayLabel));
        ArrayList<ImageView> imageViewList = new ArrayList<>(Arrays.asList(dayOneIv, dayTwoIv, dayThreeIv, dayFourIv, dayFiveIv, daySixIv, todayIv));
        Log.i("Fragment Callback: ", date);
        contentTv.setText("No Data For This Date");
        ArrayList<Integer> ta = DateParser.dateFinder();
        //Get Dates
        HashMap<String,String> dtf = DateParser.last7DaysTraining();
        for(Map.Entry x : dtf.entrySet()){
          Log.i("map:", x.getKey() +""+ x.getValue()) ;
          dates.add((String) x.getKey());
        }
        //sort dates in ascending order;
        ArrayList<String> sortedDates = DateParser.dateSorter(dates);

      /*  for(int x : ta){
            Log.i("Sorted day", String.valueOf(x));
        }
        Log.i("Sorted Start", "---");
        for(String x : sortedDates){
            Log.i("sorted", x);
        }
        Log.i("Sorted End", "---");
        Log.i("Sorted Size", String.valueOf(dtf.size())) ;

       */
        ArrayList<String> last7 = DateParser.listMaker(sortedDates,store.getDateInFocus());

        for(String x : last7){
            int index = last7.indexOf(x);
            if(x.contains("?")){
                Log.i("index", String.valueOf(index));
                imageViewList.get(index).setImageResource(R.drawable.icon_question);
            }else{
                imageViewList.get(index).setImageResource(R.drawable.icon_good_rest);
                int diff = diffFinder(x);
                switch (diff){
                    case 1:
                        imageViewList.get(index).setImageResource(R.drawable.icon_easy_training);
                        break;
                    case 2:
                        imageViewList.get(index).setImageResource(R.drawable.icon_mid_training);
                        break;
                    case 3:
                        imageViewList.get(index).setImageResource(R.drawable.icon_hard_tr);
                        break;
                    case 4:
                        imageViewList.get(index).setImageResource(R.drawable.icon_good_restt);
                        break;
                    case 5:
                        imageViewList.get(index).setImageResource(R.drawable.icon_bad_restt);
                        break;
                }
            }
        }
        if(ta.size()>1){
        int EndIndex = 6;

        for(TextView x : labelList) {
            x.setText(String.valueOf(ta.get(EndIndex)) + ".");
            EndIndex--;
        }






        }


    }

    @Override
    public void onMatchFound(WeightEntry x) {
      Log.i("Fragment Callback: ", x.toString());

    }

    @Override
    public void onTrainingMatchFound(TrainingEntry x) {
     Log.i("Fragment Callback: ", x.getTraining_name());
     for(Training y : store.getUserTrainings()){
         if(y.getTraining_name().equals(x.getTraining_name())){
            contentTv.setText(y.getTraining_desc());
         }
     }
     if(x.getTraining_name().toLowerCase(Locale.ROOT).equals("rest day")){
         contentTv.setText("Rest Day");
     }else if(x.getTraining_name().toLowerCase(Locale.ROOT).equals("skipped day")){
         contentTv.setText("Skipped Day");
     }
    }


}