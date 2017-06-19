package com.rossconnacher.setgov.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.rossconnacher.setgov.R;
import com.rossconnacher.setgov.SimpleDividerItemDecoration;
import com.rossconnacher.setgov.adapters.AgendaAdapter;
import com.rossconnacher.setgov.adapters.PersonAdapter;
import com.rossconnacher.setgov.models.Agenda;
import com.rossconnacher.setgov.models.City;
import com.rossconnacher.setgov.models.Event;
import com.rossconnacher.setgov.models.Person;

import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

public class EventInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String YOUTUBE_API_KEY = "AIzaSyBNOZCgDBHcM8WIfllk9wACNz58scFoQHw";
    private static final String BOSTON_LIVE_STREAM = "r2bswv815M8";

    private City mCity;
    private String mEventName;
    private String mEventAddress;
    private String mEventDate;
    private int mEventImageResID;
    private ArrayList<Person> mEventAttendees;
    private ArrayList<Agenda> mAgendas;
    private Event mEvent;

    @InjectView(R.id.backButton)
    public ImageView backButton;
    @InjectView(R.id.toolbarTitle)
    public TextView toolbarTitle;
    @InjectView(R.id.eventInfoAddress)
    public TextView eventInfoAddress;
    @InjectView(R.id.eventInfoDate)
    public TextView eventInfoDate;
    @InjectView(R.id.eventInfoTime)
    public TextView eventInfoTime;
    @InjectView(R.id.eventInfoDiscussion)
    public TextView eventInfoDiscussion;
    @InjectView(R.id.eventInfoImage)
    public ImageView eventInfoImage;
    @InjectView(R.id.eventInfoCircleImage)
    public CircleImageView eventInfoCircleImage;
    @InjectView(R.id.eventInfoAttendButton)
    public TextView eventInfoAttendButton;
    @InjectView(R.id.eventInfoAgenda)
    public RecyclerView eventInfoAgenda;
    @InjectView(R.id.eventInfoAttendees)
    public RecyclerView eventInfoAttendees;
    @InjectView(R.id.centerLayout)
    public LinearLayout centerLayout;

    private LinearLayoutManager mLayoutManager;
    private AgendaAdapter mAgendaAdapter;
    private YouTubePlayer YPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        ButterKnife.inject(this);

        mEvent = (Event)getIntent().getSerializableExtra("Event");
        mCity = mEvent.getCity();
        mEventName = mEvent.getName();
        mEventAddress = mEvent.getAddress();
        mEventDate = mEvent.getDate().toString();
        mEventImageResID = mEvent.getImageResID();
        mEventAttendees = mEvent.getAttendees();
        eventInfoImage.setImageResource(mEventImageResID);
        eventInfoCircleImage.setImageResource(mEventImageResID);
        toolbarTitle.setText(mEventName);
        eventInfoAddress.setText(mEventAddress);
        eventInfoDate.setText(mEventDate);
        backButton.setOnClickListener(this);
        eventInfoAttendButton.setOnClickListener(this);

        mAgendas = new ArrayList<Agenda>();

        populateAgenda();
        populateAttendees();
    }

    public void populateAgenda(){
        Agenda agenda1 = new Agenda("Tuck Tuck Tour","Licensing","test comments",mEvent);
        Agenda agenda2 = new Agenda("Expand Las Olas","Ordinance","test comments",mEvent);
        Agenda agenda3 = new Agenda("Water Works","Safety","test comments",mEvent);
        Agenda agenda4 = new Agenda("Water Works","Safety","test comments",mEvent);
        mAgendas.add(agenda1);
        mAgendas.add(agenda2);
        mAgendas.add(agenda3);
        mAgendas.add(agenda4);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        eventInfoAgenda.setLayoutManager(mLayoutManager);
        eventInfoAgenda.addItemDecoration(new SimpleDividerItemDecoration(this));
        mAgendaAdapter = new AgendaAdapter(eventInfoAgenda,this, mAgendas);
        eventInfoAgenda.setAdapter(mAgendaAdapter);
    }

    public void populateAttendees(){
        eventInfoAttendees.setAdapter(new PersonAdapter(this,mEventAttendees));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        eventInfoAttendees.setLayoutManager(layoutManager);
    }


    public void initLiveStream() {

        //remove other views
        centerLayout.setVisibility(View.GONE);
        eventInfoImage.setVisibility(View.GONE);
        YouTubePlayerFragment youTubePlayerFragment = YouTubePlayerFragment.newInstance();
        youTubePlayerFragment.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean wasRestored) {
                YPlayer = youTubePlayer;
                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                youTubePlayer.loadVideo(BOSTON_LIVE_STREAM);
                youTubePlayer.play();
               // youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
               // youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
                youTubePlayer.setFullscreenControlFlags(0);



            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.youtubeFragment, youTubePlayerFragment).commit();
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==backButton.getId()){
            Intent i = new Intent(this,CityActivity.class);
            i.putExtra("City",mCity);
            startActivity(i);
        } else if (v.getId() == eventInfoAttendButton.getId()){
            //connect to live stream
            //alert dialog prompt
            eventInfoAttendButton.setText("Attending");
            initLiveStream();
        }
    }
}
