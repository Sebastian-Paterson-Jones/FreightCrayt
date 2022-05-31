package com.example.freightcrayt.models;

public class Collection {

    public String title;
    public int goal;
    public String description;
    public String collectionID;

    public Collection(String title, int goal, String description, String collectionID) {
        this.title = title;
        this.goal = goal;
        this.description = description;
        this.collectionID = collectionID;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
