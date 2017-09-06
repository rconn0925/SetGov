package com.rossconnacher.setgov.activities;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.rossconnacher.setgov.eventscrapers.BostonEventScraper;
import com.rossconnacher.setgov.eventscrapers.ForLauderdaleEventScraper;
import com.rossconnacher.setgov.R;
import com.rossconnacher.setgov.fragments.AgendaInfoFragment;
import com.rossconnacher.setgov.fragments.CitiesFragment;
import com.rossconnacher.setgov.fragments.CityFragment;
import com.rossconnacher.setgov.fragments.EventInfoFragment;
import com.rossconnacher.setgov.models.Event;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity implements
        CitiesFragment.OnFragmentInteractionListener,
        CityFragment.OnFragmentInteractionListener,
        EventInfoFragment.OnFragmentInteractionListener,
        AgendaInfoFragment.OnFragmentInteractionListener,
        View.OnClickListener{

    @InjectView(R.id.toolbar)
    public Toolbar toolbar;
    @InjectView(R.id.backButton)
    public ImageView backButton;

    ArrayList<Event> mEvents = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);

        //scrapeBostonHTML();
        //scrapeFortLauderdaleExcel();

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = CitiesFragment.newInstance();
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
    public void onClick(View v) {
        if(v.getId()==backButton.getId()){

            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            } else {
                super.onBackPressed();
            }

        }
    }
}
