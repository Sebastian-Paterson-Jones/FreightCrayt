package com.example.freightcrayt;

public class CollectionItem {

    public String title;
    public String description;
    public String collectionID;

    public CollectionItem(String title, String description, String collectionID) {
        this.title = title;
        this.description = description;
        this.collectionID = collectionID;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
