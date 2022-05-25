package com.example.freightcrayt.models;

import android.graphics.Bitmap;

public class CollectionItem {

    public Bitmap image;
    public String title;
    public String acquisitionDate;
    public String description;
    public String collectionID;
    public String itemID;

    public CollectionItem (String title, String acquisitionDate, String description, String collectionID, String itemID) {
        this.title = title;
        this.acquisitionDate = acquisitionDate;
        this.description = description;
        this.collectionID = collectionID;
        this.itemID = itemID;
    }

    public CollectionItem (String title, String acquisitionDate, String description, String collectionID, Bitmap image, String itemID) {
        this.title = title;
        this.acquisitionDate = acquisitionDate;
        this.description = description;
        this.collectionID = collectionID;
        this.image = image;
        this.itemID = itemID;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
