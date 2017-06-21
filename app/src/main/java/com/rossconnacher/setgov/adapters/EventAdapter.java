package com.rossconnacher.setgov.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rossconnacher.setgov.R;
import com.rossconnacher.setgov.fragments.EventInfoFragment;
import com.rossconnacher.setgov.models.Event;
import com.rossconnacher.setgov.models.Person;
import com.rossconnacher.setgov.viewholders.EventViewHolder;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ross on 6/16/2017.
 */

public class EventAdapter  extends RecyclerView.Adapter<EventViewHolder> implements View.OnClickListener{
    private static final String TAG = "EventAdapter";
    private Context mContext;
    private List<Event> mEvents;
    private RecyclerView mRecyclerView;


    public EventAdapter(RecyclerView view, Context context, ArrayList<Event> events){
        this.mContext = context;
        this.mEvents = events;
        this.mRecyclerView = view;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.event_item, parent, false);
        view.setOnClickListener(this);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        final Event event = mEvents.get(position);
        holder.eventName.setText(event.getName());
        holder.eventType.setText(event.getType());
        holder.eventTag1.setText(event.getTags()[0]);
        if(event.getTags().length>1){
            holder.eventTag2.setText(event.getTags()[1]);
        }
        holder.eventDate.setText(new DateFormatSymbols().getMonths()[event.getDate().getMonth()] + " "+ event.getDate().getDate());
       // holder.eventImage.setImageBitmap(event.getImage());
        ArrayList<Person> personList = event.getAttendees();
        holder.eventAttendees.setAdapter(new PersonAdapter(mContext,personList));
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        holder.eventAttendees.setLayoutManager(layoutManager);
        holder.eventImage.setImageResource(event.getImageResID());

    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    @Override
    public void onClick(View v) {
        int itemPosition = mRecyclerView.getChildLayoutPosition(v);
        Event event = mEvents.get(itemPosition);

        FragmentManager fragmentManager = ((FragmentActivity)mContext).getSupportFragmentManager();
        Fragment currentFragment = EventInfoFragment.newInstance(event);
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(TAG).replace(R.id.contentFrame, currentFragment).commit();

    }
}
