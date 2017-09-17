package com.setgov.android.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.setgov.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Ross on 9/16/2017.
 */

public class AgendaPDFViewHolder extends RecyclerView.ViewHolder  {


    @InjectView(R.id.agendaCategory)
    public TextView agendaCategory;


    public AgendaPDFViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this,itemView);
    }
}