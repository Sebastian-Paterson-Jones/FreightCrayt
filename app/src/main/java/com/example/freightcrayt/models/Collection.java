package com.example.freightcrayt.models;

public class Collection {

    private String title;
    private int goal;
    private String description;
    private String collectionID;
    private int size;
    private boolean isCollaboration;

    public Collection() {
    }

    public Collection(String title, int goal, String description, String collectionID) {
        this.title = title;
        this.goal = goal;
        this.description = description;
        this.collectionID = collectionID;
        this.size = 0;
    }
    public Collection(String title, int goal, String description, String collectionID, int size) {
        this.title = title;
        this.goal = goal;
        this.description = description;
        this.collectionID = collectionID;
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean getIsCollaboration() {
        return isCollaboration;
    }

    public boolean setIsCollaboration(boolean isCollaboration) {
        this.isCollaboration = isCollaboration;
        return isCollaboration;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
