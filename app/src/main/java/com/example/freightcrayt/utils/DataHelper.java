package com.example.freightcrayt.utils;

import com.example.freightcrayt.models.CollectionItem;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class DataHelper {

    // Singleton instance for holding persistent data in prototype
    private static DataHelper singleton_instance = null;

    // User collection
    private static ArrayList<CollectionItem> collections = new ArrayList<CollectionItem>();

    // static method for getting single instance
    public static DataHelper getInstance()
    {
        if (singleton_instance == null)
            singleton_instance = new DataHelper();

        return singleton_instance;
    }

    public String getUsername() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser().getDisplayName();
    }

    public String getUserEmail() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser().getEmail();
    }

    public String getUserID() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser().getUid();
    }

    public ArrayList<CollectionItem> getUserCategories() {
        return collections;
    }

    public int getUserCategoriesLength() { return collections.size(); }

    public void addUserCategory(String title, String description, String collectionID) {
        CollectionItem newCollection = new CollectionItem(title, description, collectionID);
        collections.add(newCollection);
    }

    public void removeCategory(String id) {
        // still needs to be implemented
    }
}
