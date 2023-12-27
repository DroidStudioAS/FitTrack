package com.aa.fittracker;

import com.aa.fittracker.models.TrainingEntry;
import com.aa.fittracker.models.WeightEntry;

public interface FragmentCommunicator {
    void onDateClicked(String date);
    void onMatchFound(WeightEntry x);
    void onTrainingMatchFound(TrainingEntry x);
}
