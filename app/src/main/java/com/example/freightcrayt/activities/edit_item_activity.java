package com.example.freightcrayt.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.freightcrayt.adapters.AddEditItemSpinnerAdapter;
import com.example.freightcrayt.models.Collection;
import com.example.freightcrayt.utils.DataHelper;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class edit_item_activity extends AppCompatActivity {

    // Data helper for getting user credentials
    DataHelper data;

    // Item ID
    private String itemID;

    // camera permission final ints
    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private static final int REQUEST_IMAGE_CAPTURE_PERMISSION = 100;

    // fields
    Bitmap image;
    ImageView itemImage;
    TextView itemTitle;
    TextView date;
    TextView itemDescription;
    Spinner categoryChoice;
    CircleImageView editButton;
    Button updateButton;
    Button discardButton;

    @Override
    protected void onResume() {
        super.onResume();

        categoryChoice = (Spinner) findViewById(R.id.category_dropdown);

        ArrayList<Collection> items = data.getUserCategories();

        AddEditItemSpinnerAdapter adapter = new AddEditItemSpinnerAdapter(edit_item_activity.this, items);

        categoryChoice.setAdapter(adapter);

        // set the collectionID
        String collectionID = getIntent().getStringExtra("extraInfo");

        if(!(collectionID == null)) {
            categoryChoice.setSelection(adapter.getPosition(data.getUserCategory(collectionID)));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // initiate dataHelper
        data = DataHelper.getInstance();

        // set the itemID
        this.itemID = getIntent().getStringExtra("extraInfo");

        // init fields
        itemImage = (ImageView) findViewById(R.id.item_add_Image);
        itemTitle = (TextView) findViewById(R.id.title_textview);
        date = (TextView) findViewById(R.id.date_textview);
        itemDescription = (TextView) findViewById(R.id.description_textview);
        editButton = (CircleImageView) findViewById(R.id.item_detail_btnEditItem);
        updateButton = (Button) findViewById(R.id.update_button);
        discardButton = (Button) findViewById(R.id.discard_button);

        // broadcast to finish activity on logout
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        }, intentFilter);

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
                builder.setMessage("Are you sure you want to discard this item?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener);
                builder.show();
            }
        });

        // update item
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = itemTitle.getText().toString();
                String dateOfAcquisition = date.getText().toString();
                Collection collection = (Collection) categoryChoice.getSelectedItem();
                String description = itemDescription.getText().toString();

                if(isValidFields()) {
                    data.addUserCategoryItem(title, dateOfAcquisition, description, collection.collectionID, image);
                    finish();
                }
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
        if(!categoryChoice.isSelected()) {
            categoryChoice.setSelection(0);
        }

        return isValid;
    }
}