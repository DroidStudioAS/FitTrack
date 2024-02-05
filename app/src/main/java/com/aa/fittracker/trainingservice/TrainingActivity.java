package com.aa.fittracker.trainingservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.aa.fittracker.R;
import com.aa.fittracker.TrainingDeletedCallback;
import com.aa.fittracker.dialog.InfoDialog;
import com.aa.fittracker.presentation.pagerAdapter;
import com.aa.fittracker.presentation.taAdapter;
import com.google.android.material.tabs.TabLayout;

public class TrainingActivity extends AppCompatActivity implements TrainingAddedCallback {

    private static ViewPager vPager;
    private taAdapter adapter;
    private TabLayout tl;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.infor:
                InfoDialog infoDialog = new InfoDialog(this);
                infoDialog.setOpenedFrom("TA");
                infoDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.basic_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        ActionBar ab = getSupportActionBar();
        if(ab!=null){
            ab.setTitle("My Trainings");
        }

        vPager=(ViewPager) findViewById(R.id.vpTa);
        tl=(TabLayout)findViewById(R.id.taTabs);
        tl.setTabTextColors(ColorStateList.valueOf(Color.BLACK));
        tl.setSelectedTabIndicatorColor(Color.BLACK);

        adapter = new taAdapter(getSupportFragmentManager());



        //set up vPager
        vPager.setAdapter(adapter);
        tl.setupWithViewPager(vPager);


    }
    public static void setCurrentPosition(int pos){
        vPager.setCurrentItem(pos);
    }


    @Override
    public void onTrainingInput() {

    }
}