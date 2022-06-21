package com.example.freightcrayt.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.freightcrayt.R;
import com.example.freightcrayt.utils.DataHelper;
import com.example.freightcrayt.utils.IntentHelper;
import com.google.firebase.auth.FirebaseAuth;

public class add_category extends AppCompatActivity {

    // data helper
    DataHelper data;

    // fields
    EditText categoryName;
    EditText categoryGoal;
    EditText categoryDescription;
    AppCompatButton deleteButton;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        // broadcast to finish activity on logout
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        }, intentFilter);

        // set fields
        categoryName = findViewById(R.id.add_category_name);
        categoryGoal = findViewById(R.id.add_category_goal);
        categoryDescription = findViewById(R.id.add_category_description);
        deleteButton = findViewById(R.id.add_category_discard_btn);
        submitButton = findViewById(R.id.add_category_submit);

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

        // set event listeners
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(add_category.this);
                builder.setMessage("Are you sure you want to discard changes?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener);
                builder.show();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validFields()) {
                    String name = categoryName.getText().toString();
                    int goal = Integer.parseInt(categoryGoal.getText().toString());
                    String description = categoryDescription.getText().toString();

                    DataHelper.addUserCategory(name, goal, description);
                    IntentHelper.openIntent(add_category.this, "Item Created", MainActivity.class);
                }
            }
        });
    }

    private boolean validFields() {
        boolean isValid = true;

        if(categoryName.getText().toString().isEmpty()) {
            isValid = false;
            categoryName.setError("Name cannot be empty");
        }
        if(categoryGoal.getText().toString().isEmpty()) {
            isValid = false;
            categoryGoal.setError("Goal cannot be empty");
        } else {
            try {
                Integer.parseInt(categoryGoal.getText().toString());
            } catch (Exception e) {
                isValid = false;
                categoryGoal.setError("Goal must be an integer");
            }
        }

        return isValid;
    }
}