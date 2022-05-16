package com.example.freightcrayt.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freightcrayt.models.CollectionItem;
import com.example.freightcrayt.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class categoryItemListAdapter extends ArrayAdapter<CollectionItem> {

    public categoryItemListAdapter(Context context, ArrayList<CollectionItem> collectionArrayList) {
        super(context, R.layout.collection_list_item, R.id.personal_listItemID, collectionArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        CollectionItem item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.collection_list_item, parent, false);
        }

        ImageView collectionItemImage = (ImageView) convertView.findViewById(R.id.personal_collectionItemImage);
        TextView collectionItemTitle = (TextView) convertView.findViewById(R.id.personal_collectionItemTitle);
        TextView collectionItemCount = (TextView) convertView.findViewById(R.id.personal_collectionItemCount);
        MaterialButton shareButton = (MaterialButton) convertView.findViewById(R.id.personal_collectionItemShare);
        MaterialButton editButton = (MaterialButton) convertView.findViewById(R.id.personal_collectionItemEdit);
        LinearLayout container = (LinearLayout) convertView.findViewById(R.id.personal_listContainer);

        // default image cuz items don't have images as of yet
        // TODO: update image
        collectionItemImage.setImageResource(R.drawable.ic_baseline_person_24);

        // set the title
        collectionItemTitle.setText(item.title);

        // default count to 0 since no logic to get count yet
        // TODO: set count variable in collectionItem class
        collectionItemCount.setText("10");


        // no handler just yet for redirecting to share activity
        // TODO: set redirect to category share activity
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Redirect to share category activity", Toast.LENGTH_LONG).show();
            }
        });

        // no handler just yet for redirecting to edit activity
        // TODO: set redirect to category edit activity
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Redirect to edit category activity", Toast.LENGTH_LONG).show();
            }
        });

        // no handler just yet for redirecting to info activity
        // TODO: set redirect to category info activity
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Redirect to info category activity", Toast.LENGTH_LONG).show();
            }
        });

        return super.getView(position, convertView, parent);
    }
}
