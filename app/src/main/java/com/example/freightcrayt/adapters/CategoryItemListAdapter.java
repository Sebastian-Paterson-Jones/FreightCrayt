package com.example.freightcrayt.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freightcrayt.R;
import com.example.freightcrayt.activities.ViewItem;
import com.example.freightcrayt.models.CollectionItem;
import com.example.freightcrayt.utils.DataHelper;
import com.example.freightcrayt.utils.IntentHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.example.freightcrayt.activities.edit_item_activity;

import java.util.ArrayList;

public class CategoryItemListAdapter extends ArrayAdapter<CollectionItem> {

    private ArrayList<CollectionItem> items;

    public CategoryItemListAdapter(Context context, ArrayList<CollectionItem> collectionItemArrayList) {
        super(context, R.layout.collection_item_list_item, R.id.collection_item_title, collectionItemArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        CollectionItem item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.collection_item_list_item, parent, false);
        }

        MaterialCardView collectionItemCard = (MaterialCardView) convertView.findViewById(R.id.collection_item_card);
        ImageView collectionItemImage = (ImageView) convertView.findViewById(R.id.collection_item_image);
        TextView collectionItemTitle = (TextView) convertView.findViewById(R.id.collection_item_title);
        TextView collectionItemSubtitle = (TextView) convertView.findViewById(R.id.collection_item_subtitle);
        TextView collectionItemDescription = (TextView) convertView.findViewById(R.id.collection_item_description);
        MaterialButton editButton = (MaterialButton) convertView.findViewById(R.id.collection_item_btnEdit);
        MaterialButton deleteButton = (MaterialButton) convertView.findViewById(R.id.collection_item_btnDelete);

        // data instance
        DataHelper data = DataHelper.getInstance();

        // set image
        if(item.image != null) {
            collectionItemImage.setImageBitmap(item.image);
        }

        // set the title
        collectionItemTitle.setText(item.title);

        // set subtitle
        collectionItemSubtitle.setText(item.acquisitionDate);

        // set description
        collectionItemDescription.setText(item.description);


        // no handler just yet for redirecting to share activity
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.openIntent(getContext(), item.itemID, edit_item_activity.class);
            }
        });

        // dialog listener for delete yes no option box
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        data.removeUserCategoryItem(item.itemID);
                        CategoryItemListAdapter.super.remove(item);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        // delete item event
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener);
                builder.show();
            }
        });


        // when item card is clicked
        collectionItemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.openIntent(getContext(), item.itemID, ViewItem.class);
            }
        });

        return super.getView(position, convertView, parent);
    }
}
