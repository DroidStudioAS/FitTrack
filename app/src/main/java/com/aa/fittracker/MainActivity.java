package com.aa.fittracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;

import com.aa.fittracker.network.networkHelper;
import com.aa.fittracker.presentation.pagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
  /*************UI ELEMENTS DECLERATION*************/
   private ViewPager vPager;
   private pagerAdapter adapter;
   private TabLayout tl;
   private Button trigger;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.basic_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*************UI ELEMENTS DECLERATION*************/




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    }
}