package com.rossconnacher.setgov.fragments;

import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventInfoFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "event";

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


    private OnFragmentInteractionListener mListener;
    private ImageView backButton;

    public EventInfoFragment() {
        // Required empty public constructor
    }


    public static EventInfoFragment newInstance(Event event) {
        EventInfoFragment fragment = new EventInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, event);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEvent = (Event)getArguments().getSerializable(ARG_PARAM1);
            mCity = mEvent.getCity();
            mEventName = mEvent.getName();
            mEventAddress = mEvent.getAddress();
            DateFormat dateFormat = new SimpleDateFormat("M/d/yy  h:mm a");
            mEventDate = dateFormat.format(mEvent.getDate());
            mEventImageResID = mEvent.getImageResID();
            mEventAttendees = mEvent.getAttendees();
            mAgendas = new ArrayList<Agenda>();

        }
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
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        eventInfoAgenda.setLayoutManager(mLayoutManager);
        eventInfoAgenda.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mAgendaAdapter = new AgendaAdapter(eventInfoAgenda,getActivity(), mAgendas);
        eventInfoAgenda.setAdapter(mAgendaAdapter);
    }

    public void populateAttendees(){
        eventInfoAttendees.setAdapter(new PersonAdapter(getActivity(),mEventAttendees));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
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
        FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
        transaction.add(R.id.youtubeFragment, youTubePlayerFragment).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_event_info, container, false);
        ButterKnife.inject(this,view);
        eventInfoImage.setImageResource(mEventImageResID);
        eventInfoCircleImage.setImageResource(mEventImageResID);
        eventInfoAddress.setText(mEventAddress);
        eventInfoDate.setText(mEventDate);
        backButton = (ImageView) getActivity().findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
        eventInfoAttendButton.setOnClickListener(this);
        TextView toolbarTitle = (TextView) getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(mEventName);
        populateAgenda();
        populateAttendees();
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
        if(v.getId()==backButton.getId()){

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            Fragment currentFragment = CityFragment.newInstance(mCity);
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).replace(R.id.contentFrame, currentFragment).commit();
        } else if (v.getId() == eventInfoAttendButton.getId()){
            //connect to live stream
            //alert dialog prompt
            eventInfoAttendButton.setText("Attending");
            initLiveStream();
        }
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
