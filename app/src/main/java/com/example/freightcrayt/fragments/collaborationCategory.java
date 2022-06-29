package com.example.freightcrayt.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.freightcrayt.R;
import com.example.freightcrayt.adapters.CategoryListAdapter;
import com.example.freightcrayt.models.Collection;
import com.example.freightcrayt.utils.DataHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class collaborationCategory extends Fragment {

    // list item adapter
    CategoryListAdapter itemListAdapter;

    // data array
    ArrayList<Collection> collaborationCollections;

    // fields
    ListView categoriesList;
    FrameLayout loadingContainer;

    // assign parent view;
    View view;

    // firebase references
    DatabaseReference userCollectionsRef;
    DatabaseReference collectionsRef;
    DatabaseReference collaborationsRef;

    public collaborationCategory() {
    }

    public static collaborationCategory newInstance() {
        collaborationCategory fragment = new collaborationCategory();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_collaboration_category, container, false);

        // set collections list
        collaborationCollections = new ArrayList<Collection>();

        // retrieve user collections
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        userCollectionsRef = db.getReference("UserCategories");
        collectionsRef = db.getReference("Categories");
        collaborationsRef = db.getReference("CollectionCollaborations");

        // adapter init
        itemListAdapter = new CategoryListAdapter(getContext(), collaborationCollections);

        // assign adapter to listview
        categoriesList = (ListView) view.findViewById(R.id.personal_collaborationsListView);
        categoriesList.setAdapter(itemListAdapter);

        // get loading state
        loadingContainer = (FrameLayout) view.findViewById(R.id.collaboration_loading_progress_container);

        // hide respective views
        this.showLoadingSate();

        // get the search box
        TextInputEditText searchBox = (TextInputEditText) view.findViewById(R.id.collaborationsCategory_txtBoxSearch);

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

        // listener for user collaborations they own
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
                                if(collection.getIsCollaboration()) {
                                    removeListCategoryByID(collection.getCollectionID());
                                    collaborationCollections.add(collection);
                                    refreshAdapter(collaborationCollections);
                                }
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

        // listner for user collaborations they are invited to
        collaborationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()) {
                    String collectionID = snap.getKey();
                    if(collectionID != null) {
                        Boolean isUser = snap.child(DataHelper.getUserID()).getValue(Boolean.class);
                        if(isUser != null) {
                            if(isUser) {
                                collectionsRef.child(collectionID).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Collection collection = snapshot.getValue(Collection.class);
                                        if(collection != null) {
                                            removeListCategoryByID(collection.getCollectionID());
                                            collaborationCollections.add(collection);
                                            refreshAdapter(collaborationCollections);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getContext(), "failed to retrieve collections", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                }

                hideLoadingState();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void refreshAdapter(ArrayList<Collection> ArrayList) {
        // adapter init
        itemListAdapter = new CategoryListAdapter(getContext(), collaborationCollections);
        categoriesList.setAdapter(itemListAdapter);
    }

    private void removeListCategoryByID(String collectionID) {
        for(Collection item : collaborationCollections) {
            if(collectionID.equals(item.getCollectionID())) {
                collaborationCollections.remove(item);
                return;
            }
        }
    }

    private void showLoadingSate() {
        loadingContainer.setVisibility(View.VISIBLE);
        categoriesList.setVisibility(View.GONE);
    }

    private void hideLoadingState() {
        loadingContainer.setVisibility(View.GONE);
        categoriesList.setVisibility(View.VISIBLE);
    }
}