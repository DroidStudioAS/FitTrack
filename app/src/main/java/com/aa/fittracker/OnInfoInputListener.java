package com.aa.fittracker;

import com.aa.fittracker.models.TrainingEntry;
import com.aa.fittracker.models.WeightEntry;

public interface OnInfoInputListener {
    void onWeightInput(WeightEntry x);
    void onTrainingInput(TrainingEntry x);
}
