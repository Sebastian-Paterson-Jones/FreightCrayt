package com.example.freightcrayt.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freightcrayt.R;
import com.example.freightcrayt.models.CollectionItem;
import com.example.freightcrayt.utils.DataHelper;
import com.example.freightcrayt.utils.IntentHelper;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewItem extends AppCompatActivity {

    //collectionItem
    CollectionItem collectionItem;

    // Item fields
    private String itemID;
    private String itemCollectionID;
    private int collectionSize;

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

        // set the fields
        this.itemID = getIntent().getExtras().getString("itemID");
        this.itemCollectionID = getIntent().getExtras().getString("collectionID");
        this.collectionSize = getIntent().getExtras().getInt("collectionSize");

        // init fields
        itemImage = (ImageView) findViewById(R.id.item_detail_Image);
        itemTitle = (TextView) findViewById(R.id.item_detail_title);
        itemAcquisition = (TextView) findViewById(R.id.item_detail_aquisitionDate);
        itemDescription = (TextView) findViewById(R.id.item_detail_description);
        editButton = (CircleImageView) findViewById(R.id.item_detail_btnEditItem);
        deleteButton = (CircleImageView) findViewById(R.id.item_detail_btnDeleteItem);

        // pull item data from database
        // retrieve user collections
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference itemRef = db.getReference("Items").child(itemID);

        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                collectionItem = snapshot.getValue(CollectionItem.class);
                if (collectionItem != null) {
                    itemTitle.setText(collectionItem.getTitle());
                    itemAcquisition.setText(collectionItem.getAcquisitionDate());
                    itemDescription.setText(collectionItem.getDescription());
                    if(collectionItem.getImage() != null) {
                        Picasso.get().load(collectionItem.getImage()).into(itemImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // dialog listener for delete yes no option box
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        DataHelper.removeCategoryItem(itemCollectionID, collectionSize, itemID, collectionItem.getImage());
                        finish();
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
                Bundle itemBundle = new Bundle();
                itemBundle.putString("title", collectionItem.getTitle());
                itemBundle.putString("acquisitionDate", collectionItem.getAcquisitionDate());
                itemBundle.putString("description", collectionItem.getDescription());
                itemBundle.putString("itemID", collectionItem.getItemID());
                itemBundle.putString("collectionID", collectionItem.getCollectionID());
                itemBundle.putString("image", collectionItem.getImage());
                IntentHelper.openIntent(ViewItem.this, itemBundle, edit_item_activity.class);
            }
        });
    }
}