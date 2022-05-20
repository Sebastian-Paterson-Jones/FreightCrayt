package com.example.freightcrayt.models;

public class Collection {

    public String title;
    public String description;
    public String collectionID;

    public Collection(String title, String description, String collectionID) {
        this.title = title;
        this.description = description;
        this.collectionID = collectionID;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
