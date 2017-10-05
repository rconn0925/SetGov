package com.setgov.android.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.setgov.android.R;
import com.setgov.android.models.Event;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class RSVPFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "event";
    @InjectView(R.id.rsvpBackground)
    public RelativeLayout rsvpBackground;
    @InjectView(R.id.rsvpAddCalendarButton)
    public TextView rsvpAddCalendarButton;
    @InjectView(R.id.rsvpInviteFriendsButton)
    public TextView rsvpInviteFriendsButton;
    @InjectView(R.id.rsvp_event_date)
    public TextView rsvpEventDate;
    @InjectView(R.id.rsvp_event_name)
    public TextView rsvpEventName;
    @InjectView(R.id.rsvp_event_time)
    public TextView rsvpEventTime;

    private static final String TAG = "RSVPFragment";


    private OnFragmentInteractionListener mListener;
    private Event mEvent;

    public RSVPFragment() {
        // Required empty public constructor

    }


    public static RSVPFragment newInstance(Event event) {
        RSVPFragment fragment = new RSVPFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, event);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEvent = (Event)getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.rsvp_layout, container, false);
        ButterKnife.inject(this,view);
        rsvpAddCalendarButton.setOnClickListener(this);
        rsvpBackground.setOnClickListener(this);
        rsvpInviteFriendsButton.setOnClickListener(this);

        rsvpEventName.setText(mEvent.getName());
        rsvpEventDate.setText(mEvent.getDateStr());
        rsvpEventTime.setText(mEvent.getTime());

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==rsvpAddCalendarButton.getId()){

                Log.d(TAG,"Event datestr: " + mEvent.getDateTimeStamp().getTime());
              //  Log.d(TAG,"current datestr: " + Calendar.getInstance().getTime().getTime());
                long startTime = mEvent.getDateTimeStamp().getTime();
                long endTime = startTime + 3600000;


            Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime)
                        .putExtra(CalendarContract.Events.TITLE, mEvent.getName())
                        .putExtra(CalendarContract.Events.DESCRIPTION, mEvent.getDescription())
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, mEvent.getAddress())
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                      //  .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
                startActivity(intent);


        } else if(v.getId()==rsvpInviteFriendsButton.getId()){
            Intent i = new Intent(android.content.Intent.ACTION_VIEW);
            i.putExtra("sms_body", "Come to this event");
            i.setType("vnd.android-dir/mms-sms");
            try {
                startActivity(i);
            } catch (ActivityNotFoundException e) {
                // Display some sort of error message here.
            }
        } else if(v.getId()==rsvpBackground.getId()){

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.contentFrame);
            if(fragment != null){
                Log.d(TAG,"is this null???");
                FragmentTransaction fragTransaction =fragmentManager.beginTransaction();
                fragTransaction.detach(fragment);
                fragTransaction.attach(fragment);
                fragTransaction.commitAllowingStateLoss();
            } else {
                Log.d(TAG,"you messed up!");
            }


            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.exit_to_right,R.anim.enter_from_left).remove(this).commit();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
