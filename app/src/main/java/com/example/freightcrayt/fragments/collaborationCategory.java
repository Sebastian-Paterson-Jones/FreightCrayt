package com.example.freightcrayt.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.freightcrayt.R;

public class collaborationCategory extends Fragment {

    public collaborationCategory() {
    }

    public static collaborationCategory newInstance() {
        collaborationCategory fragment = new collaborationCategory();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collaboration_category, container, false);
    }
}