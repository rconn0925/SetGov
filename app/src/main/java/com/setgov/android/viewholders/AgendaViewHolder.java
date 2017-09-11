package com.setgov.android.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.setgov.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Ross on 6/16/2017.
 */

public class AgendaViewHolder  extends RecyclerView.ViewHolder  {

    @InjectView(R.id.agendaTitle)
    public TextView agendaTitle;
    @InjectView(R.id.agendaCategory)
    public TextView agendaCategory;
    @InjectView(R.id.agendaImage)
    public ImageView agendaImage;

    public AgendaViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this,itemView);
    }
}
