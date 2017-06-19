package com.rossconnacher.setgov.activities;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.rossconnacher.setgov.R;
import com.rossconnacher.setgov.SimpleDividerItemDecoration;
import com.rossconnacher.setgov.adapters.CityAdapter;
import com.rossconnacher.setgov.models.City;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CitiesActivity extends AppCompatActivity {


    @InjectView(R.id.citiesRecyclerView)
    public RecyclerView citiesView;
    private ArrayList<City> mCities;
    private GridLayoutManager mCityLayoutManager;
    private CityAdapter mCityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cities);
        ButterKnife.inject(this);
        mCities = new ArrayList<>();

        City boston = new City("Boston","MA");
        City fortlauderdale = new City("Fort Lauderdale","FL");
        City newyork = new City("New York","NY");

        mCities.add(boston);
        mCities.add(fortlauderdale);
        mCities.add(newyork);

        mCityLayoutManager = new GridLayoutManager(this, 1);
        citiesView.setLayoutManager(mCityLayoutManager);
        citiesView.addItemDecoration(new SimpleDividerItemDecoration(this));
        mCityAdapter = new CityAdapter(citiesView,this, mCities);
        citiesView.setAdapter(mCityAdapter);

    }


}
