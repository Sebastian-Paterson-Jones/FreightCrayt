package com.example.freightcrayt.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.freightcrayt.R;
//import com.example.freightcrayt.utils.DataHelper;
import com.example.freightcrayt.adapters.CategoryItemListAdapter;
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

    // current collection id
    //private String collectionID;
    //private String collectionTitle;
    //private String collectionDescription;
    private int collectionGoal;
    private int collectionSize;

    // Data helper for getting user analytics
    //DataHelper data;
    //Sample arraylist for initial testing
    ArrayList barArrayList;

    //fields
    private ImageView backButton;

    // items array
    private ArrayList<CollectionItem> items;

    // list adapter
    //CategoryItemListAdapter itemListAdapter;

    // firebase reference
    DatabaseReference itemsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        // set the collection details
        //this.collectionID = getIntent().getExtras().getString("collectionID");
        //this.collectionTitle = getIntent().getExtras().getString("title");
        //this.collectionDescription = getIntent().getExtras().getString("description");
        this.collectionGoal = getIntent().getExtras().getInt("goal");
        this.collectionSize = getIntent().getExtras().getInt("size");

        //set fields
        this.backButton = (ImageView) findViewById(R.id.category_detail_backButton);

        // Set the category labels
        //categoryTitle.setText(this.collectionTitle);

        // update num items title
        //categoryNumItems.setText(this.collectionSize + " of " + this.collectionGoal + " items");

        // category items
        items = new ArrayList<>();

        // retrieve user collections
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        itemsRef = db.getReference("Items");

        BarChart barChart = findViewById(R.id.barchart);
        GetSampleData();
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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void GetSampleData()
    {
        int testData = this.collectionSize/this.collectionGoal;
        testData = testData * 100;

        barArrayList = new ArrayList();
        barArrayList.add(new BarEntry(2f,testData));
        barArrayList.add(new BarEntry(3f,15));
        barArrayList.add(new BarEntry(4f,25));
        barArrayList.add(new BarEntry(5f,35));
        barArrayList.add(new BarEntry(6f,50));
    }
}