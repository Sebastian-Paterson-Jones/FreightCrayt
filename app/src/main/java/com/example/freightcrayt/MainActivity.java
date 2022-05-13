package com.example.freightcrayt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    // pagers for logic
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check if user authenticated
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null){
            IntentHelper.openIntent(MainActivity.this, "Not logged in", Login.class);
            return;
        }

        setContentView(R.layout.activity_main);

        viewPager = (ViewPager2) findViewById(R.id.mainViewPager);
        tabLayout = (TabLayout) findViewById(R.id.mainTabLayout);

        adapter = new TabAdapter(getSupportFragmentManager(), getLifecycle());
        adapter.addFragment(personalCategory.newInstance());
        adapter.addFragment(collaborationCategory.newInstance());

        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0) {
                    tab.setText("Personal");
                } else {
                    tab.setText("Collaboration");
                }
            }
        }).attach();
        viewPager.setCurrentItem(0);
    }
}
