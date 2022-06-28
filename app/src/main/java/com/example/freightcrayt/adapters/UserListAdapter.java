package com.example.freightcrayt.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freightcrayt.R;
import com.example.freightcrayt.models.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends FirebaseRecyclerAdapter<User, UserListAdapter.UserView> {

    public UserListAdapter(@NonNull FirebaseRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserView holder, int position, @NonNull User model) {
        // set the username
        holder.userName.setText(model.getUserName());

        // event when user is selected
        holder.userContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public UserView onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);

        return new UserListAdapter.UserView(view);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class UserView extends RecyclerView.ViewHolder {

        TextView userName;
        CircleImageView userImage;
        LinearLayout userContainer;

        public UserView(View view) {
            super(view);

            userName = (TextView) view.findViewById(R.id.user_list_item_name);
            userImage = (CircleImageView) view.findViewById(R.id.user_list_item_image);
            userContainer = (LinearLayout) view.findViewById(R.id.user_list_item_container);
        }
    }
}
