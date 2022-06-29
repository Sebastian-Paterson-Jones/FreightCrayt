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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class Analytics extends AppCompatActivity {

    //Sample arraylist for initial testing
    ArrayList<BarEntry> barArrayList;
    ArrayList<String> labelsArrayList;

    //fields
    private ImageView backButton;

    // data array
    ArrayList<Collection> collections;

    BarDataSet barDataSet;
    BarChart barChart;
    BarData barData;

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

        //set bar chart and data set
        barChart = findViewById(R.id.barchart);

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
                               addEntriesToChart(collection);
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
                                            addEntriesToChart(collection);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(Analytics.this, "failed to retrieve collaboration collections", Toast.LENGTH_SHORT).show();
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

        //set fields
        this.backButton = (ImageView) findViewById(R.id.analytics_backButton);

        //on click listener for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void addEntriesToChart(Collection collection) {
        barArrayList.add(new BarEntry( barArrayList.size(), collection.getSize()));
        labelsArrayList.add(collection.getTitle());
        barDataSet = new BarDataSet(barArrayList, "crate item count");
        barData = new BarData(barDataSet);
        barChart.notifyDataSetChanged();
        barChart.setData(barData);
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(false);
        barChart.animateY(1000);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labelsArrayList));
        barChart.invalidate();
    }

    private void removeListCategoryByID(String collectionID) {
        collections = new ArrayList<>();
        for(Collection item : collections) {
            if(collectionID.equals(item.getCollectionID())) {
                collections.remove(item);
                return;
            }
        }
    }
}