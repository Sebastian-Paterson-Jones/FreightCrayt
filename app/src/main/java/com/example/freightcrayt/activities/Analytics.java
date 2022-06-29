package com.example.freightcrayt.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.freightcrayt.R;
import com.example.freightcrayt.adapters.CategoryItemListAdapter;
import com.example.freightcrayt.models.Collection;
import com.example.freightcrayt.models.CollectionItem;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Analytics extends AppCompatActivity {

    // current collections
    private String collectionTitle;
    private int collectionGoal;
    private int collectionSize;
    private String titleChar;

    //Sample arraylist for initial testing
    ArrayList barArrayList;
    ArrayList labelsArrayList;

    //fields
    private ImageView backButton;

    // items array
    private ArrayList<CollectionItem> items;

    // data array
    ArrayList<Collection> collections;

    // firebase reference
    DatabaseReference itemsRef;
    DatabaseReference userCollectionsRef;
    DatabaseReference collectionsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        // set collections list
        collections = new ArrayList<Collection>();

        // category items
        items = new ArrayList<>();

        // retrieve user collections
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        userCollectionsRef = db.getReference("UserCategories");
        collectionsRef = db.getReference("Categories");
        itemsRef = db.getReference("Items");

        // set the collection details
        this.collectionTitle = getIntent().getExtras().getString("title");
        this.collectionGoal = getIntent().getExtras().getInt("goal");
        this.collectionSize = getIntent().getExtras().getInt("size");

        //set fields
        this.backButton = (ImageView) findViewById(R.id.analytics_backButton);

        BarChart barChart = findViewById(R.id.barchart);
        GetAnalytics();
        BarDataSet barDataSet = new BarDataSet(barArrayList, "Analytics");
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

    // get the analytics for the collection
    private void GetAnalytics()
    {
        int testData = this.collectionSize;

        labelsArrayList = new ArrayList();
        barArrayList = new ArrayList();

        for (int i = 0; i < 1; i++) {
            titleChar = Character.toString(this.collectionTitle.charAt(0));
            barArrayList.add(new BarEntry( 2f,15));
            labelsArrayList.add(titleChar);
        }
    }


}