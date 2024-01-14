package com.aa.fittracker.trainingservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import com.aa.fittracker.R;
import com.aa.fittracker.presentation.pagerAdapter;
import com.aa.fittracker.presentation.taAdapter;
import com.google.android.material.tabs.TabLayout;

public class TrainingActivity extends AppCompatActivity {

    private ViewPager vPager;
    private taAdapter adapter;
    private TabLayout tl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        vPager=(ViewPager) findViewById(R.id.vpTa);
        tl=(TabLayout)findViewById(R.id.taTabs);
        tl.setTabTextColors(ColorStateList.valueOf(Color.BLACK));
        tl.setSelectedTabIndicatorColor(Color.BLACK);

        adapter = new taAdapter(getSupportFragmentManager());

        //set up vPager
        vPager.setAdapter(adapter);
        tl.setupWithViewPager(vPager);
    }
    public void setCurrentPosition(int pos){
        vPager.setCurrentItem(pos);
    }
}