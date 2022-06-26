package com.example.freightcrayt.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.freightcrayt.models.Collection;
import com.example.freightcrayt.utils.DataHelper;
import com.example.freightcrayt.utils.IntentHelper;
import com.example.freightcrayt.R;
import com.example.freightcrayt.adapters.TabAdapter;
import com.example.freightcrayt.fragments.collaborationCategory;
import com.example.freightcrayt.fragments.personalCategory;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    // bottom nav functionality
    private BottomAppBar bottomNav;
    private FloatingActionButton addItemButton;
    private ActionMenuItemView accountNav;

    // pagers for logic
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    // scrim
    private FrameLayout scrim;


    // for user collaborations picker
    private static final int RESULT_PICK_CONTACT = 1;

    // current collaboration collection
    Collection collection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // redirect to login screen if not logged in
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null){
            IntentHelper.openIntent(MainActivity.this, "Not logged in", Login.class);
            finish();
        }

        setContentView(R.layout.activity_main);

        // set fields
        viewPager = (ViewPager2) findViewById(R.id.mainViewPager);
        tabLayout = (TabLayout) findViewById(R.id.mainTabLayout);
        bottomNav = (BottomAppBar) findViewById(R.id.bottom_nav_bar);
        addItemButton = (FloatingActionButton) findViewById(R.id.bottom_nav_addItem);
        accountNav = (ActionMenuItemView) findViewById(R.id.bottomNavPerson);

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

        // set listeners
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.openIntent(MainActivity.this, "addNew", add_new.class);
            }
        });
        accountNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.openIntent(MainActivity.this, "Account nav", UserDetail.class);
            }
        });
    }

    public void showCollaborationOverlay(Collection collection) {
        this.collection = collection;
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Email.CONTENT_URI);
        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_PICK_CONTACT) {
                contactPicked(data);
            }
        } else {
            Log.e("ContactFragment", "Failed to pick contact");
        }
    }



    private void contactPicked(Intent data) {
        String[] emails = data.getStringArrayExtra(ContactsContract.CommonDataKinds.Email.DATA);
        if (emails != null) {
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference userRef = db.getReference("Users");
            for(String email : emails) {
                userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            // if user is null don't add to collaboration
                            if(userSnapshot.getValue() == null) {
                                Toast.makeText(MainActivity.this, "Email" + email + "not found", Toast.LENGTH_SHORT).show();
                                continue;
                            } else {
                                String userId = userSnapshot.getKey();
                                if(DataHelper.addUserToCategory(collection.getCollectionID(), userId)) {
                                    Toast.makeText(MainActivity.this, "Added " + email + " to collection", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Failed to add " + email + " to collection", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("ContactFragment", "Not a valid user");
                    }
                });
            }
        } else {
            Toast.makeText(this, "contact does not have an email", Toast.LENGTH_LONG).show();
        }
    }
}
