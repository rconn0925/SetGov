package com.setgov.android.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.setgov.android.R;
import com.setgov.android.fragments.CityFragment;
import com.setgov.android.models.City;
import com.setgov.android.models.User;
import com.setgov.android.viewholders.CityViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private User mUser;

    public CityAdapter(User user, RecyclerView recyclerView, Context context, ArrayList<City> cities){
        this.mContext = context;
        this.mCities = cities;
        this.mRecyclerView = recyclerView;
        this.mUser = user;
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

    public boolean cityHasEvents(String city) {
        Log.d(TAG, "Get events for " + city);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
        String eventsJson = prefs.getString(city + "Events", "null");
        Log.d(TAG, "Event JSON: " + eventsJson);
        if (!eventsJson.equals("null")) {
            try {
                JSONObject eventsJsonObj = new JSONObject(eventsJson);
                JSONArray eventsJsonArray = eventsJsonObj.getJSONArray("upcomingEvents");
                if(eventsJsonArray.length()==0){
                    return false;
                } else if(eventsJsonArray.length()>0){
                    return true;
                }
            } catch (JSONException e) {
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int itemPosition = mRecyclerView.getChildLayoutPosition(v);
        City city = mCities.get(itemPosition);
        if(cityHasEvents(city.getCityName())){
            SharedPreferences prefs = mContext.getApplicationContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
            prefs.edit().putString("userHomeCity",city.getCityName()).apply();

            FragmentManager fragmentManager = ((FragmentActivity)mContext).getSupportFragmentManager();
            Fragment currentFragment = CityFragment.newInstance(mUser,city);
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(TAG).replace(R.id.contentFrame, currentFragment).commit();
        } else {
            Toast.makeText(mContext.getApplicationContext(), "No events for "+ city.toString()+".",
                    Toast.LENGTH_SHORT).show();
        }

    }
}
