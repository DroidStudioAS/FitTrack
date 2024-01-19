package com.aa.fittracker.dialog;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aa.fittracker.R;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.presentation.CalendarAdapter;

import java.time.Month;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class MonthlyBreakdownDialog extends Dialog {
    public HashMap<String,String> toSet = new HashMap<>();

    public void setToSet(HashMap<String, String> toSet) {
        this.toSet = toSet;
    }

    TextView breakdownTitleTv;
    TextView yearTv;
    TextView breakdownTv0;
    TextView breakdownTv1;
    TextView breakdownTv2;
    TextView breakdownTv3;
    TextView breakdownTv4;
    TextView breakdownTv5;

    ImageView closeDialog;


    public MonthlyBreakdownDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mb);

        yearTv=(TextView)findViewById(R.id.Year);
        breakdownTitleTv = (TextView) findViewById(R.id.breakdownTitleTv);
        breakdownTv0 = (TextView) findViewById(R.id.breakdownTv0);
        breakdownTv1 =(TextView) findViewById(R.id.breakdwonTv1);
        breakdownTv2 =(TextView) findViewById(R.id.breakdwonTv2);
        breakdownTv3 =(TextView) findViewById(R.id.breakdwonTv3);
        breakdownTv4 =(TextView) findViewById(R.id.breakdwonTv4);
        breakdownTv5 =(TextView) findViewById(R.id.breakdwonTv5);
        closeDialog = (ImageView) findViewById(R.id.closeDialog);

        toggleUiElements();
        setDialog();

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    public void setDialog(){
        String year = store.getDateInFocus().split("-")[0];
        Month month = Month.of(Integer.parseInt(store.getDateInFocus().split("-")[1]));
        Character toCap = Character.toUpperCase(month.toString().charAt(0));
        String title = toCap + month.toString().substring(1,month.toString().length()).toLowerCase(Locale.ROOT) +" " + "Breakdown";
        Log.i("title", title);
        Log.i("target", String.valueOf(breakdownTitleTv));
        yearTv.setText(year);
        breakdownTitleTv.setText(title);
        for(Map.Entry<String,String> map : toSet.entrySet()){
            CharSequence value = map.getValue();
            Spannable spannable = new SpannableString(value);

            // Apply UnderlineSpan
            spannable.setSpan(new UnderlineSpan(), 0, value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            //Set the data for the user
            if (map.getKey().contains("missing")) {
                breakdownTv0.setText("Missing info for: ");
                breakdownTv0.append(spannable);
                breakdownTv0.append(" days");
            } else if (map.getKey().contains("eazy")) {
                breakdownTv1.setText("Did: ");
                breakdownTv1.append(spannable);
                breakdownTv1.append(" Easy Trainings");
            } else if (map.getKey().contains("mid")) {
                breakdownTv2.setText("Did: ");
                breakdownTv2.append(spannable);
                breakdownTv2.append(" Medium Trainings");
            } else if (map.getKey().contains("hard")) {
                breakdownTv3.setText("Did: ");
                breakdownTv3.append(spannable);
                breakdownTv3.append(" Hard Trainings");
            } else if (map.getKey().contains("total")) {
                breakdownTv4.setText("Rested A Total Of: ");
                breakdownTv4.append(spannable);
                breakdownTv4.append(" Days");
            } else if (map.getKey().contains("planned")) {
                breakdownTv5.setText(spannable);
                breakdownTv5.append(" Of Which Were Planned");
            } else if (map.getKey().contains("perfect")) {
                breakdownTv1.setText("You Were At Your Optimal Weight For ");
                breakdownTv1.append(spannable);
                breakdownTv1.append(" Days");
            } else if (map.getKey().contains("good")) {
                breakdownTv2.setText("You Made Good Weight Changes: ");
                breakdownTv2.append(spannable);
                breakdownTv2.append(" Times");
            } else if (map.getKey().contains("bad")) {
                breakdownTv3.setText("You Made Bad Weight Changes: ");
                breakdownTv3.append(spannable);
                breakdownTv3.append(" Times");
            }
        }



    }
    public void toggleUiElements (){
        switch (store.getUserMode()){
            case "journal":
                breakdownTv1.setVisibility(View.VISIBLE);
                breakdownTv2.setVisibility(View.VISIBLE);
                breakdownTv3.setVisibility(View.VISIBLE);
                breakdownTv4.setVisibility(View.VISIBLE);
                breakdownTv5.setVisibility(View.VISIBLE);
                break;
            case "weight":
                breakdownTv1.setVisibility(View.VISIBLE);
                breakdownTv2.setVisibility(View.VISIBLE);
                breakdownTv3.setVisibility(View.VISIBLE);
                breakdownTv4.setVisibility(View.INVISIBLE);
                breakdownTv5.setVisibility(View.INVISIBLE);
                break;
        }
    }
}
