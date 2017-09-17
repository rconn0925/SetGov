package com.setgov.android.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.setgov.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Ross on 6/15/2017.
 */

public class CityViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.cityImage)
    public ImageView cityImage;
    @InjectView(R.id.cityNameText)
    public TextView cityNameText;
    @InjectView(R.id.cityStateText)
    public TextView cityStateText;
    @InjectView(R.id.cityComingSoon)
    public TextView cityComingSoon;
    @InjectView(R.id.cityBackground)
    public RelativeLayout cityBackground;

    public CityViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this,itemView);
    }
}
