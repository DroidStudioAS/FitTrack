package com.aa.fittracker.trainingservice;

import com.aa.fittracker.models.SharedTraining;
import com.aa.fittracker.models.Training;

public interface onItemClickListener {
    void onTrainingFocus(Training training);
    void onSharedTrainingFocus(SharedTraining st);
}