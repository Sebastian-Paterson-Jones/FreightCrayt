package com.example.freightcrayt.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.freightcrayt.R;
//import com.example.freightcrayt.utils.DataHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;

public class Analytics extends AppCompatActivity {

    // Data helper for getting user analytics
    //DataHelper data;
    //Sample arraylist for initial testing
    ArrayList barArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

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
    }

    private void GetSampleData()
    {
        barArrayList = new ArrayList();
        barArrayList.add(new BarEntry(2f,10));
        barArrayList.add(new BarEntry(3f,15));
        barArrayList.add(new BarEntry(4f,25));
        barArrayList.add(new BarEntry(5f,35));
        barArrayList.add(new BarEntry(6f,50));
    }
}