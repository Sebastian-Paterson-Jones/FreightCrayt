package com.example.freightcrayt.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freightcrayt.activities.AddItem;
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
        MaterialButton deleteButton = (MaterialButton) convertView.findViewById(R.id.personal_collectionItemDelete);
        LinearLayout container = (LinearLayout) convertView.findViewById(R.id.personal_listContainer);
        FrameLayout itemCountGraphic = (FrameLayout) convertView.findViewById(R.id.personal_itemCountDisplay);

        // set the title
        collectionTitle.setText(item.getTitle());

        // set item count graphic
        ViewGroup.LayoutParams layoutParams = itemCountGraphic.getLayoutParams();
        if (item.getSize() > item.getGoal()) {
            layoutParams.width = 125;
            itemCountGraphic.setLayoutParams(layoutParams);
        } else {
            if (item.getGoal() > 0) {
                layoutParams.width = 125*item.getSize()/item.getGoal();
                itemCountGraphic.setLayoutParams(layoutParams);
            }
        }

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

        // dialog listener for delete yes no option box
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        DataHelper.removeCategory(item.getCollectionID());
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to delete this category?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener);
                builder.show();
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
