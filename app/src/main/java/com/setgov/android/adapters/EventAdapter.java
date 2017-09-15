package com.setgov.android.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.setgov.android.R;
import com.setgov.android.fragments.EventInfoFragment;
import com.setgov.android.models.Event;
import com.setgov.android.models.User;
import com.setgov.android.viewholders.EventViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

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
    private User mUser;


    public EventAdapter(RecyclerView view, Context context, ArrayList<Event> events){
        this.mContext = context;
        this.mEvents = events;
        this.mRecyclerView = view;
        SharedPreferences sp = mContext.getApplicationContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
        String userJson = sp.getString("loggedInUserJson","");
        try {
            this.mUser = new User(new JSONObject(userJson));
        } catch (JSONException e) {
            e.printStackTrace();
        }

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


        ArrayList<User> userList = event.getAttendees();
        holder.eventAttendees.setAdapter(new UserAdapter(mContext,userList));
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        holder.eventAttendees.setLayoutManager(layoutManager);


        holder.eventDateTime.setText(event.getDateStr()+" @ "+ event.getTime());
        holder.eventNumberGoing.setText(event.getAttendees().size()+" going ");
        //check if user is attending
        if(mUser.isAttendingEvent(event.getId())){
            holder.eventTag.setText(R.string.attending);
            holder.eventTag.setBackgroundResource(R.drawable.rounded_border_green);
        } else {
            holder.eventTag.setText(event.getDescription());
            holder.eventTag.setBackgroundResource(R.drawable.rounded_border_purple);
        }



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
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack("EventInfoFragment").replace(R.id.contentFrame, currentFragment).commit();

    }
}
