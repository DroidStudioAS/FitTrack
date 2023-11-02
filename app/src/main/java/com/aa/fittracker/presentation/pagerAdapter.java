package com.aa.fittracker.presentation;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.aa.fittracker.RegisterFragment;
import com.aa.fittracker.loginFragment;

public class pagerAdapter extends FragmentPagerAdapter {
    static final int NUM_OF_PAGES = 2;
    static final String[] TITLES = {"Login","Register"};


    public pagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                return new loginFragment();
            case 1:
                return new RegisterFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_OF_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}

