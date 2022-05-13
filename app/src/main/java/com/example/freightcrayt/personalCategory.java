package com.example.freightcrayt;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

public class personalCategory extends Fragment {

    public personalCategory() {
    }

    public static personalCategory newInstance() {
        personalCategory fragment = new personalCategory();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_catagory, container, false);

        // create and instantiate dummy data
        DataHelper data = DataHelper.getInstance();

        data.addUserCategory("memes", "good ol memes", "1");
        data.addUserCategory("toys", "good ol toys", "2");
        data.addUserCategory("movies", "Movies", "3");

        // assign data to adapter
        categoryItemListAdapter itemListAdapter = new categoryItemListAdapter(getContext(), data.getUserCategories());

        // assign adapter to listview
        ListView categoriesList = (ListView) view.findViewById(R.id.personal_catergoriesListView);
        categoriesList.setAdapter(itemListAdapter);

        return view;
    }
}