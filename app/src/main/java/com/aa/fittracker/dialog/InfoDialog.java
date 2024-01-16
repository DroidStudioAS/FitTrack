package com.aa.fittracker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;

import com.aa.fittracker.R;

import org.w3c.dom.Text;

public class InfoDialog extends Dialog {
    String openedFrom = "";

    TextView infoTitle;
    TextView trainingExp;
    TextView tJournalExp;
    TextView wJournalExp;

    TextView expTarget;

    Group trainingGroup;
    Group tjGroup;
    Group wjGroup;

    ImageView diss;


    public InfoDialog(@NonNull Context context) {
        super(context);
    }

    public void setOpenedFrom(String openedFrom) {
        this.openedFrom = openedFrom;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_info);

        trainingExp = (TextView) findViewById(R.id.trainingsExpButton);
        tJournalExp = (TextView) findViewById(R.id.trainingJournalExpButton);
        wJournalExp = (TextView) findViewById(R.id.weightJournalExpButton);

        trainingGroup = (Group) findViewById(R.id.trainingGroup);
        tjGroup = (Group) findViewById(R.id.tjGroup);
        wjGroup = (Group) findViewById(R.id.wjGroup);

        expTarget = (TextView) findViewById(R.id.expTarget);
        diss=(ImageView)findViewById(R.id.diss);
        diss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        trainingExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openedFrom = "T";
                focusOnTitle();
                setExp();
                trainingGroup.setVisibility(View.VISIBLE);
                tjGroup.setVisibility(View.INVISIBLE);
                wjGroup.setVisibility(View.INVISIBLE);
            }
        });
        tJournalExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openedFrom = "TJ";
                focusOnTitle();
                setExp();
                trainingGroup.setVisibility(View.INVISIBLE);
                tjGroup.setVisibility(View.VISIBLE);
                wjGroup.setVisibility(View.INVISIBLE);
            }
        });
        wJournalExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openedFrom = "WJ";
                focusOnTitle();
                setExp();
                trainingGroup.setVisibility(View.INVISIBLE);
                tjGroup.setVisibility(View.INVISIBLE);
                wjGroup.setVisibility(View.VISIBLE);
            }
        });

    }

    public void setDialog() {

    }

    public void setExp() {
        switch (openedFrom) {
            case "T":
                expTarget.setText("The Training Page Is Where You Can Add You Training Routines.\n\nTo Add A Training Just Go To Add Trainings And Fill The Necessary data.\n\nIn The App The Following Colors, Represent The Routine Difficulty :");
                break;
            case "TJ":
                expTarget.setText("The Training Journal Is Where You Can Log Your Exercise History.\n\nJust Pick A Date, And Tell The App What You Did On That Day!.");
                break;
            case "WJ":
                expTarget.setText("The Weight Journal Is Going To Follow Your Weight For You.\n\nJust Make Sure To Enter Your Weight On The Day, And The App Will Do The Rest!.");
                break;
        }
    }

    public void focusOnTitle()

    {
        switch (openedFrom) {
            case "T":
                trainingExp.setTextSize(20);
                trainingExp.setTypeface(null, Typeface.BOLD);

                tJournalExp.setTextSize(16);
                tJournalExp.setTypeface(null, Typeface.NORMAL);

                wJournalExp.setTextSize(16);
                wJournalExp.setTypeface(null, Typeface.NORMAL);

                break;
            case "TJ":
                trainingExp.setTextSize(16);
                trainingExp.setTypeface(null, Typeface.NORMAL);

                tJournalExp.setTextSize(20);
                tJournalExp.setTypeface(null, Typeface.BOLD);

                wJournalExp.setTextSize(16);
                wJournalExp.setTypeface(null, Typeface.NORMAL);

                break;
            case "WJ":
                trainingExp.setTextSize(16);
                trainingExp.setTypeface(null, Typeface.NORMAL);

                tJournalExp.setTextSize(16);
                tJournalExp.setTypeface(null, Typeface.NORMAL);

                wJournalExp.setTextSize(20);
                wJournalExp.setTypeface(null, Typeface.BOLD);

                break;
        }
    }
}

