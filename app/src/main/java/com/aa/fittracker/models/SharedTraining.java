package com.aa.fittracker.models;

public class SharedTraining extends Training {

    private int shared_training_difficulty;
    private String shared_training_name;
    private String shared_training_desc;

    public int getShared_training_difficulty() {
        return shared_training_difficulty;
    }

    public void setShared_training_difficulty(int shared_training_difficulty) {
        this.shared_training_difficulty = shared_training_difficulty;
    }

    public String getShared_training_name() {
        return shared_training_name;
    }

    public void setShared_training_name(String shared_training_name) {
        this.shared_training_name = shared_training_name;
    }

    public String getShared_training_desc() {
        return shared_training_desc;
    }

    public void setShared_training_desc(String shared_training_desc) {
        this.shared_training_desc = shared_training_desc;
    }

    @Override
    public String toString() {
        return "Name: " + shared_training_name + "\n"
                + "Difficulty: " + shared_training_difficulty + "\n"
                + "Description: " + shared_training_desc + "\n";
    }
}
