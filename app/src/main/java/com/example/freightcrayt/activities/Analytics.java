package com.example.freightcrayt.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.freightcrayt.R;
import com.example.freightcrayt.adapters.CategoryListAdapter;
import com.example.freightcrayt.utils.DataHelper;
import com.example.freightcrayt.adapters.CategoryItemListAdapter;
import com.example.freightcrayt.models.Collection;
import com.example.freightcrayt.models.CollectionItem;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class Analytics extends AppCompatActivity {

    //Sample arraylist for initial testing
    ArrayList barArrayList;
    ArrayList labelsArrayList;

    // list item adapter
    CategoryListAdapter itemListAdapter;

    //fields
    private ImageView backButton;

    // data array
    ArrayList<Collection> collections;

    // firebase reference
    DatabaseReference userCollectionsRef;
    DatabaseReference collectionsRef;
    DatabaseReference collaborationsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        barArrayList = new ArrayList<>();
        labelsArrayList = new ArrayList<>();

        // retrieve user collections
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        userCollectionsRef = db.getReference("UserCategories");
        collectionsRef = db.getReference("Categories");
        collaborationsRef = db.getReference("CollectionCollaborations");

        userCollectionsRef.child(DataHelper.getUserID()).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot collectionsIDSnap : snapshot.getChildren()) {
                   String collectionID = collectionsIDSnap.getValue(String.class);

                   // add listener to collection
                   collectionsRef.child(collectionID).addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           Collection collection = snapshot.getValue(Collection.class);
                           if (collection != null) {
                               removeListCategoryByID(collection.getCollectionID());
                               collections.add(collection);
                               refreshAdapter(collections);

                               // Adding collections to the barchart
                               for (int i = 0; i < collections.size(); i++) {
                                   barArrayList.add(new BarEntry(i, collections.get(i).getSize()));
                                   labelsArrayList.add(collections.get(i).getTitle());
                               }
                           }
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {
                           Toast.makeText(Analytics.this, "failed to retrieve collections", Toast.LENGTH_SHORT).show();
                       }
                   });
               }
           }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Analytics.this, "Failed to retrieve collections", Toast.LENGTH_SHORT).show();
            }
        });

        //set fields
        this.backButton = (ImageView) findViewById(R.id.analytics_backButton);

        BarChart barChart = findViewById(R.id.barchart);
        //GetAnalytics();
        BarDataSet barDataSet = new BarDataSet(barArrayList, "Categories");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        //Set the Colour of Bar Graph Data Set
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        //Set Text Colour
        barDataSet.setValueTextColor(Color.BLACK);

        //Set the text size
        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(true);

        //on click listener for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void refreshAdapter(ArrayList<Collection> ArrayList) {
        // adapter init
        itemListAdapter = new CategoryListAdapter(Analytics.this, collections);
        //categoriesList.setAdapter(itemListAdapter);
    }

    private void removeListCategoryByID(String collectionID) {
        /*for(Collection item : collections) {
            if(collectionID.equals(item.getCollectionID())) {
                collections.remove(item);
                return;
            }
        }*/
    }
}