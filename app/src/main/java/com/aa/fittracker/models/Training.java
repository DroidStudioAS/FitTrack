package com.aa.fittracker.models;
public class Training {

    private int id;
    private int difficulty;
    private String name;
    private String description;

    public Training() {
    }

    public Training(int id, int difficulty, String name, String description) {
        this.id = id;
        this.difficulty = difficulty;
        this.name = name;
        this.description = description;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
