package com.example.freightcrayt.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.freightcrayt.activities.MainActivity;
import com.example.freightcrayt.models.Collection;
import com.example.freightcrayt.models.CollectionItem;
import com.google.firebase.auth.FirebaseAuth;
import java.util.UUID;
import java.util.ArrayList;

public class DataHelper {

    // Singleton instance for holding persistent data in prototype
    private static DataHelper singleton_instance = null;

    // User collections
    private static ArrayList<Collection> collections = new ArrayList<Collection>();

    // User items
    private static ArrayList<CollectionItem> items = new ArrayList<CollectionItem>();

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

    public ArrayList<Collection> getUserCategories() {
        ArrayList<Collection> userCollections = new ArrayList<Collection>();
        for(int index = 0; index < collections.size(); index++) {
            if(collections.get(index).userID.equals(getUserID())) {
                userCollections.add(collections.get(index));
            }
        }
        return userCollections;
    }

    public int getUserCategoriesLength() {
        return getUserCategories().size();
    }

    public ArrayList<CollectionItem> getUserCategoryItems(String collectionID) {
        ArrayList<CollectionItem> categoryItems = new ArrayList<CollectionItem>();
        for(int index = 0; index < items.size(); index++) {
            if(items.get(index).collectionID.equals(collectionID)) {
                categoryItems.add(items.get(index));
            }
        }
        return categoryItems;
    }
    
    public Collection getUserCategory(String collectionID) {
        for(int index = 0; index < collections.size(); index++) {
            if(collections.get(index).collectionID.equals(collectionID)) {
                return collections.get(index);
            }
        }
        return null;
    }

    public int getUserCategorySize(String collectionID) {
        int count = 0;
        for(int index = 0; index < items.size(); index++) {
            if(items.get(index).collectionID.equals(collectionID)) {
                count += 1;
            } else {
                continue;
            }
        }
        return count;
    }

    public int getUserCategoryGoal(String collectionID) {
        for(int index = 0; index < items.size(); index++) {
            if(collections.get(index).collectionID.equals(collectionID)) {
                return collections.get(index).goal;
            } else {
                continue;
            }
        }
        return 0;
    }

    public void addUserCategory(String title, int goal, String description) {
        String collectionID = UUID.randomUUID().toString().replaceAll("_", "");
        Collection newCollection = new Collection(title, goal, description, collectionID, getUserID());
        collections.add(newCollection);
    }

    public void removeUserCategory(String collectionID) {
        for(int index = 0; index < collections.size(); index++) {
            if(collections.get(index).collectionID.equals(collectionID)) {
                collections.remove(index);
            }
        }
    }

    public ArrayList<CollectionItem> getUserItems() { return items; }

    public int getUserItemsLength() { return items.size(); }

    public CollectionItem getUserItem(String itemID) {
        for(int i=0;i<items.size();i++) {
            CollectionItem temp = items.get(i);
            if(temp.itemID.equals(itemID)) {
                return temp;
            }
        }
        return null;
    }

    public void addUserCategoryItem(String title, String subtitle, String description, String collectionID, @Nullable Bitmap image) {
        String itemID = UUID.randomUUID().toString().replaceAll("_", "");
        CollectionItem item;
        if(image == null) {
             item = new CollectionItem(title, subtitle, description, collectionID, itemID);
        } else {
            item = new CollectionItem(title, subtitle, description, collectionID, image, itemID);
        }
        items.add(item);
    }

    public void removeUserCategoryItem(String itemID) {
        for(int index = 0; index < items.size(); index++) {
            if(items.get(index).itemID.equals(itemID)) {
                items.remove(index);
            }
        }
    }

}
