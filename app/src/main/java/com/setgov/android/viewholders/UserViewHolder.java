package com.setgov.android.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.setgov.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ross on 6/18/2017.
 */

public class UserViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.personImageView)
    public CircleImageView personImageView;

    public UserViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }
}
