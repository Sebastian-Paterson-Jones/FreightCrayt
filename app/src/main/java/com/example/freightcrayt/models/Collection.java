package com.example.freightcrayt.models;

public class Collection {

    public String title;
    public int goal;
    public String description;
    public String collectionID;
    public String userID;

    public Collection(String title, int goal, String description, String collectionID, String userID) {
        this.title = title;
        this.goal = goal;
        this.description = description;
        this.collectionID = collectionID;
        this.userID = userID;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
