package com.setgov.android.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.setgov.android.eventscrapers.BostonEventScraper;
import com.setgov.android.eventscrapers.ForLauderdaleEventScraper;
import com.setgov.android.R;
import com.setgov.android.fragments.AgendaInfoFragment;
import com.setgov.android.fragments.ChangeMyCityFragment;
import com.setgov.android.fragments.CitiesFragment;
import com.setgov.android.fragments.CityEventsFragment;
import com.setgov.android.fragments.CityFragment;
import com.setgov.android.fragments.CityInfoFragment;
import com.setgov.android.fragments.EventInfoFragment;
import com.setgov.android.fragments.RSVPFragment;
import com.setgov.android.fragments.SettingsFragment;
import com.setgov.android.fragments.WhereToVoteFragment;
import com.setgov.android.models.Event;
import com.setgov.android.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity implements
        CitiesFragment.OnFragmentInteractionListener,
        CityFragment.OnFragmentInteractionListener,
        EventInfoFragment.OnFragmentInteractionListener,
        AgendaInfoFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener,
        CityEventsFragment.OnFragmentInteractionListener,
        CityInfoFragment.OnFragmentInteractionListener,
        RSVPFragment.OnFragmentInteractionListener,
        ChangeMyCityFragment.OnFragmentInteractionListener,
        //WhereToVoteFragment.OnFragmentInteractionListener,
        View.OnClickListener{

    private static final String TAG = "MainActivity";

    @InjectView(R.id.toolbar)
    public Toolbar toolbar;
    @InjectView(R.id.settingsButton)
    public ImageView settingsButton;
    @InjectView(R.id.toolbarTitle)
    public TextView toolbarTitle;

    ArrayList<Event> mEvents = new ArrayList<>();
    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        settingsButton.setOnClickListener(this);
        mUser = (User)getIntent().getSerializableExtra("User");
        if(mUser==null){
            SharedPreferences sp = getApplicationContext().getSharedPreferences
                    ("auth", Context.MODE_PRIVATE);
            String userJson = sp.getString("loggedInUserJson","");
            try {
                mUser = new User(new JSONObject(userJson));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

       // Typeface tf = new Typeface();
        //toolbarTitle.setTypeface(tf);
        //scrapeBostonHTML();
        //scrapeFortLauderdaleExcel();

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = CitiesFragment.newInstance(mUser);
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.exit_to_right,R.anim.enter_from_left).replace(R.id.contentFrame, currentFragment).commit();

    }

    public void scrapeBostonHTML(){
        try {
            mEvents = new BostonEventScraper().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public void scrapeFortLauderdaleExcel(){
        try {
            mEvents = new ForLauderdaleEventScraper(this).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        settingsButton.setImageDrawable(null);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==settingsButton.getId()){
            Log.d(TAG,"settings button pressed");
            FragmentManager fragmentManager = getSupportFragmentManager();
            if(settingsButton.getTag().toString().equals(getString(R.string.settings))){
                Fragment currentFragment = SettingsFragment.newInstance();
                settingsButton.setImageResource(R.drawable.ic_arrow_back_black_24dp);
                settingsButton.setTag(getString(R.string.back));
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack("Settings").replace(R.id.contentFrame, currentFragment).commit();
            } else if (settingsButton.getTag().toString().equals(getString(R.string.back))){
                int drawableResourceId = this.getResources().getIdentifier("account_circle_white_48x48", "drawable-nodpi", this.getPackageName());
                settingsButton.setImageResource(drawableResourceId);
                settingsButton.setTag(getString(R.string.settings));
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                } else {
                    super.onBackPressed();
                }
            }
        }
    }
}
