package com.rossconnacher.setgov.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

public class CityActivity extends AppCompatActivity implements View.OnClickListener {

    @InjectView(R.id.backButton)
    public ImageView backButton;
    @InjectView(R.id.toolbarTitle)
    public TextView toolbarTitle;
    @InjectView(R.id.eventRecyclerView)
    public RecyclerView eventView;
    private ArrayList<Event> mEvents;
    private GridLayoutManager mEventLayoutManager;
    private EventAdapter mEventAdapter;
    private City mCity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.inject(this);
        mCity = (City)getIntent().getSerializableExtra("City");
        toolbarTitle.setText(mCity.toString());


        mEvents= new ArrayList<Event>();

        if(mCity.toString().equals("Boston, MA")){
        //add boston events
            ArrayList<Person> attendees = new ArrayList<>();
            Resources resources = this.getResources();
            final int personResourceId = resources.getIdentifier("kappaross", "drawable",
                    this.getPackageName());

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
                    this.getPackageName());
            mEvents.add(new Event("City Council","Meeting", mCity, Calendar.getInstance().getTime(),"1 City Hall Square",new String[]{"enviromental","legislation"},attendees, eventImageResourceId));
            mEvents.add(new Event("City Council","Meeting", mCity, Calendar.getInstance().getTime(),"1 City Hall Square",new String[]{"enviromental","legislation"},attendees, eventImageResourceId));
            mEvents.add(new Event("City Council","Meeting", mCity, Calendar.getInstance().getTime(),"1 City Hall Square",new String[]{"enviromental","legislation"},attendees, eventImageResourceId));

        } else if(mCity.toString().equals("Fort Lauderdale, FL")){
        //add fort lauderdale events

        } else if(mCity.toString().equals("New York, NY")){
        //add new york events

        }
        mEventLayoutManager = new GridLayoutManager(this, 1);
        eventView.setLayoutManager(mEventLayoutManager);
        eventView.addItemDecoration(new SimpleDividerItemDecoration(this));
        mEventAdapter = new EventAdapter(eventView,this, mEvents);
        eventView.setAdapter(mEventAdapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==backButton.getId()){
            Intent i = new Intent(this,CitiesActivity.class);
            startActivity(i);
        }
    }

}
