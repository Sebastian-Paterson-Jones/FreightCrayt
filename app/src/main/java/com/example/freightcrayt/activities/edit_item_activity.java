package com.example.freightcrayt.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.freightcrayt.R;
import com.example.freightcrayt.models.Collection;
import com.example.freightcrayt.models.CollectionItem;
import com.example.freightcrayt.utils.DataHelper;
import com.squareup.picasso.Picasso;
import com.example.freightcrayt.utils.IntentHelper;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class edit_item_activity extends AppCompatActivity {

    // camera permission final ints
    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private static final int REQUEST_IMAGE_CAPTURE_PERMISSION = 100;

    // item components
    Bitmap image;
    ImageView itemImage;
    TextView itemTitle;
    TextView date;
    TextView itemDescription;
    CircleImageView editButton;
    Button addButton;
    Button discardButton;

    // Item fields
    private String itemID;
    private String itemTitleText;
    private String itemAcquisitionDatetext;
    private String itemDescriptionText;
    private String itemCollectionID;
    private String imageUrl;

    // bottom nav functionality
    private BottomAppBar bottomNav;
    private FloatingActionButton addItemButton;
    private ActionMenuItemView accountNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // broadcast to finish activity on logout
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        }, intentFilter);

        // set the item fields
        this.itemID = getIntent().getExtras().getString("itemID");
        this.itemTitleText = getIntent().getExtras().getString("title");
        this.itemAcquisitionDatetext = getIntent().getExtras().getString("acquisitionDate");
        this.itemDescriptionText = getIntent().getExtras().getString("description");
        this.itemCollectionID = getIntent().getExtras().getString("collectionID");
        this.imageUrl = getIntent().getExtras().getString("image");

        // init component fields
        this.itemImage = (ImageView) findViewById(R.id.item_edit_Image);
        this.itemTitle = (TextView) findViewById(R.id.title_edit_textview);
        this.date = (TextView) findViewById(R.id.date_edit_textview);
        this.itemDescription = (TextView) findViewById(R.id.description_edit_textview);
        this.editButton = (CircleImageView) findViewById(R.id.item_edit_image_btn);
        this.addButton = (Button) findViewById(R.id.edit_button);
        this.discardButton = (Button) findViewById(R.id.edit_discard_button);
        bottomNav = (BottomAppBar) findViewById(R.id.bottom_nav_bar);
        addItemButton = (FloatingActionButton) findViewById(R.id.bottom_nav_addItem);
        accountNav = (ActionMenuItemView) findViewById(R.id.bottomNavPerson);

        // set the data to fields
        itemTitle.setText(itemTitleText);
        date.setText(itemAcquisitionDatetext);
        itemDescription.setText(itemDescriptionText);
        if(imageUrl != null) {
            Picasso.get().load(imageUrl).into(itemImage);
        }

        // dialog listener for delete yes no option box
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        // edit image button
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(edit_item_activity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED
                ) {
                    final String[] permissions = {Manifest.permission.CAMERA};

                    ActivityCompat.requestPermissions(edit_item_activity.this,
                            permissions, REQUEST_IMAGE_CAPTURE_PERMISSION);
                } else {
                    takePhoto();
                }
            }
        });

        // discard button click event
        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(edit_item_activity.this);
                builder.setMessage("Are you sure you want to discard these fields?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener);
                builder.show();
            }
        });

        // add new item
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = itemTitle.getText().toString();
                String dateOfAcquisition = date.getText().toString();
                String description = itemDescription.getText().toString();

                if(isValidFields()) {
                    DataHelper.editCategoryItem(title, dateOfAcquisition, description, itemCollectionID, image, imageUrl, itemID);
                    finish();
                }
            }
        });

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.openIntent(edit_item_activity.this, "addNew", add_new.class);
            }
        });
        accountNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.openIntent(edit_item_activity.this, "Account nav", UserDetail.class);
            }
        });
        bottomNav.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.openIntent(edit_item_activity.this, "Home", MainActivity.class);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @Nullable String[] permissions,
                                           @Nullable int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_IMAGE_CAPTURE_PERMISSION &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_GRANTED) {
            takePhoto();
        }
    }

    private void takePhoto() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
        } catch (Exception e) {

        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            itemImage.setImageBitmap(bitmap);
            image = bitmap;
        }
    }

    private boolean isValidFields() {
        boolean isValid = true;

        if(itemTitle.getText().toString().isEmpty()) {
            itemTitle.setError("Title cannot be empty");
            isValid = false;
        }
        if(date.getText().toString().isEmpty()) {
            date.setError("Date cannot be empty");
            isValid = false;
        }

        return isValid;
    }
}