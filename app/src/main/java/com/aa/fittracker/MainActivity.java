package com.aa.fittracker;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.aa.fittracker.dialog.InfoDialog;
import com.aa.fittracker.logic.NotificationReceiver;
import com.aa.fittracker.network.FirebaseMessagingServ;
import com.aa.fittracker.network.networkHelper;

import com.aa.fittracker.presentation.pagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
  /*************UI ELEMENTS DECLERATION*************/
   private ViewPager vPager;
   private pagerAdapter adapter;
   private TabLayout tl;
   private Button trigger;

   NotificationReceiver nr = new NotificationReceiver();
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.infor:
                InfoDialog infoDialog = new InfoDialog(this);
                infoDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.basic_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*************UI ELEMENTS DECLERATION*************/




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        if(!networkHelper.isNetworkConnected(getApplicationContext())){
            //user not connected to internet


        };



        // Set up the AlarmManager to trigger the BroadcastReceiver every 24 hours
        long intervalMillis = 24 * 60 * 60 * 500; // 24 hours in milliseconds
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntentFirst = PendingIntent.getBroadcast(this, 0, intent, 0);
        // Create PendingIntent with request code 1 for the second notification
        PendingIntent pendingIntentSecond = PendingIntent.getBroadcast(this, 1, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                pendingIntentFirst);


        // Set the repeating alarm to trigger every 12 hours
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + intervalMillis,
                intervalMillis, pendingIntentSecond);

        //initializing
        vPager = (ViewPager) findViewById(R.id.vPager);
        tl=(TabLayout)findViewById(R.id.tabs);
        tl.setTabTextColors(ColorStateList.valueOf(Color.BLACK));
        tl.setSelectedTabIndicatorColor(Color.BLACK);

        adapter = new pagerAdapter(getSupportFragmentManager());


        //set up vPager
        vPager.setAdapter(adapter);
        tl.setupWithViewPager(vPager);


        // Set up ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Fit Tracker");
        }

        nr.createNotificationChannel(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}