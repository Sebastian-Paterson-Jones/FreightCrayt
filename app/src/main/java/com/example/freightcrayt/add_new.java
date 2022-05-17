package com.example.freightcrayt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class add_new extends AppCompatActivity {

    private Button btnAddCrayt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);


        btnAddCrayt = (Button) (findViewById(R.id.add_crayt_btn));
        btnAddCrayt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openAddCraytActivity();

            }
        });
    }

    private void openAddCraytActivity()
    {
        Intent intent = new Intent(this, add_category.class);
        startActivity(intent);

    }
}