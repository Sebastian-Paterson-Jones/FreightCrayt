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
import android.widget.TextView;

import com.example.freightcrayt.R;
import com.example.freightcrayt.utils.DataHelper;
import com.example.freightcrayt.utils.IntentHelper;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserDetail extends AppCompatActivity {

    // Data helper for getting user credentials
    DataHelper dataHelper;

    private ImageView userImage;
    private TextView userEmail;
    private TextView userName;
    private Button updateButton;
    private Button deleteButton;
    private TextView totalCrates;
    private TextView totalItems;
    private Button signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        // broadcast to finish activity on logout
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        }, intentFilter);

        // initiate fields
        userImage = (ImageView) findViewById(R.id.user_userImage);
        userEmail = (TextView) findViewById(R.id.user_txtboxEmail);
        userName = (TextView) findViewById(R.id.user_txtboxUsername);
        updateButton = (Button) findViewById(R.id.user_btnUpdate);
        deleteButton = (Button) findViewById(R.id.user_btnDelete);
        totalCrates = (TextView) findViewById(R.id.user_totalCrates);
        totalItems = (TextView) findViewById(R.id.user_totalItems);
        signOut = (Button) findViewById(R.id.user_signOut);

        // set user details
        userImage.setImageResource(R.drawable.ic_baseline_person_24);
        userEmail.setText(dataHelper.getUserEmail());
        userName.setText(dataHelper.getUsername());
        totalCrates.setText("0" + " Crates");
        totalItems.setText("0" + " Items");

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                IntentHelper.logout(UserDetail.this);
            }
        });

        // dialog listener for delete yes no option box
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        // TODO: delete user and data
                        FirebaseAuth.getInstance().signOut();
                        IntentHelper.logout(UserDetail.this);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserDetail.this);
                builder.setMessage("Are you sure you want to delete your account?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener);
                builder.show();
            }
        });
    }
}