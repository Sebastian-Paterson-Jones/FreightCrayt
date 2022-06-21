package com.example.freightcrayt.models;

import android.graphics.Bitmap;

public class CollectionItem {

    private Bitmap image;
    private String title;
    private String acquisitionDate;
    private String description;
    private String collectionID;
    private String itemID;

    public CollectionItem() {
    }

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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(String acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCollectionID() {
        return collectionID;
    }

    public void setCollectionID(String collectionID) {
        this.collectionID = collectionID;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
