package com.aa.fittracker.models;
public class TrainingEntry {
    private String training_date;
    private String training_name;

    public TrainingEntry(String training_date, String training_name) {
        this.training_date = training_date;
        this.training_name = training_name;
    }

    public TrainingEntry() {
    }

    public String getTraining_date() {
        return training_date;
    }

    public void setTraining_date(String training_date) {
        this.training_date = training_date;
    }

    public String getTraining_name() {
        return training_name;
    }

    public void setTraining_name(String training_name) {
        this.training_name = training_name;
    }


}
