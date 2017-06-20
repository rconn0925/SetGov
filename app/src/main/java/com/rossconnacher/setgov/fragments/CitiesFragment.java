package com.rossconnacher.setgov.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rossconnacher.setgov.R;
import com.rossconnacher.setgov.SimpleDividerItemDecoration;
import com.rossconnacher.setgov.adapters.CityAdapter;
import com.rossconnacher.setgov.models.City;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CitiesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CitiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CitiesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @InjectView(R.id.citiesRecyclerView)
    public RecyclerView citiesView;
    private ArrayList<City> mCities;
    private GridLayoutManager mCityLayoutManager;
    private CityAdapter mCityAdapter;

    private OnFragmentInteractionListener mListener;

    public CitiesFragment() {
        // Required empty public constructor
    }

    public static CitiesFragment newInstance() {
        CitiesFragment fragment = new CitiesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCities = new ArrayList<>();

        City boston = new City("Boston","MA");
        City fortlauderdale = new City("Fort Lauderdale","FL");
        City newyork = new City("New York","NY");

        mCities.add(boston);
        mCities.add(fortlauderdale);
        mCities.add(newyork);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_cities, container, false);
        ButterKnife.inject(this,view);
        mCityLayoutManager = new GridLayoutManager(getActivity(), 1);
        citiesView.setLayoutManager(mCityLayoutManager);
        citiesView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mCityAdapter = new CityAdapter(citiesView,getActivity(), mCities);
        citiesView.setAdapter(mCityAdapter);

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
