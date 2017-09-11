package com.setgov.android.fragments;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.setgov.android.R;
import com.setgov.android.SimpleDividerItemDecoration;
import com.setgov.android.adapters.AgendaAdapter;
import com.setgov.android.adapters.PersonAdapter;
import com.setgov.android.models.Agenda;
import com.setgov.android.models.City;
import com.setgov.android.models.Event;
import com.setgov.android.models.User;
import com.setgov.android.networking.ApiGraphRequestTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class EventInfoFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "event";
    private static final String TAG = "EventInfoFragment";

    private static final String YOUTUBE_API_KEY = "AIzaSyBNOZCgDBHcM8WIfllk9wACNz58scFoQHw";
    private static final String BOSTON_LIVE_STREAM = "r2bswv815M8";

    private City mCity;
    private String mEventName;
    private String mEventAddress;
    private String mEventDate;
    private int mEventImageResID;
    private ArrayList<User> mEventAttendees;
    private ArrayList<Agenda> mAgendas;
    private Event mEvent;
    
    @InjectView(R.id.eventInfoLocation)
    public TextView eventInfoLocation;
    @InjectView(R.id.eventInfoDateTime)
    public TextView eventInfoDateTime;
    @InjectView(R.id.eventInfoNumberGoing)
    public TextView eventInfoNumberGoing;
    @InjectView(R.id.eventInfoName)
    public TextView eventInfoName;
    @InjectView(R.id.eventInfoTag)
    public TextView eventInfoTag;
    @InjectView(R.id.eventInfoImage)
    public ImageView eventInfoImage;
    @InjectView(R.id.eventInfoAttendButton)
    public TextView eventInfoAttendButton;
    @InjectView(R.id.eventInfoAgenda)
    public RecyclerView eventInfoAgenda;
    @InjectView(R.id.eventInfoAttendees)
    public RecyclerView eventInfoAttendees;
    @InjectView(R.id.eventInfoComments)
    public RecyclerView eventInfoComments;

    private LinearLayoutManager mLayoutManager;
    private AgendaAdapter mAgendaAdapter;
    private YouTubePlayer YPlayer;


    private OnFragmentInteractionListener mListener;
    private ApiGraphRequestTask activeApiCall;
    private User mUser;

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
            //mEventDate = dateFormat.format(mEvent.getDate());
            mEventDate = mEvent.getDateStr();
         //   mEventImageResID = mEvent.getImageResID();
          //  mEventAttendees = mEvent.getAttendees();
            mAgendas = new ArrayList<Agenda>();

            SharedPreferences sp = getActivity().getApplicationContext().getSharedPreferences
                    ("auth", Context.MODE_PRIVATE);
            String userJson = sp.getString("loggedInUserJson", "");
            try {
                mUser = new User(new JSONObject(userJson));
            } catch ( JSONException e){

            }


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
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_event_info, container, false);

        ButterKnife.inject(this,view);
       // eventInfoImage.setImageResource(mEventImageResID);
        //eventInfoCircleImage.setImageResource(mEventImageResID);
        eventInfoLocation.setText(mEventAddress);
        eventInfoDateTime.setText(mEventDate);

        if(mUser.getAttendingEvents().contains(mEvent.getId())){
            eventInfoTag.setText(R.string.attending);
        } else {
            eventInfoTag.setText(mEvent.getDescription());
        }

        eventInfoName.setText(mEvent.getName());
        String numGoing = mEvent.getAttendees().size()+" going";
        eventInfoNumberGoing.setText(numGoing);
        ImageView settingsButton = (ImageView) getActivity().findViewById(R.id.settingsButton);
        settingsButton.setImageResource(R.drawable.ic_arrow_back_black_24dp);
       // eventInfoAttendButton.setOnClickListener(this);
        TextView toolbarTitle = (TextView) getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(R.string.event_details);
        eventInfoAttendButton.setOnClickListener(this);
        populateAgenda();
       // populateAttendees();
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

       if (v.getId() == eventInfoAttendButton.getId()){
          // kickoffAttendEvent();

           FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
           RSVPFragment frag = RSVPFragment.newInstance(mEvent);
           fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.exit_to_right,R.anim.enter_from_left).add(R.id.rsvp_container, frag).show(frag).commit();
        }

    }

    private void kickoffAttendEvent() {
        activeApiCall = new ApiGraphRequestTask(getActivity());

        String jsonQuery="mutation{attendEvent(event_id: \""+mEvent.getId()+"\"){id}}";

        activeApiCall.run(jsonQuery,new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "ApiGraphRequestTask: onFailure ");
                activeApiCall = null;
                //     handler.post(apiFailure);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                activeApiCall = null;
                try {
                    JSONObject jsonResponse = new JSONObject(response.body().string());
                    Log.d(TAG, "attend event response: " + jsonResponse.toString());
                    //JSONObject data = jsonResponse.getJSONObject("data");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
