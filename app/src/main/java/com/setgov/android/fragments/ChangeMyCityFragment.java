package com.setgov.android.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.setgov.android.R;
import com.setgov.android.networking.ApiGraphRequestTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChangeMyCityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChangeMyCityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeMyCityFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ChangeMyCityFragment";

    @InjectView(R.id.changeCityBack)
    public ImageView changeCityBack;
    @InjectView(R.id.changeCityForward)
    public ImageView changeCityForward;
    @InjectView(R.id.changeCityDone)
    public TextView changeCityDone;
    @InjectView(R.id.changeCityBackground)
    public RelativeLayout changeCityBackground;
    @InjectView(R.id.cityPicker)
    public NumberPicker cityPicker;
    private ChangeMyCityFragment thisFrag;

   // private String[] allCities = {"Boston","Fort Lauderdale","Phoenix","Miami","San Jose","Austin"};
    private String[] allCities = {"Fort Lauderdale","Boston"};
    private String mHomeCity;
    private Handler handler;
    private Runnable toastSuccess = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getActivity().getApplicationContext(), "Home city is now " + mHomeCity , Toast.LENGTH_LONG).show();
        }
    };
    private Runnable toastError = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getActivity().getApplicationContext(), mHomeCity+ " has no events. Please select another city.", Toast.LENGTH_LONG).show();
        }
    };


    private OnFragmentInteractionListener mListener;
    private ApiGraphRequestTask activeApiCall;
    private SharedPreferences prefs;

    public ChangeMyCityFragment() {
        // Required empty public constructor
    }


    public static ChangeMyCityFragment newInstance() {
        ChangeMyCityFragment fragment = new ChangeMyCityFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        handler = new Handler();
        thisFrag = this;
        prefs = getActivity().getApplicationContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
        mHomeCity = allCities[0];//prefs.getString("userHomeCity","");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_my_city, container, false);
        ButterKnife.inject(this,view);
        changeCityBack.setOnClickListener(this);
        changeCityForward.setOnClickListener(this);
        changeCityDone.setOnClickListener(this);
        changeCityBackground.setOnClickListener(this);
        cityPicker.setMinValue(0);
        cityPicker.setMaxValue(allCities.length-1);
        cityPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mHomeCity = allCities[newVal];
                Log.d(TAG,"city value change: "+ mHomeCity);
            }
        });
        cityPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return allCities[value];
            }
        });
        try {
            Method method = cityPicker.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
            method.setAccessible(true);
            method.invoke(cityPicker, true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

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
        if(v.getId() == changeCityBackground.getId()){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).remove(this).commit();
        } else if (v.getId() == changeCityBack.getId()){
            try {
                Method method = cityPicker.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
                method.setAccessible(true);
                method.invoke(cityPicker, false);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if (v.getId() == changeCityForward.getId()){
            try {
                Method method = cityPicker.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
                method.setAccessible(true);
                method.invoke(cityPicker, true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if (v.getId() == changeCityDone.getId()){
            if(cityHasEvents(mHomeCity)){
                kickoffChangeHomeCity();
            } else {
                handler.post(toastError);
            }
        }
    }

    public boolean cityHasEvents(String city) {
        Log.d(TAG, "Get events for " + city);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
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

    public void kickoffChangeHomeCity(){
        activeApiCall = new ApiGraphRequestTask(getActivity());

        String jsonQuery="mutation{setHomeCity(home_city: \""+mHomeCity+"\"){id}}";

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
                    Log.d(TAG, "home city response: " + jsonResponse.toString());
                    prefs.edit().putString("userHomeCity",mHomeCity).apply();
                    handler.post(toastSuccess);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    //recall OnCreateView in settings to update homecity textview
                    Fragment fragment = fragmentManager.findFragmentById(R.id.contentFrame);
                    if(fragment != null){
                        Log.d(TAG,"is this null???");
                        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
                        fragTransaction.detach(fragment);
                        fragTransaction.attach(fragment);
                        fragTransaction.commitAllowingStateLoss();
                    }
                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).remove(thisFrag).commit();

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
