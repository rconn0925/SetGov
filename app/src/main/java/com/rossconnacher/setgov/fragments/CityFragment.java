package com.rossconnacher.setgov.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rossconnacher.setgov.R;
import com.rossconnacher.setgov.SimpleDividerItemDecoration;
import com.rossconnacher.setgov.adapters.EventAdapter;
import com.rossconnacher.setgov.models.City;
import com.rossconnacher.setgov.models.Event;
import com.rossconnacher.setgov.models.Person;

import java.util.ArrayList;
import java.util.Calendar;

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

    // TODO: Rename and change types of parameters


    @InjectView(R.id.eventRecyclerView)
    public RecyclerView eventView;
    private ArrayList<Event> mEvents;
    private GridLayoutManager mEventLayoutManager;
    private EventAdapter mEventAdapter;
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
        mEvents= new ArrayList<Event>();

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_city, container, false);
        ButterKnife.inject(this,view);
        TextView toolbarTitle = (TextView) getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(mCity.toString());
      //  toolbarTitle.setText(mCity.toString());
        if(mCity.toString().equals("Boston, MA")){
            //add boston events
            ArrayList<Person> attendees = new ArrayList<>();
            Resources resources = this.getResources();
            final int personResourceId = resources.getIdentifier("kappaross", "drawable",
                    getActivity().getPackageName());

            Person person1 = new Person("KappaRoss",personResourceId);
            Person person2 = new Person("KappaRoss",personResourceId);
            Person person3 = new Person("KappaRoss",personResourceId);
            Person person4 = new Person("KappaRoss",personResourceId);
            Person person5 = new Person("KappaRoss",personResourceId);
            attendees.add(person1);
            attendees.add(person2);
            attendees.add(person3);
            attendees.add(person4);
            attendees.add(person5);

            final int eventImageResourceId = resources.getIdentifier("firestation", "drawable",
                    getActivity().getPackageName());
            mEvents.add(new Event("City Council","Meeting", mCity, Calendar.getInstance().getTime(),"1 City Hall Square",new String[]{"enviromental","legislation"},attendees, eventImageResourceId));
            mEvents.add(new Event("City Council","Meeting", mCity, Calendar.getInstance().getTime(),"1 City Hall Square",new String[]{"enviromental","legislation"},attendees, eventImageResourceId));
            mEvents.add(new Event("City Council","Meeting", mCity, Calendar.getInstance().getTime(),"1 City Hall Square",new String[]{"enviromental","legislation"},attendees, eventImageResourceId));

        } else if(mCity.toString().equals("Fort Lauderdale, FL")){
            //add fort lauderdale events

        } else if(mCity.toString().equals("New York, NY")){
            //add new york events

        }
        mEventLayoutManager = new GridLayoutManager(getActivity(), 1);
        eventView.setLayoutManager(mEventLayoutManager);
        eventView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mEventAdapter = new EventAdapter(eventView,getActivity(), mEvents);
        eventView.setAdapter(mEventAdapter);

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
