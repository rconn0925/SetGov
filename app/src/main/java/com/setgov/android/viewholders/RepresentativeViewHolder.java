package com.setgov.android.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.setgov.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by Ross on 9/18/2017.
 */


public class RepresentativeViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.repName)
    public TextView repName;
    @InjectView(R.id.repOfficeName)
    public TextView repOfficeName;
    //@InjectView(R.id.repMoreInfo)
    //public ImageView repMoreInfo;

    public RepresentativeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }
}
