package com.example.freightcrayt.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freightcrayt.adapters.UserListAdapter;
import com.example.freightcrayt.models.Collection;
import com.example.freightcrayt.models.User;
import com.example.freightcrayt.utils.DataHelper;
import com.example.freightcrayt.utils.IntentHelper;
import com.example.freightcrayt.R;
import com.example.freightcrayt.adapters.TabAdapter;
import com.example.freightcrayt.fragments.collaborationCategory;
import com.example.freightcrayt.fragments.personalCategory;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    // bottom nav functionality
    private FloatingActionButton addItemButton;
    private ActionMenuItemView accountNav;
    private BottomSheetBehavior bottomSheet;

    // pagers for logic
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    // Recycler View object
    private RecyclerView recyclerView;
    UserListAdapter userAdapter;
    DatabaseReference usersRef;

    // add email objects
    private Button addEmailButton;
    private TextInputEditText emailBox;

    // current select collection
    private Collection collection;

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
        addItemButton = (FloatingActionButton) findViewById(R.id.bottom_nav_addItem);
        accountNav = (ActionMenuItemView) findViewById(R.id.bottomNavPerson);
        bottomSheet = BottomSheetBehavior.from(findViewById(R.id.shareBottomSheet));
        recyclerView = (RecyclerView)findViewById(R.id.share_user_list);
        emailBox = (TextInputEditText)findViewById(R.id.collaborations_emailBox);
        addEmailButton = (Button)findViewById(R.id.btnAddEmail);

        // init user ref
        usersRef = FirebaseDatabase.getInstance().getReference();

        // Set LayoutManager on Recycler View
        LinearLayoutManager RecyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(RecyclerViewLayoutManager);

        // firebase recycler options
        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>().setQuery(usersRef, User.class).build();
        userAdapter = new UserListAdapter(options);
        recyclerView.setAdapter(userAdapter);

        adapter = new TabAdapter(getSupportFragmentManager(), getLifecycle());
        adapter.addFragment(personalCategory.newInstance());
        adapter.addFragment(collaborationCategory.newInstance());

        //set the bottom sheet behavior
        bottomSheet.setState(BottomSheetBehavior.STATE_HIDDEN);

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
        addEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = emailBox.getText().toString();
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference userRef = db.getReference("Users");
                userRef.orderByChild("userEmail").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getChildrenCount() < 1) {
                            Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        } else {
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                String userID = snap.child("userID").getValue(String.class);
                                if(userID != null) {
                                    DataHelper.addUserToCategory(collection.getCollectionID(), userID);
                                    Toast.makeText(MainActivity.this, "User added to collection", Toast.LENGTH_SHORT).show();
                                    hideCollaborationOverlay();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        return;
                    }
                });
            }
        });

        // set horizontal layout for users list if possible
        LinearLayoutManager HorizontalLayout = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);
    }

    @Override
    protected void onStart() {
        super.onStart();
        userAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        userAdapter.stopListening();
    }

    public void showCollaborationOverlay(Collection collection) {
        this.collection = collection;
        bottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void hideCollaborationOverlay() {
        bottomSheet.setState(BottomSheetBehavior.STATE_HIDDEN);
    }
}
