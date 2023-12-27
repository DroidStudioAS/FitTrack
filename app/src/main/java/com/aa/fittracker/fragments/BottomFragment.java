package com.aa.fittracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aa.fittracker.R;
import com.aa.fittracker.logic.store;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;

public class BottomFragment extends Fragment {

    TextView contentTv;

    TextView extraLabel;
    TextView dayOneLabel;
    TextView dayTwoLabel;
    TextView dayThreeLabel;
    TextView dayFourLabel;
    TextView dayFiveLabel;
    TextView daySixLabel;
    TextView todayLabel;

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
        view.animate().translationY(1100);
        expanded = -1;
        /***********Ui Initializations************/
        extraLabel = (TextView) view.findViewById(R.id.extraLabel);
        dayOneLabel = (TextView) view.findViewById(R.id.dayOneLabel);
        dayTwoLabel = (TextView) view.findViewById(R.id.dayTwoLabel);
        dayThreeLabel = (TextView) view.findViewById(R.id.dayThreeLabel);
        dayFourLabel = (TextView) view.findViewById(R.id.dayFourLabel);
        dayFiveLabel = (TextView) view.findViewById(R.id.dayFiveLabel);
        daySixLabel = (TextView) view.findViewById(R.id.daySixLabel);
        todayLabel = (TextView) view.findViewById(R.id.todayLabel);
        ArrayList<TextView> labelList = new ArrayList<>(Arrays.asList(extraLabel, dayOneLabel, dayTwoLabel, dayThreeLabel, dayFourLabel, dayFiveLabel, daySixLabel, todayLabel));

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
            view.animate().translationY(1100);
            switchExpanded();
        } else {    //Open
            view.animate().translationY(0);
            switchExpanded();

        }
    }

    public void UiEnabler(int status) {
        switch (status) {
            //disable
            case -1:

                break;
            //enable
            case 1:

                break;
        }
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

}