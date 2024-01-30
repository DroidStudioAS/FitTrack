package com.aa.fittracker;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.FillEventHistory;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aa.fittracker.dialog.DeleteDialog;
import com.aa.fittracker.dialog.InfoDialog;
import com.aa.fittracker.dialog.InputDialog;
import com.aa.fittracker.dialog.MonthlyBreakdownDialog;
import com.aa.fittracker.dialog.promptDialog;
import com.aa.fittracker.fragments.BottomFragment;
import com.aa.fittracker.fragments.calendarFragment;
import com.aa.fittracker.logic.DateParser;
import com.aa.fittracker.logic.store;
import com.aa.fittracker.models.TrainingEntry;
import com.aa.fittracker.models.WeightEntry;
import com.aa.fittracker.network.networkHelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import kotlin.reflect.KFunction;
import okhttp3.OkHttpClient;

public class calendarActivity extends AppCompatActivity implements OnDateClickListener, OnInfoInputListener ,GestureDetector.OnGestureListener {

    int clickCount;

    ConstraintLayout root;

    TextView dateTV;
    TextView infoLabel;
    TextView optimalLabel;
    TextView infoTv;
    TextView optimalTv;

    ImageView expandTrigger;

    Button missingInfoButton;
    Button missingOptimalButton;
    Button deleteEntryTrigger;

    Context context;


    OkHttpClient client;
    Gson gson;

    BottomFragment bf;
    FragmentCommunicator fc;
    GestureDetector gestureDetector;


    boolean isExpanded = false;
    boolean isDeleteVisible, isMissingVisible = false;
    boolean clickedMaybeLater = false;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mb:
                //LIST TO COUNT:
                 ArrayList<String> monthBreakdown = DateParser.monthBreakdown();
                 HashMap<String,String> monthCount = DateParser.monthBreakdownCounter(monthBreakdown);
                 for(Map.Entry<String,String> entry : monthCount.entrySet()){
                     Log.i("mbCount",entry.getKey() + entry.getValue());
                 }
                 if(monthCount.size()!=0) {
                     MonthlyBreakdownDialog mbd = new MonthlyBreakdownDialog(this);
                     mbd.setCancelable(false);
                     mbd.setToSet(monthCount);
                     mbd.show();
                 }

                 if(monthBreakdown.size()==0){
                     //no date in focus
                 }
                 //

                return true;
            case R.id.help:
                InfoDialog infoDialog = new InfoDialog(this);
                switch (store.getUserMode()){
                    case "journal":
                        infoDialog.setOpenedFrom("journal");
                        break;
                    case "weight":
                        infoDialog.setOpenedFrom("weight");
                }
                infoDialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        clickCount = 0;

        /*****Action Bar*******/
        ActionBar ab = getSupportActionBar();
        if(ab!=null){
            ab.setTitle("Fit Journal");
        }

        /*******************Ui Initializations**********************/
        infoLabel = (TextView) findViewById(R.id.infoLabel);
        optimalLabel = (TextView) findViewById(R.id.optimalLabel);
        infoTv = (TextView) findViewById(R.id.infoTv);
        optimalTv = (TextView) findViewById(R.id.optimalTv);
        dateTV = (TextView) findViewById(R.id.dateTV);

        missingInfoButton = (Button) findViewById(R.id.missingInfoButton);
        missingOptimalButton = (Button) findViewById(R.id.missingOptimalButton);
        deleteEntryTrigger = (Button) findViewById(R.id.deleteEntryTrigger);
        expandTrigger = (ImageView) findViewById(R.id.expandTrigger);
        deleteEntryTrigger.setVisibility(View.INVISIBLE);
        bf = (BottomFragment) getSupportFragmentManager().findFragmentById(R.id.bf);
        fc = bf;

        root=(ConstraintLayout)findViewById(R.id.root);


        /**********************Neccesary**********************/
        client = new OkHttpClient();
        gson = new Gson();
        context = getApplicationContext();
        gestureDetector=new GestureDetector(this, this);
        /*********************User mode determiner**********************/
        Intent incoming = getIntent();
        switch (store.getUserMode()) {
            case "journal":
                labelSeter("Trained:", "Rest Day:");
                break;
            case "weight":
                labelSeter("Weight:", "Optimal:");
                optimalTv.setText(store.getUserWeightKg());
                break;
            case "cals":
                labelSeter("Intake:", "Allowed:");
                break;
        }
        /************************OnClickListeners***************************/

        expandTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandLogic();
            }
        });
        missingInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!clickedMaybeLater && store.getUserTrainings().isEmpty()) {
                    promptDialog pd = new promptDialog(calendarActivity.this);
                    pd.show();
                    pd.noTrainingsPrompt(1);
                    clickedMaybeLater=true;
                    return;
                }

                    InputDialog inputDialog = new InputDialog(calendarActivity.this, calendarActivity.this);
                    inputDialog.show();
                    missingInfoButton.setVisibility(View.GONE);






            }
        });
        deleteEntryTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /********Execution switch********/
                DeleteDialog deleteDialog = new DeleteDialog(calendarActivity.this);
                deleteDialog.show();


            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /****************Helper functions begin*******************/
    //removes resutls from frontend list for instant refresh
    public static void listRemover(int i) {
        //i=1-TRAININGSERVICE
        //I=2-WEIGHTSERVICE
        switch (i) {
            case 1:
                Log.i("size before", String.valueOf(store.getTrainingEntries().size()));
                store.removeFromTrainingEntries(store.getDateInFocus());
                Log.i("size after", String.valueOf(store.getTrainingEntries().size()));
                break;
            case 2:
                Log.i("size before", String.valueOf(store.getWeightEntries().size()));
                store.removeFromWeightEntries(store.getDateInFocus());
                Log.i("size after", String.valueOf(store.getWeightEntries().size()));
                break;
        }
    }

    public void labelSeter(String infoString, String optimalString) {
        infoLabel.setText(infoString);
        optimalLabel.setText(optimalString);
        if(store.getUserMode().equals("journal")){
            optimalLabel.setVisibility(View.GONE);
        }else{
            optimalLabel.setVisibility(View.VISIBLE);
        }
    }
    public void expandLogic() {
        if(store.getDateInFocus().equals("")){
           promptDialog pd = new promptDialog(this);
           pd.show();
           pd.noDateInFoucsPrompt();
           return;
        }
        bf.translate();
        if (clickCount % 2 == 0) {
            //open
            expandTrigger.animate().rotation(180);
            if (deleteEntryTrigger.getVisibility() == View.VISIBLE) {
                isDeleteVisible = true;
            }
            if (missingInfoButton.getVisibility() == View.VISIBLE) {
                isMissingVisible = true;
            }

            if (isDeleteVisible) {
                deleteEntryTrigger.setVisibility(View.INVISIBLE);
            }
            if (isMissingVisible) {
                missingInfoButton.setVisibility(View.INVISIBLE);
            }

            isExpanded = true;

        } else {
            //close
            expandTrigger.animate().rotation(0);

            if (isDeleteVisible) {
                deleteEntryTrigger.setVisibility(View.VISIBLE);
                isDeleteVisible = false;
            }
            if (isMissingVisible) {
                missingInfoButton.setVisibility(View.VISIBLE);
                isMissingVisible = false;
            }
            isExpanded = false;
        }

        clickCount++;
    }
    /****************Helper functions end*******************/
    /*******************Callbacks received********************/
    @Override
    public void onDateClicked(String date) {
        dateTV.setText(date);
        infoTv.setText("");
        if (!isExpanded) {
            missingInfoButton.setVisibility(View.VISIBLE);
            deleteEntryTrigger.setVisibility(View.INVISIBLE);
        } else {
            isDeleteVisible = false;
            isMissingVisible = true;
        }
        Log.i("ONDATECLICKED", "...");
        IndexActivity.Logger();

        fc.onDateClicked(date);

        store.setCurrentUserWeight("-1");
        Log.i("current: " , store.getCurrentUserWeight());
        Log.i("dif",store.getDateInFocus());
    }



    /************** !very important this callback happens after onDateClicked! **************/
    @Override
    public void onMatchFound(WeightEntry x) {
        if (store.getUserMode().equals("weight")) {
            Log.i("Weight Match Found", x.getWeight_value() + " " + x.getWeight_date());
            if (!isExpanded) {
                missingInfoButton.setVisibility(View.GONE);
                deleteEntryTrigger.setVisibility(View.VISIBLE);
            } else {
                isDeleteVisible = true;
                isMissingVisible = false;
            }

            infoTv.setText(x.getWeight_value());
            store.setCurrentUserWeight(x.getWeight_value());
            Log.i("current: " , store.getCurrentUserWeight());


            IndexActivity.Logger();

            fc.onMatchFound(x);
        }
    }

    @Override
    public void onTrainingMatchFound(TrainingEntry x) {
        if (store.getUserMode().equals("journal")) {
            Log.i("Training Match Found", x.getTraining_name() + " " + x.getTraining_date());
            if (!isExpanded) {
                missingInfoButton.setVisibility(View.GONE);
                deleteEntryTrigger.setVisibility(View.VISIBLE);
            } else {
                isDeleteVisible = true;
                isMissingVisible = false;
            }

            infoTv.setText(x.getTraining_name());
            IndexActivity.Logger();

            fc.onTrainingMatchFound(x);
        }
    }


    @Override
    public void onWeightInput(WeightEntry x) {
        Log.i("weignt input", x.getWeight_date() + x.getWeight_value());


        recreate();
    }

    @Override
    public void onTrainingInput(TrainingEntry x) {
        Log.i("training input", x.getTraining_date() + x.getTraining_name());


        recreate();
    }

    @Override
    public void onDeleted() {
        recreate();
    }

    @Override
    public boolean onDown(@NonNull MotionEvent e) {
        Log.i("gdactive: onDown", String.valueOf(e.getEventTime()));
        return false;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
        float deltaY = e2.getY() - e1.getY();
        if (Math.abs(deltaY) > 50) { // Adjust this threshold as needed
            if (deltaY > 0) {
                // Swipe down
                Log.i("Swipe down", String.valueOf(e1.getEventTime()));
                expandLogic();
            } else {
                // Swipe up
                Log.i("Swipe up", String.valueOf(e1.getEventTime()));
                expandLogic();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent e) {

    }

    @Override
    public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}



