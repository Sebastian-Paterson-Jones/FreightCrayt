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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.freightcrayt.R;
import com.example.freightcrayt.models.CollectionItem;
import com.example.freightcrayt.utils.DataHelper;
import com.example.freightcrayt.utils.IntentHelper;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewItem extends AppCompatActivity {

    // Data helper for getting user credentials
    DataHelper data;

    // Item ID
    private String itemID;

    // fields
    ImageView itemImage;
    TextView itemTitle;
    TextView itemAcquisition;
    TextView itemDescription;
    CircleImageView editButton;
    CircleImageView deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

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
        itemImage = (ImageView) findViewById(R.id.item_detail_Image);
        itemTitle = (TextView) findViewById(R.id.item_detail_title);
        itemAcquisition = (TextView) findViewById(R.id.item_detail_aquisitionDate);
        itemDescription = (TextView) findViewById(R.id.item_detail_description);
        editButton = (CircleImageView) findViewById(R.id.item_detail_btnEditItem);
        deleteButton = (CircleImageView) findViewById(R.id.item_detail_btnDeleteItem);

        // get item model
        CollectionItem currentItem = data.getUserItem(itemID);

        // set the item image
        this.itemImage.setImageBitmap(currentItem.image);

        // set text title
        this.itemTitle.setText(currentItem.title);

        // set text acquisition date
        this.itemAcquisition.setText(currentItem.acquisitionDate);

        // set the description
        this.itemDescription.setText(currentItem.description);

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
        // delete button click event
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewItem.this);
                builder.setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener);
                builder.show();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.openIntent(ViewItem.this, itemID, edit_item_activity.class);
            }
        });
    }
}