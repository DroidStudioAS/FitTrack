package com.aa.fittracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
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
    /*************UI ELEMENTS DECLERATION*************/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing
        vPager = (ViewPager) findViewById(R.id.vPager);
        tl=(TabLayout)findViewById(R.id.tabs);
        adapter = new pagerAdapter(getSupportFragmentManager());




        //set up vPager
        vPager.setAdapter(adapter);
        tl.setupWithViewPager(vPager);


    }
}