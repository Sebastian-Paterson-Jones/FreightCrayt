package com.example.freightcrayt.activities;

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
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freightcrayt.R;
import com.example.freightcrayt.adapters.CategoryItemListAdapter;
import com.example.freightcrayt.utils.DataHelper;
import com.example.freightcrayt.utils.IntentHelper;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class CategoryDetail extends AppCompatActivity {

    private String collectionID;
    private TextInputEditText searchBox;
    private TextView categoryTitle;
    private TextView categoryNumItems;
    private ImageView addNewItemButton;
    private FloatingActionButton addNew;
    private ImageView backButton;
    private ActionMenuItemView accountNav;
    private BottomAppBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        // create and instantiate dummy data
        DataHelper data = DataHelper.getInstance();

        // broadcast to finish activity on logout
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        }, intentFilter);

        // set the collectionID
        this.collectionID = getIntent().getStringExtra("extraInfo");

        // set the fields
        this.searchBox = (TextInputEditText) findViewById(R.id.category_detail_add_txtBoxSearch);
        this.categoryTitle = (TextView) findViewById(R.id.category_detail_title);
        this.categoryNumItems = (TextView) findViewById(R.id.category_detail_numItems);
        this.addNewItemButton = (ImageView) findViewById(R.id.category_detail_add);
        this.backButton = (ImageView) findViewById(R.id.category_detail_backButton);

//        Toast.makeText(this, data.getUserCategory(this.collectionID, this).title, Toast.LENGTH_SHORT).show();
        // Set the header text
        categoryTitle.setText(data.getUserCategory(this.collectionID).title);
        categoryNumItems.setText(String.valueOf(data.getUserCategorySize(this.collectionID)) + " items");

        // adapter init
        CategoryItemListAdapter itemListAdapter = new CategoryItemListAdapter(CategoryDetail.this, data.getUserCategoryItems(this.collectionID));

        // assign adapter to gridView
        GridView grid = (GridView) findViewById(R.id.category_detail_grid);
        grid.setAdapter(itemListAdapter);

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
                IntentHelper.openIntent(CategoryDetail.this, collectionID, AddItem.class);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}