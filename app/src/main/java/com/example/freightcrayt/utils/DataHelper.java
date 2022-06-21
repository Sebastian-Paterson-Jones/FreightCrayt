package com.example.freightcrayt.utils;

import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freightcrayt.activities.AddItem;
import com.example.freightcrayt.models.Collection;
import com.example.freightcrayt.models.CollectionItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;
import java.util.ArrayList;

public class DataHelper {

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

    public static boolean addUserCategory(String title, int goal, String description) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        String currentUserID = getUserID();
        final boolean[] res = {true};

        // save category to categories collection
        DatabaseReference categoryRef = db.getReference("Categories").push();
        Collection newCollection = new Collection(title, goal, description, categoryRef.getKey());
        categoryRef.setValue(newCollection, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if(error != null) {
                    res[0] = false;
                }
            }
        });

        if(!res[0]) {
            return res[0];
        }

        // save category to categories collection
        DatabaseReference userCollectionsRef = db.getReference("UserCategories/" + getUserID()).push();
        userCollectionsRef.setValue(categoryRef.getKey(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if(error != null) {
                    res[0] = false;
                }
            }
        });

        return res[0];
    }

    public static boolean removeCategory(String collectionID) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final boolean[] res = {true};

        DatabaseReference categoryRef = db.getReference("Categories");
        categoryRef.child(collectionID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                res[0] = false;
            }
        });

        return res[0];
    }

    public static boolean addCategoryItem(String title, String acquisitionDate, String description, String collectionID, @Nullable Bitmap image, int collectionSize) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final boolean[] res = {true};

        // save item to collection
        DatabaseReference itemsRef = db.getReference("Items").push();
        CollectionItem newCollection = new CollectionItem(title, acquisitionDate, description, collectionID, image, itemsRef.getKey());
        itemsRef.setValue(newCollection, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if(error != null) {
                    res[0] = false;
                }
            }
        });

        if(!res[0]) {
            return res[0];
        }

        incrementCollectionSize(collectionID, collectionSize);

        return res[0];
    }

    public static boolean editCategoryItem(CollectionItem item) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final boolean[] res = {true};

        DatabaseReference categoryRef = db.getReference("Items").child(item.getItemID());
        categoryRef.setValue(item, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if(error != null) {
                    res[0] = false;
                }
            }
        });

        return res[0];
    }

    public static boolean removeCategoryItem(String collectionID, int collectionSize, String itemID) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final boolean[] res = {true};

        DatabaseReference categoryRef = db.getReference("Items");
        categoryRef.child(itemID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
                decrementCollectionSize(collectionID, collectionSize);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                res[0] = false;
            }
        });

        return res[0];
    }

    public static void incrementCollectionSize(String collectionID, int collectionSize) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db.getReference("Categories").child(collectionID).child("size").setValue(collectionSize + 1);
    }

    public static void decrementCollectionSize(String collectionID, int collectionSize) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db.getReference("Categories").child(collectionID).child("size").setValue(collectionSize - 1);
    }
}
