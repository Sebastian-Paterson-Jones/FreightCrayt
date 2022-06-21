package com.example.freightcrayt.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.freightcrayt.activities.CategoryDetail;
import com.example.freightcrayt.adapters.CategoryItemListAdapter;
import com.example.freightcrayt.models.Collection;
import com.example.freightcrayt.models.CollectionItem;
import com.example.freightcrayt.utils.DataHelper;
import com.example.freightcrayt.R;
import com.example.freightcrayt.adapters.CategoryListAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class personalCategory extends Fragment {

    // list item adapter
    CategoryListAdapter itemListAdapter;

    // data array
    ArrayList<Collection> collections;

    // data helper
    DataHelper data;

    // fields
    ListView categoriesList;

    // assign parent view;
    View view;

    DatabaseReference userCollectionsRef;
    DatabaseReference collectionsRef;

    public personalCategory() {
    }

    public static personalCategory newInstance() {
        personalCategory fragment = new personalCategory();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal_category, container, false);

        // set collections list
        collections = new ArrayList<Collection>();

        // retrieve user collections
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        userCollectionsRef = db.getReference("UserCategories");
        collectionsRef = db.getReference("Categories");

        userCollectionsRef.child(DataHelper.getUserID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot collectionsIDSnap : snapshot.getChildren()) {
                    String collectionID = collectionsIDSnap.getValue(String.class);

                    collectionsRef.child(collectionID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Collection collection = snapshot.getValue(Collection.class);
                            if(collection != null) {
                                removeListCategoryByID(collection.getCollectionID());
                                collections.add(collection);
                                refreshAdapter(collections);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), "failed to retrieve collections", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "failed to retrieve collections", Toast.LENGTH_SHORT).show();
            }
        });

        // adapter init
        itemListAdapter = new CategoryListAdapter(getContext(), collections);

        // assign adapter to listview
        categoriesList = (ListView) view.findViewById(R.id.personal_catergoriesListView);
        categoriesList.setAdapter(itemListAdapter);

        // get the search box
        TextInputEditText searchBox = (TextInputEditText) view.findViewById(R.id.personalCategory_txtBoxSearch);

        // set event listener for search box filtering
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                itemListAdapter.getFilter().filter(searchBox.getText());
            }
        });

        return view;
    }

    private void refreshAdapter(ArrayList<Collection> ArrayList) {
        // adapter init
        itemListAdapter = new CategoryListAdapter(getContext(), collections);
        categoriesList.setAdapter(itemListAdapter);
    }

    private void removeListCategoryByID(String collectionID) {
        for(Collection item : collections) {
            if(collectionID.equals(item.getCollectionID())) {
                collections.remove(item);
                return;
            }
        }
    }
}