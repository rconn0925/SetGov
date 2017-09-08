package com.rossconnacher.setgov.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rossconnacher.setgov.eventscrapers.BostonEventScraper;
import com.rossconnacher.setgov.eventscrapers.ForLauderdaleEventScraper;
import com.rossconnacher.setgov.R;
import com.rossconnacher.setgov.SimpleDividerItemDecoration;
import com.rossconnacher.setgov.adapters.EventAdapter;
import com.rossconnacher.setgov.eventscrapers.NewYorkEventScraper;
import com.rossconnacher.setgov.models.City;
import com.rossconnacher.setgov.models.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CityFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "city";

    private static final String TAG = "CityFragment";

    @InjectView(R.id.myCityButton)
    public LinearLayout MyCityButton;
    @InjectView(R.id.eventsButton)
    public LinearLayout EventsButton;
    private City mCity;

    private OnFragmentInteractionListener mListener;
    private ImageView backButton;

    public CityFragment() {
        // Required empty public constructor
    }

    public static CityFragment newInstance(City city) {
        CityFragment fragment = new CityFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.city_bottom_nav, container, false);
        ButterKnife.inject(this,view);
        TextView toolbarTitle = (TextView) getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(mCity.toString());
        ImageView toolbarImage = (ImageView) getActivity().findViewById(R.id.settingsButton);
        toolbarImage.setImageResource(R.drawable.account_circle_white_192x192);

        MyCityButton.setOnClickListener(this);
        EventsButton.setOnClickListener(this);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        setFragment(CityEventsFragment.newInstance(mCity));
        return view;
    }
    protected void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentContainer, fragment);
        fragmentTransaction.commit();
    }

    /*
    private void scrapeNewYorkEvents() {
        try {
            mEvents = new NewYorkEventScraper(getActivity()).execute().get();
            mEventLayoutManager = new GridLayoutManager(getActivity(), 1);
            eventView.setLayoutManager(mEventLayoutManager);
            eventView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
            mEventAdapter = new EventAdapter(eventView,getActivity(), mEvents);
            eventView.setAdapter(mEventAdapter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    private void scrapeFortLauderdaleEvents() {
        try {
            mEvents = new ForLauderdaleEventScraper(getActivity()).execute().get();
            mEventLayoutManager = new GridLayoutManager(getActivity(), 1);
            eventView.setLayoutManager(mEventLayoutManager);
            eventView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
            mEventAdapter = new EventAdapter(eventView,getActivity(), mEvents);
            eventView.setAdapter(mEventAdapter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void scrapeBostonEvents(){
        try {
            mEvents = new BostonEventScraper().execute().get();
            mEventLayoutManager = new GridLayoutManager(getActivity(), 1);
            eventView.setLayoutManager(mEventLayoutManager);
            eventView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
            mEventAdapter = new EventAdapter(eventView,getActivity(), mEvents);
            eventView.setAdapter(mEventAdapter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

*/
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

        if(v.getId() == MyCityButton.getId()){

            MyCityButton.setBackgroundResource(R.color.colorNavyBlue);
            EventsButton.setBackgroundResource(R.color.colorOtherBlue);
            setFragment(CityInfoFragment.newInstance(mCity));
        } else if(v.getId() == EventsButton.getId()){
            EventsButton.setBackgroundResource(R.color.colorNavyBlue);
            MyCityButton.setBackgroundResource(R.color.colorOtherBlue);
            setFragment(CityEventsFragment.newInstance(mCity));
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
