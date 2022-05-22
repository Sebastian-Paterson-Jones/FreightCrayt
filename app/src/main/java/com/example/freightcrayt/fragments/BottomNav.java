package com.example.freightcrayt.fragments;

import android.os.Bundle;

import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.freightcrayt.R;
import com.example.freightcrayt.activities.MainActivity;
import com.example.freightcrayt.activities.UserDetail;
import com.example.freightcrayt.activities.add_new;
import com.example.freightcrayt.utils.IntentHelper;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BottomNav extends Fragment {

    private BottomAppBar bottomNav;
    private FloatingActionButton addItemButton;
    private ActionMenuItemView accountNav;

    public BottomNav() {
    }

    public static BottomNav newInstance() {
        BottomNav fragment = new BottomNav();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_nav, container, false);

        // set fields
        bottomNav = (BottomAppBar) view.findViewById(R.id.bottom_nav_bar);
        addItemButton = (FloatingActionButton) view.findViewById(R.id.bottom_nav_addItem);
        accountNav = (ActionMenuItemView) view.findViewById(R.id.bottomNavPerson);

        // set icon colours
        if(getContext().getClass().equals(MainActivity.class)) {
            bottomNav.getNavigationIcon().setTint(getResources().getColor(R.color.black));
        } else if(getContext().getClass().equals(add_new.class)) {
            addItemButton.setColorFilter(getResources().getColor(R.color.white));
        } else if(getContext().getClass().equals(UserDetail.class)) {
            accountNav.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.black));
        }

        // click handles
        bottomNav.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!getContext().getClass().equals(MainActivity.class)) {
                    IntentHelper.openIntent(getContext(), "Home", MainActivity.class);
                }
            }
        });
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!getContext().getClass().equals(add_new.class)) {
                    IntentHelper.openIntent(getContext(), "addNew", add_new.class);
                }
            }
        });
        accountNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!getContext().getClass().equals(UserDetail.class)) {
                    IntentHelper.openIntent(getContext(), "Account nav", UserDetail.class);
                }
            }
        });

        return view;
    }
}