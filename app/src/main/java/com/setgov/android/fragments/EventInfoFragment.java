package com.setgov.android.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.setgov.android.R;
import com.setgov.android.SimpleDividerItemDecoration;
import com.setgov.android.adapters.AgendaAdapter;
import com.setgov.android.adapters.AgendaPDFAdapter;
import com.setgov.android.adapters.CommentAdapter;
import com.setgov.android.adapters.UserAdapter;
import com.setgov.android.models.Agenda;
import com.setgov.android.models.AgendaPDF;
import com.setgov.android.models.City;
import com.setgov.android.models.Event;
import com.setgov.android.models.User;
import com.setgov.android.networking.ApiGraphRequestTask;
import com.setgov.android.util.DebouncedOnClickListener;
import com.setgov.android.util.MyEditText;

import org.json.JSONArray;
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
    private ArrayList<AgendaPDF> mAgendas;
    private Event mEvent;
    private Context mContext;
    
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
    @InjectView(R.id.eventInfoCommentEditText)
    public MyEditText eventInfoCommentEditText;
    @InjectView(R.id.eventInfoTimeLayout)
    public LinearLayout eventInfoTimeLayout;
    @InjectView(R.id.eventInfoLocationLayout)
    public LinearLayout eventInfoLocationLayout;
    @InjectView(R.id.eventInfoBackground)
    public LinearLayout eventInfoBackground;
    @InjectView(R.id.eventInfoImageBackground)
    public RelativeLayout eventInfoImageBackground;

    private LinearLayoutManager mLayoutManager;
    private AgendaPDFAdapter mAgendaAdapter;
    private YouTubePlayer YPlayer;


    private OnFragmentInteractionListener mListener;
    private ApiGraphRequestTask activeApiCall;
    private User mUser;
    private Handler handler;
    private Runnable toastAttending = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getActivity().getApplicationContext(), "Attending "+ mEvent.getName() , Toast.LENGTH_SHORT).show();
        }
    };
    private Runnable toastUnattending = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getActivity().getApplicationContext(), "Unattending "+ mEvent.getName() , Toast.LENGTH_SHORT).show();
        }
    };
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
            handler = new Handler();
            mContext = getActivity().getApplicationContext();

            SharedPreferences sp = mContext.getSharedPreferences
                    ("auth", Context.MODE_PRIVATE);
            String userJson = sp.getString("loggedInUserJson", "");

            try {
                mUser = new User(new JSONObject(userJson));
            } catch ( JSONException e) {
            }


        }
    }

    public void populateAgenda(){
        AgendaPDF agendaPDF = new AgendaPDF(mEvent,"https://fortlauderdale.legistar.com/View.ashx?M=A&ID=542490&GUID=2D769EE5-7983-4869-A3F4-E5D31967452E");
    //    Agenda agenda2 = new Agenda("Expand Las Olas","Ordinance","test comments",mEvent);
     //   Agenda agenda3 = new Agenda("Water Works","Safety","test comments",mEvent);
    //    Agenda agenda4 = new Agenda("Water Works","Safety","test comments",mEvent);
      //  mAgendas.add(agenda1);
      //  mAgendas.add(agenda2);
       // mAgendas.add(agenda3);
        mAgendas.add(agendaPDF);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        eventInfoAgenda.setLayoutManager(mLayoutManager);
        eventInfoAgenda.addItemDecoration(new SimpleDividerItemDecoration(getActivity(),false));
        mAgendaAdapter = new AgendaPDFAdapter(eventInfoAgenda,getActivity(), mAgendas);
        eventInfoAgenda.setAdapter(mAgendaAdapter);
    }

    public void populateAttendees(){
        eventInfoAttendees.setAdapter(new UserAdapter(getActivity(),mEvent.getAttendees()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        eventInfoAttendees.setLayoutManager(layoutManager);
    }

    public void populateComments(){
        eventInfoComments.setAdapter(new CommentAdapter(mUser,eventInfoCommentEditText,getActivity(),mEvent.getComments()));
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        eventInfoComments.setLayoutManager(layoutManager);
      //  eventInfoComments.addItemDecoration(new SimpleDividerItemDecoration(getActivity(),false));
    }

    /*
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
    */

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_event_info, container, false);
        Log.d(TAG,"OnCreateView");

        ButterKnife.inject(this,view);
        mAgendas = new ArrayList<AgendaPDF>();
       // eventInfoImage.setImageResource(mEventImageResID);
        Resources res = getActivity().getResources();
       // String mDrawableName = mEvent.getCity().getCityName().toLowerCase().replace(" ","")+"cityhall";
        //int resID = res.getIdentifier(mDrawableName , "drawable", getActivity().getPackageName());



        if(mEvent.getCity().getCityName().equals("Boston")){
            if(mEvent.getAddress().contains("1 City Hall")){
                eventInfoImage.setImageResource(R.drawable.bostoncityhall);
                eventInfoImageBackground.setBackgroundResource(R.color.light_black_overlay);
            }else {
                eventInfoImage.setImageResource(R.drawable.bostonother);
            }
        }
        else {
            String mDrawableName = mEvent.getCity().getCityName().toLowerCase().replace(" ","")+"cityhall";
            int resID = res.getIdentifier(mDrawableName , "drawable", getActivity().getPackageName());
            eventInfoImage.setImageResource(resID);
        }

        eventInfoLocation.setText(mEventAddress);
        eventInfoDateTime.setText(mEventDate+ " @ "+ mEvent.getTime());
        eventInfoTag.setText(mEvent.getDescription());
        for(int i = 0 ;i<mEvent.getAttendees().size();i++){
            if(mUser.getID()==mEvent.getAttendees().get(i).getID()){
                eventInfoTag.setText(R.string.attending);
                eventInfoTag.setBackgroundResource(R.drawable.rounded_border_green);
                eventInfoAttendButton.setVisibility(View.GONE);
            }
        }
        eventInfoName.setText(mEvent.getName());
        String numGoing = mEvent.getAttendees().size()+" going";
        eventInfoNumberGoing.setText(numGoing);
        ImageView settingsButton = (ImageView) getActivity().findViewById(R.id.settingsButton);
        settingsButton.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        settingsButton.setTag("back");
       // eventInfoAttendButton.setOnClickListener(this);
        TextView toolbarTitle = (TextView) getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(R.string.event_details);
        eventInfoTag.setOnClickListener(new DebouncedOnClickListener(1000) {
            @Override
            public void onDebouncedClick(View v) {
                if(eventInfoTag.getText().toString().equals("ATTENDING"))
                {
                    kickoffUnattendEvent();
                    eventInfoTag.setText(mEvent.getDescription());
                    eventInfoTag.setBackgroundResource(R.drawable.rounded_border_purple);
                } else {
                    kickoffAttendEvent();
                }
            }
        });
        eventInfoLocationLayout.setOnClickListener(this);
        eventInfoTimeLayout.setOnClickListener(this);
        eventInfoBackground.setOnClickListener(this);
        eventInfoAttendButton.setOnClickListener(new DebouncedOnClickListener(1000) {
            @Override
            public void onDebouncedClick(View v) {
                kickoffAttendEvent();
                if(eventInfoCommentEditText.hasFocus()){
                    eventInfoCommentEditText.clearFocus();
                }
                eventInfoTag.setText(R.string.attending);
                eventInfoTag.setBackgroundResource(R.drawable.rounded_border_green);
            }
        });
        eventInfoCommentEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        eventInfoCommentEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Do whatever you want here
                    Log.d(TAG,"COMMENT DONE");

                    SharedPreferences sp = getActivity().getApplicationContext().getSharedPreferences
                            ("auth", Context.MODE_PRIVATE);
                    if(sp.getBoolean("isReply",false)){
                        if(sp.getInt("replyTo",-1)>0){
                            kickoffAddReply(eventInfoCommentEditText.getText().toString(),mEvent.getId(),sp.getInt("replyTo",0),mEvent.getCity().getCityName());
                        }else {
                            kickoffAddComment(eventInfoCommentEditText.getText().toString(),mEvent.getId(),mEvent.getCity().getCityName());
                        }
                    } else {
                        kickoffAddComment(eventInfoCommentEditText.getText().toString(),mEvent.getId(),mEvent.getCity().getCityName());
                    }
                    eventInfoCommentEditText.setGravity(Gravity.CENTER);
                    eventInfoCommentEditText.setText("");
                    eventInfoCommentEditText.setHint(R.string.enter_a_comment);
                    eventInfoAttendButton.setVisibility(View.VISIBLE);
                    eventInfoCommentEditText.clearFocus();
                    return true;
                }
                return false;
            }
        });
        eventInfoCommentEditText.setOnFocusChangeListener(
            new View.OnFocusChangeListener() {
                  @Override
                  public void onFocusChange(View v, boolean hasFocus) {
                      if(hasFocus){
                          eventInfoCommentEditText.setGravity(Gravity.LEFT | Gravity.TOP);
                          eventInfoCommentEditText.setHint("");
                          eventInfoAttendButton.setVisibility(View.GONE);

                      } else {
                          hideKeyboard(v);
                          eventInfoCommentEditText.setGravity(Gravity.CENTER);
                          eventInfoCommentEditText.setText("");
                          eventInfoCommentEditText.setHint(R.string.enter_a_comment);
                          eventInfoAttendButton.setVisibility(View.VISIBLE);
                          SharedPreferences sp = getActivity().getApplicationContext().getSharedPreferences
                                  ("auth", Context.MODE_PRIVATE);
                          SharedPreferences.Editor editor = sp.edit();
                          editor.putBoolean("isReply",false);
                          editor.putInt("replyTo",-1);
                          editor.apply();
                      }
                  }
        });
        eventInfoCommentEditText.setKeyImeChangeListener(new MyEditText.KeyImeChange(){
            @Override
            public void onKeyIme(int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    if(keyCode == KeyEvent.KEYCODE_BACK){
                        eventInfoCommentEditText.clearFocus();
                    }
                }
            }
        });
        populateAgenda();
        populateAttendees();
        populateComments();
        return view;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

    public void goToRSVPFrag(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        RSVPFragment frag = RSVPFragment.newInstance(mEvent);
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).add(R.id.rsvp_container, frag).show(frag).commit();
    }

    @Override
    public void onClick(View v) {
/*
       if (v.getId() == eventInfoAttendButton.getId()){
           kickoffAttendEvent();
           if(eventInfoCommentEditText.hasFocus()){
              eventInfoCommentEditText.clearFocus();
           }
           eventInfoTag.setText(R.string.attending);
           eventInfoTag.setBackgroundResource(R.drawable.rounded_border_green);
        }
        */
        if (v.getId() == eventInfoLocationLayout.getId()){
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("google.navigation:q="+mEvent.getAddress()+"+"+mEvent.getCity().getCityName()));
            startActivity(intent);

        } else if (v.getId() == eventInfoTimeLayout.getId()) {
            long startTime = mEvent.getDateTimeStamp().getTime();
            long endTime = startTime + 3600000;


            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime)
                    .putExtra(CalendarContract.Events.TITLE, mEvent.getName())
                    .putExtra(CalendarContract.Events.DESCRIPTION, mEvent.getDescription())
                    .putExtra(CalendarContract.Events.EVENT_LOCATION, mEvent.getAddress())
                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
            //  .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
            startActivity(intent);
        } else if (v.getId() == eventInfoBackground.getId()){
            if(eventInfoCommentEditText.hasFocus()){
                eventInfoCommentEditText.clearFocus();
            }
        }

    }
    private void kickoffAddReply(String commentText, int commentEventID, int parentCommentID, final String commentEventCity){
        activeApiCall = new ApiGraphRequestTask(getActivity());
        Log.d(TAG, "addReply");
        String jsonQuery="mutation{addReply(text: \"" + commentText + "\",event_id: "+commentEventID+",parent_comment_id:"+parentCommentID+"){id}}";

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
                    Log.d(TAG, "add reply response: " + jsonResponse.toString());
                    //JSONObject data = jsonResponse.getJSONObject("data");
                    // Toast.makeText(mContext, "Added comment", Toast.LENGTH_SHORT).show();
                    kickoffGetEvents(commentEventCity,true,false);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void kickoffAddComment(String commentText, int commentEventID, final String commentEventCity){
        Log.d(TAG, "addComment");
        activeApiCall = new ApiGraphRequestTask(getActivity());

        String jsonQuery="mutation{addComment(text: \"" + commentText + "\",event_id: "+commentEventID+"){id}}";

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
                    Log.d(TAG, "add comment response: " + jsonResponse.toString());
                    //JSONObject data = jsonResponse.getJSONObject("data");
                    // Toast.makeText(mContext, "Added comment", Toast.LENGTH_SHORT).show();
                    kickoffGetEvents(commentEventCity,true,false);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void kickoffAttendEvent() {
        activeApiCall = new ApiGraphRequestTask(getActivity());

        String jsonQuery="mutation{attendEvent(event_id: "+mEvent.getId()+"){id,full_name}}";

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
                    handler.post(toastAttending);
                    kickoffGetEvents(mEvent.getCity().getCityName(),false,true);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void kickoffUnattendEvent() {
        activeApiCall = new ApiGraphRequestTask(getActivity());

        String jsonQuery="mutation{unattendEvent(event_id: "+mEvent.getId()+"){id}}";

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
                    Log.d(TAG, "unattend event response: " + jsonResponse.toString());
                    //JSONObject data = jsonResponse.getJSONObject("data");
                    handler.post(toastUnattending);
                    kickoffGetEvents(mEvent.getCity().getCityName(),true,false);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void kickoffRefreshUser(final boolean flag) {
        activeApiCall = new ApiGraphRequestTask(getActivity());

        String jsonQuery="query{user(id: "+mUser.getID()+"){id,full_name,facebook_id,profileImage{url},home_city,eventsAttending{id}}}";

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
                    Log.d(TAG, "get user response: " + jsonResponse.toString());
                    //JSONObject data = jsonResponse.getJSONObject("data");
                    JSONObject userLoginJSON = jsonResponse.getJSONObject("data").getJSONObject("user");

                    Log.d(TAG, jsonResponse.toString());
                    mUser = new User(userLoginJSON);


                    if(isAdded()){
                        SharedPreferences sp = mContext.getSharedPreferences
                                ("auth", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("loggedInUserJson", mUser.getUserJsonStr());
                        editor.putBoolean("isLoggedIn",true);
                        editor.apply();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(flag){
                     goToRSVPFrag();
                }
            }
        });
    }

    private void kickoffGetEvents(final String city, final boolean refreshAfter, final boolean flag){
        activeApiCall = new ApiGraphRequestTask(getActivity());
        String jsonQuery="query{upcomingEvents(city: \""+city+"\"){id,name,city,address,date,time,description,type,attendingUsers" +
                "{id,profileImage{url}},comments{id,event{id,city},user{id,full_name,facebook_id,profileImage{url},home_city,eventsAttending{id}},text,karma,timestamp," +
                "votes{id,user{id},comment{id},vote_value},replies{id},parentComment{id}},agendaItems{id,name,description,type,event{id}}}}";
        activeApiCall.run(jsonQuery,new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "ApiGraphRequestTask: onFailure ");
                activeApiCall = null;
                //     handler.post(apiFailure);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                activeApiCall = null;
                try {
                    JSONObject jsonResponse = new JSONObject(response.body().string());
                    JSONObject data = jsonResponse.getJSONObject("data");
                    if(isAdded()){
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(city+"Events",data.toString());
                        editor.apply();
                    }

                    JSONArray eventsJsonArray = data.getJSONArray("upcomingEvents");
                    for(int i = 0; i < eventsJsonArray.length();i++) {
                        Log.d(TAG, "Event " + i);
                        Event event = new Event(eventsJsonArray.getJSONObject(i));
                        if(event.getId() == mEvent.getId()){
                            mEvent = event;
                        }
                    }
                    if(refreshAfter){
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        Fragment fragment = fragmentManager.findFragmentById(R.id.contentFrame);
                        if(fragment != null){
                            Log.d(TAG,"is this null???");
                            FragmentTransaction fragTransaction =fragmentManager.beginTransaction();
                            fragTransaction.detach(fragment);
                            fragTransaction.attach(fragment);
                            fragTransaction.commitAllowingStateLoss();
                        } else {
                            Log.d(TAG,"you messed up!");
                        }
                    }

                    kickoffRefreshUser(flag);
                    Log.d(TAG, "upcoming events str: " + data.toString());

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
