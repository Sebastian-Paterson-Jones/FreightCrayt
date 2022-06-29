package com.example.freightcrayt.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freightcrayt.R;
import com.example.freightcrayt.adapters.CategoryItemListAdapter;
import com.example.freightcrayt.adapters.CategoryListAdapter;
import com.example.freightcrayt.models.Collection;
import com.example.freightcrayt.models.CollectionItem;
import com.example.freightcrayt.utils.DataHelper;
import com.example.freightcrayt.utils.IntentHelper;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class CategoryDetail extends AppCompatActivity {

    // bottom nav functionality
    private BottomAppBar bottomNav;
    private FloatingActionButton addItemButton;
    private ActionMenuItemView accountNav;

    // current collection id
    private String collectionID;
    private String collectionTitle;
    private String collectionDescription;
    private int collectionGoal;
    private int collectionSize;

    // fields
    private TextInputEditText searchBox;
    private TextView categoryTitle;
    private TextView categoryNumItems;
    private ImageView addNewItemButton;
    private ImageView backButton;

    // grid
    GridView grid;

    // loading container
    private FrameLayout loadingContainer;

    // items array
    private ArrayList<CollectionItem> items;

    // list adapter
    CategoryItemListAdapter itemListAdapter;

    // firebase reference
    DatabaseReference itemsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        // set the collection details
        this.collectionID = getIntent().getExtras().getString("collectionID");
        this.collectionTitle = getIntent().getExtras().getString("title");
        this.collectionDescription = getIntent().getExtras().getString("description");
        this.collectionGoal = getIntent().getExtras().getInt("goal");
        this.collectionSize = getIntent().getExtras().getInt("size");


        // broadcast to finish activity on logout
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        }, intentFilter);

        // set the fields
        this.searchBox = (TextInputEditText) findViewById(R.id.category_detail_add_txtBoxSearch);
        this.categoryTitle = (TextView) findViewById(R.id.category_detail_title);
        this.categoryNumItems = (TextView) findViewById(R.id.category_detail_numItems);
        this.addNewItemButton = (ImageView) findViewById(R.id.category_detail_add);
        this.backButton = (ImageView) findViewById(R.id.category_detail_backButton);
        this.grid = (GridView) findViewById(R.id.category_detail_grid);
        bottomNav = (BottomAppBar) findViewById(R.id.bottom_nav_bar);
        addItemButton = (FloatingActionButton) findViewById(R.id.bottom_nav_addItem);
        accountNav = (ActionMenuItemView) findViewById(R.id.bottomNavPerson);
        loadingContainer = (FrameLayout) findViewById(R.id.category_detail_loading_progress_container);

        // set loading
        showLoadingSate();

        // Set the header text
        categoryTitle.setText(this.collectionTitle);

        // update num items title
        categoryNumItems.setText(this.collectionSize + " of " + this.collectionGoal + " items");

        // category items
        items = new ArrayList<>();

        // retrieve user collections
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        itemsRef = db.getReference("Items");

        itemsRef.orderByChild("collectionID").equalTo(this.collectionID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                CollectionItem item = snapshot.getValue(CollectionItem.class);
                if(item != null) {
                    items.add(item);
                    refreshAdapter(items);
                    hideLoadingState();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                CollectionItem item = snapshot.getValue(CollectionItem.class);
                if(item != null) {
                    removeListItemByID(item.getItemID());
                    items.add(item);
                    refreshAdapter(items);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                CollectionItem item = snapshot.getValue(CollectionItem.class);
                if(item != null) {
                    removeListItemByID(item.getItemID());
                    refreshAdapter(items);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CategoryDetail.this, "Item updates canceled", Toast.LENGTH_SHORT).show();
            }
        });

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

        // onClick handlers
        addNewItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle intentBundle = new Bundle();
                intentBundle.putString("collectionID", collectionID);
                intentBundle.putString("title", collectionTitle);
                intentBundle.putString("description", collectionDescription);
                intentBundle.putInt("goal", collectionGoal);
                intentBundle.putInt("size", collectionSize);
                IntentHelper.openIntent(CategoryDetail.this, intentBundle, AddItem.class);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.openIntent(CategoryDetail.this, "addNew", add_new.class);
            }
        });
        accountNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.openIntent(CategoryDetail.this, "Account nav", UserDetail.class);
            }
        });
        bottomNav.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.openIntent(CategoryDetail.this, "Home", MainActivity.class);
            }
        });
    }

    private void refreshAdapter(ArrayList<CollectionItem> ArrayList) {
        // adapter init
        itemListAdapter = new CategoryItemListAdapter(CategoryDetail.this, ArrayList, collectionSize);
        grid.setAdapter(itemListAdapter);
    }

    private void removeListItemByID(String itemID) {
        for(CollectionItem item : items) {
            if(itemID.equals(item.getItemID())) {
                items.remove(item);
                return;
            }
        }
    }

    private void showLoadingSate() {
        loadingContainer.setVisibility(View.VISIBLE);
        grid.setVisibility(View.GONE);
    }

    private void hideLoadingState() {
        loadingContainer.setVisibility(View.GONE);
        grid.setVisibility(View.VISIBLE);
    }
}