package com.example.freightcrayt.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.freightcrayt.R;
import com.example.freightcrayt.utils.IntentHelper;

public class add_new extends AppCompatActivity {

    private Button btnAddCrayt;
    private Button btnAddItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        // broadcast to finish activity on logout
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        }, intentFilter);

        btnAddCrayt = (Button) (findViewById(R.id.add_crayt_btn));
        btnAddItem = (Button) (findViewById(R.id.add_item_btn));

        btnAddCrayt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                IntentHelper.openIntent(add_new.this, "Create new crate", add_category.class);
            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.openIntent(add_new.this, "", AddItem.class);
            }
        });
    }
}