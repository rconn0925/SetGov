package com.rossconnacher.setgov.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rossconnacher.setgov.R;
import com.rossconnacher.setgov.SimpleDividerItemDecoration;
import com.rossconnacher.setgov.adapters.EventAdapter;
import com.rossconnacher.setgov.models.City;
import com.rossconnacher.setgov.models.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CityEventsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CityEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CityEventsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "city";
    private static final String TAG ="CityEventsFragment";
    @InjectView(R.id.eventRecyclerView)
    public RecyclerView eventView;

    private OnFragmentInteractionListener mListener;
    private City mCity;
    private ArrayList<Event> mEvents;
    private GridLayoutManager mEventLayoutManager;
    private EventAdapter mEventAdapter;

    public CityEventsFragment() {
        // Required empty public constructor
    }


    public static CityEventsFragment newInstance(City city) {
        CityEventsFragment fragment = new CityEventsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCity = (City)getArguments().getSerializable(ARG_PARAM1);
        }
        mEvents= new ArrayList<Event>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_city, container, false);
        ButterKnife.inject(this,view);
        getEvents(mCity.getCityName());
        return view;
    }

    public void getEvents(String city){
        Log.d(TAG,"Get events for "+ city);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String eventsJson = prefs.getString(city+"Events","null");
        Log.d(TAG,"Event JSON: "+ eventsJson);
        if(!eventsJson.equals(null)){
            try{
                JSONObject eventsJsonObj = new JSONObject(eventsJson);
                JSONArray eventsJsonArray = eventsJsonObj.getJSONArray("upcomingEvents");
                for(int i = 0; i < eventsJsonArray.length();i++){
                    Log.d(TAG,"Event "+ i);
                    Event event = new Event(eventsJsonArray.getJSONObject(i));
                    mEvents.add(event);
                }
            } catch (JSONException e){
            }
        }
        mEventLayoutManager = new GridLayoutManager(getActivity(), 1);
        eventView.setLayoutManager(mEventLayoutManager);
        eventView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mEventAdapter = new EventAdapter(eventView,getActivity(), mEvents);
        eventView.setAdapter(mEventAdapter);
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
