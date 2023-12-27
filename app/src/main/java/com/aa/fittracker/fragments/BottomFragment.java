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

public class BottomFragment extends Fragment {

    ImageView expandTrigger;

   // static TextView DataTv;
   // static TextView DateTv;
   // TextView firstLabel;
   // TextView secondLabel;

    private View view;
    private BottomSheetBehavior bottomSheetBehavior;

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
        //view.animate().translationY(300);
      //  DateTv=(TextView)view.findViewById(R.id.dateTvF);
        expanded=-1;
        /***********Ui Initializations************/
        expandTrigger=(ImageView) view.findViewById(R.id.expandTrigger);
        expandTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translate();
            }
        });

     //   firstLabel=(TextView)view.findViewById(R.id.firstLabel);
      //  secondLabel=(TextView)view.findViewById(R.id.secondLabel);
      //  DataTv=(TextView)view.findViewById(R.id.dataTv);
        //set the labels
       /* switch (store.getUserMode()) {
            case "journal":
                labelSeter("Trained:", "Rest Day:");
                break;
            case "weight":
                labelSeter("Weight:","Optimal:");
                //optimalTv.setText(store.getUserWeightKg());
                break;
            case "cals":
                labelSeter("Intake:","Allowed:");
                break;
        }       */

        return view;
    }




    /*public void labelSeter(String infoString,String optimalString){
        firstLabel.setText(infoString);
        secondLabel.setText(optimalString);
    }  */

    public void translate(){
        if(getExpanded()==1) {
            view.animate().translationY(0);
            switchExpanded();
        }else{
            view.animate().translationY(-600);
            switchExpanded();

        }
    }

    public void switchExpanded(){
       if(getExpanded()==1){
           setExpanded(-1);
       }else if(getExpanded()==-1){
           setExpanded(1);
       }
    }

    public static void setDateTvText(String txt){
        //DateTv.setText(txt);
    }
    public static void setDataTvText(String txt){
       // DataTv.setText(txt);
    }

}