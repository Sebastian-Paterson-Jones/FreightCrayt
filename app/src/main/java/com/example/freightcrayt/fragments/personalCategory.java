package com.example.freightcrayt.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.freightcrayt.utils.DataHelper;
import com.example.freightcrayt.R;
import com.example.freightcrayt.adapters.categoryItemListAdapter;
import com.google.android.material.textfield.TextInputEditText;

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

        // get the search box
        TextInputEditText searchBox = (TextInputEditText) view.findViewById(R.id.personalCategory_txtBoxSearch);

        data.addUserCategory("memes", "good ol memes", "1");
        data.addUserCategory("toys", "good ol toys", "2");
        data.addUserCategory("movies", "Movies", "3");
        data.addUserCategory("cats", "cats", "4");

        // assign data to adapter
        categoryItemListAdapter itemListAdapter = new categoryItemListAdapter(getContext(), data.getUserCategories());

        // assign adapter to listview
        ListView categoriesList = (ListView) view.findViewById(R.id.personal_catergoriesListView);
        categoriesList.setAdapter(itemListAdapter);

        // set event listener for search box filtering
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                itemListAdapter.getFilter().filter(searchBox.getText());
            }
        });

        return view;
    }
}