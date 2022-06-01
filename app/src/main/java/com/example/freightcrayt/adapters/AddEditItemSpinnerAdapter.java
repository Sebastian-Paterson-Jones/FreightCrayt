package com.example.freightcrayt.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freightcrayt.R;
import com.example.freightcrayt.models.Collection;

import java.util.ArrayList;

public class AddEditItemSpinnerAdapter extends ArrayAdapter<Collection> {

    private ArrayList<Collection> items;

    public AddEditItemSpinnerAdapter(Context context, ArrayList<Collection> collectionArrayList) {
        super(context, R.layout.collections_spinner_items, R.id.spinner_Item_Title, collectionArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Collection item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.collections_spinner_items, parent, false);
        }

        // get fields
        TextView title = (TextView) convertView.findViewById(R.id.spinner_Item_Title);
        LinearLayout container = (LinearLayout) convertView.findViewById(R.id.spinner_item);

        // set the title
        title.setText(item.title);

        return super.getView(position, convertView, parent);
    }

    @Nullable
    @Override
    public Collection getItem(int position) {
        return super.getItem(position);
    }
}
