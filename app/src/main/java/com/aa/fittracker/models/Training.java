package com.aa.fittracker.models;

public class Training {
    private int training_difficulty;
    private String training_name;
    private String training_desc;

    public int getTraining_difficulty() {
        return training_difficulty;
    }

    public void setTraining_difficulty(int training_difficulty) {
        this.training_difficulty = training_difficulty;
    }

    public String getTraining_name() {
        return training_name;
    }

    public void setTraining_name(String training_name) {
        this.training_name = training_name;
    }

    public String getTraining_desc() {
        return training_desc;
    }

    public void setTraining_desc(String training_desc) {
        this.training_desc = training_desc;
    }

    @Override
    public String toString() {
        return "Training{" +
                "training_difficulty=" + training_difficulty +
                ", training_name='" + training_name + '\'' +
                ", training_desc='" + training_desc + '\'' +
                '}';
    }
}
