package com.example.freightcrayt.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.freightcrayt.R;
import com.example.freightcrayt.utils.DataHelper;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddItem extends AppCompatActivity {

    // Data helper for getting user credentials
    DataHelper data;

    // Item ID
    private String itemID;

    // fields
    ImageView itemImage;
    TextView itemTitle;
    TextView itemSubTitle;
    TextView itemDescription;
    Spinner categoryChoice;
    CircleImageView editButton;
    Button addButton;
    Button discardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // broadcast to finish activity on logout
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        }, intentFilter);

        // initiate dataHelper
        data = DataHelper.getInstance();

        // set the itemID
        this.itemID = getIntent().getStringExtra("extraInfo");

        // init fields
        itemImage = (ImageView) findViewById(R.id.item_add_Image);
        itemTitle = (TextView) findViewById(R.id.title_textview);
        itemSubTitle = (TextView) findViewById(R.id.sub_title_textview);
        itemDescription = (TextView) findViewById(R.id.description_textview);
        editButton = (CircleImageView) findViewById(R.id.item_add_btn);
        addButton = (Button) findViewById(R.id.add_button);
        discardButton = (Button) findViewById(R.id.discard_button);
        categoryChoice = (Spinner) findViewById(R.id.category_dropdown);

        // dialog listener for delete yes no option box
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        data.removeUserCategoryItem(itemID);
                        Intent broadcastRefresh = new Intent();
                        broadcastRefresh.setAction("com.package.ACTION_LOGOUT");
                        sendBroadcast(broadcastRefresh);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        // discard button click event
        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddItem.this);
                builder.setMessage("Are you sure you want to discard this item?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener);
                builder.show();
            }
        });
    }
}