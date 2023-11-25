package com.aa.fittracker;

import com.aa.fittracker.models.WeightEntry;

public interface OnDateClickListener {
    void onDateClicked(String date);
    void onMatchFound(WeightEntry x);
}
