package com.example.freightcrayt.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freightcrayt.R;
import com.example.freightcrayt.adapters.AddEditItemSpinnerAdapter;
import com.example.freightcrayt.models.Collection;
import com.example.freightcrayt.utils.DataHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddItem extends AppCompatActivity {

    // camera permission final ints
    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private static final int REQUEST_IMAGE_CAPTURE_PERMISSION = 100;
    private Button dateButton;
    private DatePickerDialog datePickerDialog;
    // fields
    Bitmap image;
    ImageView itemImage;
    TextView itemTitle;
    TextView date;
    TextView itemDescription;
    Spinner categoryChoice;
    CircleImageView editButton;
    Button addButton;
    Button discardButton;
    String collectionID;
    String collectionTitle;
    String collectionDescription;
    int collectionGoal;
    int collectionSize;

    // items adapter
    AddEditItemSpinnerAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        initDatePicker();

        // broadcast to finish activity on logout
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        }, intentFilter);

        // set the collection details
        this.collectionID = getIntent().getExtras().getString("collectionID");
        this.collectionTitle = getIntent().getExtras().getString("title");
        this.collectionDescription = getIntent().getExtras().getString("description");
        this.collectionGoal = getIntent().getExtras().getInt("goal");
        this.collectionSize = getIntent().getExtras().getInt("size");

        // init fields
        itemImage = (ImageView) findViewById(R.id.item_add_Image);
        itemTitle = (TextView) findViewById(R.id.title_textview);

        //date = (TextView) findViewById(R.id.date_textview);

        dateButton = findViewById(R.id.date_textview);

        dateButton.setText(getTodaysDate());

        itemDescription = (TextView) findViewById(R.id.description_textview);
        editButton = (CircleImageView) findViewById(R.id.item_add_image_btn);
        addButton = (Button) findViewById(R.id.add_button);
        discardButton = (Button) findViewById(R.id.discard_button);
        categoryChoice = (Spinner) findViewById(R.id.category_dropdown);

        ArrayList<Collection> items = new ArrayList<>();

        // for populating the categories dropdown
        if (this.collectionID != null && this.collectionTitle != null) {
            items.add(new Collection(collectionTitle, collectionGoal, collectionDescription, collectionID, collectionSize));
        } else {
            // retrieve user collections
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference userCollectionsRef = db.getReference("UserCategories");
            DatabaseReference collectionsRef = db.getReference("Categories");
            DatabaseReference collaborationsRef = db.getReference("CollectionCollaborations");

            // get user categories
            userCollectionsRef.child(DataHelper.getUserID()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot collectionsIDSnap : snapshot.getChildren()) {
                        String collectionID = collectionsIDSnap.getValue(String.class);

                        collectionsRef.child(collectionID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Collection collection = snapshot.getValue(Collection.class);
                                if (collection != null) {
                                    items.add(collection);
                                    refreshAdapter(items);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(AddItem.this, "failed to retrieve collections", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(AddItem.this, "failed to retrieve collections", Toast.LENGTH_SHORT).show();
                }
            });


            // get user collaborations they are invited to
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
                                                items.add(collection);
                                                refreshAdapter(items);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(AddItem.this, "failed to retrieve collections", Toast.LENGTH_SHORT).show();
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
        }

        adapter = new AddEditItemSpinnerAdapter(AddItem.this, items);

        categoryChoice.setAdapter(adapter);

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

                if (ActivityCompat.checkSelfPermission(AddItem.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED
                ) {
                    final String[] permissions = {Manifest.permission.CAMERA};

                    ActivityCompat.requestPermissions(AddItem.this,
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
                AlertDialog.Builder builder = new AlertDialog.Builder(AddItem.this);
                builder.setMessage("Are you sure you want to discard this item?")
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
                Collection collection = (Collection) categoryChoice.getSelectedItem();
                String description = itemDescription.getText().toString();

                if(isValidFields()) {
                    DataHelper.addCategoryItem(title, dateOfAcquisition, description, collection.getCollectionID(), image, collectionSize);
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
        //if(date.getText().toString().isEmpty()) {
            //date.setError("Date cannot be empty");
           // isValid = false;
       // }
        if(!categoryChoice.isSelected()) {
            categoryChoice.setSelection(0);
        }

        return isValid;
    }

    private void refreshAdapter(ArrayList<Collection> items) {
        adapter = new AddEditItemSpinnerAdapter(AddItem.this, items);
        categoryChoice.setAdapter(adapter);
    }


    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month +1;
        int day = cal.get(Calendar.DAY_OF_WEEK);
        return makeDateString(day,month,year);
    }
    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                month = month +1;
                String date = makeDateString(dayOfMonth,month,year);
                dateButton.setText(date);

            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_WEEK);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog (this, style, dateSetListener, day, month, year);

    }

    private String makeDateString(int dayOfMonth, int month, int year)
    {
        return dayOfMonth + " " + month + " " + year;
    }

    public void openDate(View view)
    {
        datePickerDialog.show();

    }
}