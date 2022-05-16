package com.example.freightcrayt.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.freightcrayt.R;
import com.example.freightcrayt.utils.IntentHelper;
import com.google.android.material.bottomappbar.BottomAppBar;

public class UserDetail extends AppCompatActivity {

    private ImageView userImage;
    private TextView userEmail;
    private TextView userName;
    private Button updateButton;
    private Button deleteButton;
    private TextView totalCrates;
    private TextView totalItems;
    private Button signOut;
    private BottomAppBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        // initiate fields
        userImage = (ImageView) findViewById(R.id.user_userImage);
        userEmail = (TextView) findViewById(R.id.user_txtboxEmail);
        userName = (TextView) findViewById(R.id.user_txtboxUsername);
        updateButton = (Button) findViewById(R.id.user_btnUpdate);
        deleteButton = (Button) findViewById(R.id.user_btnDelete);
        totalCrates = (TextView) findViewById(R.id.user_totalCrates);
        totalItems = (TextView) findViewById(R.id.user_totalItems);
        signOut = (Button) findViewById(R.id.user_signOut);
        bottomBar = (BottomAppBar) findViewById(R.id.user_bottomAppBar);

        // On click handlers
        bottomBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.openIntent(UserDetail.this, "Home", MainActivity.class);
            }
        });
    }
}