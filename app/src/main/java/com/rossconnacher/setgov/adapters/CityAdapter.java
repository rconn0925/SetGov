package com.rossconnacher.setgov.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rossconnacher.setgov.R;
import com.rossconnacher.setgov.fragments.CityFragment;
import com.rossconnacher.setgov.models.City;
import com.rossconnacher.setgov.viewholders.CityViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ross on 6/15/2017.
 */

public class CityAdapter extends RecyclerView.Adapter<CityViewHolder> implements View.OnClickListener {

    private static final String TAG = "CityAdapter";

    private Context mContext;
    private List<City> mCities;
    private RecyclerView mRecyclerView;

    public CityAdapter(RecyclerView recyclerView,Context context, ArrayList<City> cities){
        this.mContext = context;
        this.mCities = cities;
        this.mRecyclerView = recyclerView;
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.city_item, parent, false);
        view.setOnClickListener(this);

        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        final City city = mCities.get(position);
        holder.cityText.setText(city.getCityName()+", "+city.getState());

        Resources res = mContext.getResources();
        String mDrawableName = city.getCityName().toLowerCase().replace(" ","");
        int resID = res.getIdentifier(mDrawableName , "drawable", mContext.getPackageName());
        holder.cityImage.setImageResource(resID);
    }

    @Override
    public int getItemCount() {
        return mCities.size();
    }

    @Override
    public void onClick(View v) {
        int itemPosition = mRecyclerView.getChildLayoutPosition(v);
        City city = mCities.get(itemPosition);

        FragmentManager fragmentManager = ((FragmentActivity)mContext).getSupportFragmentManager();
        Fragment currentFragment = CityFragment.newInstance(city);
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left).addToBackStack(TAG).replace(R.id.contentFrame, currentFragment).commit();

    }
}
