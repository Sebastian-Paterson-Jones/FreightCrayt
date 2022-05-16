package com.example.freightcrayt.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.freightcrayt.utils.IntentHelper;
import com.example.freightcrayt.R;
import com.example.freightcrayt.adapters.TabAdapter;
import com.example.freightcrayt.fragments.collaborationCategory;
import com.example.freightcrayt.fragments.personalCategory;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    // pagers for logic
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // redirect to login screen if not logged in
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser().getUid().isEmpty()){
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
