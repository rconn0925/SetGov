package com.rossconnacher.setgov.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rossconnacher.setgov.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Ross on 6/15/2017.
 */

public class CityViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.cityImage)
    public ImageView cityImage;
    @InjectView(R.id.cityText)
    public TextView cityText;

    public CityViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this,itemView);
    }
}
