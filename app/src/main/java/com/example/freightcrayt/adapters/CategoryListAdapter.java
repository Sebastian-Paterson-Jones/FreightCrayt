package com.example.freightcrayt.adapters;

import android.content.Context;
import android.os.Bundle;
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

import com.example.freightcrayt.activities.CategoryDetail;
import com.example.freightcrayt.activities.MainActivity;
import com.example.freightcrayt.models.Collection;
import com.example.freightcrayt.R;
import com.example.freightcrayt.utils.DataHelper;
import com.example.freightcrayt.utils.IntentHelper;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryListAdapter extends ArrayAdapter<Collection> {

    Context context;

    public CategoryListAdapter(Context context, ArrayList<Collection> collectionArrayList) {
        super(context, R.layout.collection_list_item, R.id.personal_listItemID, collectionArrayList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Collection item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.collection_list_item, parent, false);
        }

        ImageView collectionImage = (ImageView) convertView.findViewById(R.id.personal_collectionItemImage);
        TextView collectionTitle = (TextView) convertView.findViewById(R.id.personal_collectionItemTitle);
        TextView collectionCount = (TextView) convertView.findViewById(R.id.personal_collectionItemCount);
        MaterialButton shareButton = (MaterialButton) convertView.findViewById(R.id.personal_collectionItemShare);
        MaterialButton editButton = (MaterialButton) convertView.findViewById(R.id.personal_collectionItemEdit);
        LinearLayout container = (LinearLayout) convertView.findViewById(R.id.personal_listContainer);

        // set the title
        collectionTitle.setText(item.getTitle());

        // set num items
        collectionCount.setText(String.valueOf(item.getSize()) + " of " + String.valueOf(item.getGoal()));

        // collaboration activate
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof MainActivity) {
                    ((MainActivity) context).showCollaborationOverlay(item);
                }
            }
        });

        // TODO: set redirect to category edit activity
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Redirect to edit category activity", Toast.LENGTH_LONG).show();
            }
        });

        // navigate to activity detail
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle intentBundle = new Bundle();
                intentBundle.putString("collectionID", item.getCollectionID());
                intentBundle.putString("title", item.getTitle());
                intentBundle.putString("description", item.getDescription());
                intentBundle.putInt("goal", item.getGoal());
                intentBundle.putInt("size", item.getSize());
                IntentHelper.openIntent(getContext(), intentBundle, CategoryDetail.class);
            }
        });

        return super.getView(position, convertView, parent);
    }
}
