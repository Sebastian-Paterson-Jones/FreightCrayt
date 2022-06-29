package com.example.freightcrayt.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freightcrayt.R;
import com.example.freightcrayt.models.Collection;
import com.example.freightcrayt.models.CollectionItem;
import com.example.freightcrayt.utils.DataHelper;
import com.example.freightcrayt.utils.IntentHelper;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserDetail extends AppCompatActivity {

    // bottom nav functionality
    private BottomAppBar bottomNav;
    private FloatingActionButton addItemButton;

    private ImageView userImage;
    private TextView userEmail;
    private TextView userName;
    private TextView totalCrates;
    private TextView totalItems;
    private Button signOut;
    private Button analyticsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        // broadcast to finish activity on logout
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        }, intentFilter);

        // initiate fields
        userImage = (ImageView) findViewById(R.id.user_userImage);
        userEmail = (TextView) findViewById(R.id.user_txtboxEmail);
        userName = (TextView) findViewById(R.id.user_txtboxUsername);
        totalCrates = (TextView) findViewById(R.id.user_totalCrates);
        totalItems = (TextView) findViewById(R.id.user_totalItems);
        signOut = (Button) findViewById(R.id.user_signOut);
        analyticsButton = (Button) findViewById(R.id.user_analytics);
        bottomNav = (BottomAppBar) findViewById(R.id.bottom_nav_bar);
        addItemButton = (FloatingActionButton) findViewById(R.id.bottom_nav_addItem);

        // set user details
        userImage.setImageResource(R.drawable.ic_baseline_person_24);
        userEmail.setText(DataHelper.getUserEmail());
        userName.setText(DataHelper.getUsername());

        // firebase listeners for total counts
        ArrayList<Collection> collections = new ArrayList<Collection>();
        final int[] collectionItemsCounts = {0};

        // retrieve user collections
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference userCollectionsRef = db.getReference("UserCategories");
        DatabaseReference collectionsRef = db.getReference("Categories");
        DatabaseReference collaborationCollections = db.getReference("CollectionCollaborations");
        DatabaseReference itemsReference = db.getReference("Items");

        // get user categories
        userCollectionsRef.child(DataHelper.getUserID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot collectionsIDSnap : snapshot.getChildren()) {
                    String collectionID = collectionsIDSnap.getValue(String.class);

                    // add listener to collection
                    collectionsRef.child(collectionID).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Collection collection = snapshot.child(collectionID).getValue(Collection.class);
                            if(collection != null) {
                                collections.add(collection);
                                totalCrates.setText(collections.size() + " crates");
                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                            Collection collection = snapshot.child(collectionID).getValue(Collection.class);
                            if(collection != null) {
                                collections.remove(collection);
                                totalCrates.setText(collections.size() + " crates");
                            }
                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // items for user collaborations they are invited to
        collaborationCollections.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()) {
                    String collectionID = snap.getKey();
                    if(collectionID != null) {
                        Boolean isUser = snap.child(DataHelper.getUserID()).getValue(Boolean.class);
                        if(isUser != null) {
                            if(isUser) {
                                collectionsRef.child(collectionID).addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        Collection collection = snapshot.child(collectionID).getValue(Collection.class);
                                        if(collection != null) {
                                            collections.add(collection);
                                            totalCrates.setText(collections.size() + " crates");
                                        }
                                    }

                                    @Override
                                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                    }

                                    @Override
                                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                                        Collection collection = snapshot.child(collectionID).getValue(Collection.class);
                                        if(collection != null) {
                                            collections.remove(collection);
                                            totalCrates.setText(collections.size() + " crates");
                                        }
                                    }

                                    @Override
                                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // get user items
        for(Collection collection : collections) {
            itemsReference.orderByChild("collectionID").equalTo(collection.getCollectionID()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    collectionItemsCounts[0]++;
                    totalItems.setText(collectionItemsCounts[0] + " items");
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    collectionItemsCounts[0]--;
                    totalItems.setText(collectionItemsCounts[0] + " items");
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                IntentHelper.logout(UserDetail.this);
            }
        });
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.openIntent(UserDetail.this, "addNew", add_new.class);
            }
        });
        analyticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.openIntent(UserDetail.this, "Analytics", Analytics.class);
            }
        });
        bottomNav.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.openIntent(UserDetail.this, "Home", MainActivity.class);
            }
        });
    }
}