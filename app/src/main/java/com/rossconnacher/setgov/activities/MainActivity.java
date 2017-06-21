package com.rossconnacher.setgov.activities;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.rossconnacher.setgov.R;
import com.rossconnacher.setgov.fragments.AgendaInfoFragment;
import com.rossconnacher.setgov.fragments.CitiesFragment;
import com.rossconnacher.setgov.fragments.CityFragment;
import com.rossconnacher.setgov.fragments.EventInfoFragment;

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

    Fragment previousFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = CitiesFragment.newInstance();
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left).replace(R.id.contentFrame, currentFragment).commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void setPreviousFrag(Fragment prevFrag){
        previousFrag = prevFrag;
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
