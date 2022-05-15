package com.example.freightcrayt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

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

    public static String getUsername() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser().getDisplayName();
    }

    public static String getUserEmail() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser().getEmail();
    }

    public static String getUserID() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser().getUid();
    }

    public static ArrayList<CollectionItem> getUserCategories() {
        return collections;
    }

    public static void addUserCategory(String title, String description, String collectionID) {
        CollectionItem newCollection = new CollectionItem(title, description, collectionID);
        collections.add(newCollection);
    }

    public static void removeCategory(String id) {
        // still needs to be implemented
    }
}
