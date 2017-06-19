package com.rossconnacher.setgov.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rossconnacher.setgov.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Ross on 6/16/2017.
 */

public class EventViewHolder  extends RecyclerView.ViewHolder {
    @InjectView(R.id.eventImage)
    public ImageView eventImage;
    @InjectView(R.id.eventName)
    public TextView eventName;
    @InjectView(R.id.eventDate)
    public TextView eventDate;
    @InjectView(R.id.eventType)
    public TextView eventType;
    @InjectView(R.id.eventTag1)
    public TextView eventTag1;
    @InjectView(R.id.eventTag2)
    public TextView eventTag2;
    @InjectView(R.id.eventAttendeesTitle)
    public TextView eventAttendeesTitle;
    @InjectView(R.id.eventAttendees)
    public RecyclerView eventAttendees;

    public EventViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this,itemView);
    }
}
