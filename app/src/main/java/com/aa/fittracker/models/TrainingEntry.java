package com.aa.fittracker.models;
public class TrainingEntry {

    private String training_date;
    private String training_name;
    private String diff;

    private String training_description;
    private boolean isFreestyle=false;

    public TrainingEntry(String training_date, String training_name) {
        this.training_date = training_date;
        this.training_name = training_name;
    }
    public TrainingEntry(String training_date, String training_name, String diff, String training_description){
        this.training_name=training_name;
        this.training_date=training_date;
        this.diff=diff;
        this.training_description=training_description;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
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
    public String getTraining_description() {
        return training_description;
    }

    public void setTraining_description(String training_description) {
        this.training_description = training_description;
    }

    public void setTraining_name(String training_name) {
        this.training_name = training_name;
    }
    public boolean isFreestyle() {
        return isFreestyle;
    }

    public void setFreestyle(boolean freestyle) {
        isFreestyle = freestyle;
    }


}
