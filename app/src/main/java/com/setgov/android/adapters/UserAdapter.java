package com.setgov.android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.setgov.android.R;
import com.setgov.android.models.User;
import com.setgov.android.viewholders.UserViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ross on 6/18/2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder>{

    private static final String TAG = "UserAdapter";
    private Context mContext;
    private List<User> mUsers;
   // private RecyclerView mRecyclerView;

    public UserAdapter(Context context, ArrayList<User> users){
        this.mContext = context;
        this.mUsers = users;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.person_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        final User user = mUsers.get(position);
        //holder.personImageView.setImageResource(user.getImageResID());
        Picasso.with(mContext).load(user.getProfileImageUrl()).into(holder.personImageView);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
