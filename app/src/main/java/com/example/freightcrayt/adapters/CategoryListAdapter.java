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

import com.example.freightcrayt.activities.CategoryDetail;
import com.example.freightcrayt.models.Collection;
import com.example.freightcrayt.R;
import com.example.freightcrayt.utils.DataHelper;
import com.example.freightcrayt.utils.IntentHelper;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class CategoryListAdapter extends ArrayAdapter<Collection> {

    public CategoryListAdapter(Context context, ArrayList<Collection> collectionArrayList) {
        super(context, R.layout.collection_list_item, R.id.personal_listItemID, collectionArrayList);
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

        // data instance
        DataHelper data = DataHelper.getInstance();

        // default image cuz items don't have images as of yet
        collectionImage.setImageResource(R.drawable.ic_baseline_person_24);

        // set the title
        collectionTitle.setText(item.title);

        // set num items
        collectionCount.setText(String.valueOf(data.getUserCategorySize(item.collectionID)) + " of " + String.valueOf(data.getUserCategoryGoal(item.collectionID)));

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

        // navigate to activity detail
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.openIntent(getContext(), item.collectionID, CategoryDetail.class);
            }
        });

        return super.getView(position, convertView, parent);
    }
}
