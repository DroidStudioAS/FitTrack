package com.aa.fittracker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aa.fittracker.R;

public class MonthlyBreakdownDialog extends Dialog {
    TextView titleTv;
    TextView breakdownTv1;
    TextView breakdownTv2;
    TextView breakdownTv3;
    TextView breakdownTv4;
    TextView breakdownTv5;


    public MonthlyBreakdownDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mb);

        titleTv = (TextView) findViewById(R.id.breakdownTitle);
        breakdownTv1 =(TextView) findViewById(R.id.breakdwonTv1);
        breakdownTv2 =(TextView) findViewById(R.id.breakdwonTv2);
        breakdownTv3 =(TextView) findViewById(R.id.breakdwonTv3);
        breakdownTv4 =(TextView) findViewById(R.id.breakdwonTv4);
        breakdownTv5 =(TextView) findViewById(R.id.breakdwonTv5);
    }
}
