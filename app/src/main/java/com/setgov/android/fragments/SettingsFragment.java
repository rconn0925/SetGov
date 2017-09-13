package com.setgov.android.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.setgov.android.R;
import com.setgov.android.activities.LoginActivity;
import com.setgov.android.models.User;
import com.setgov.android.networking.ApiGraphRequestTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private ApiGraphRequestTask activeApiCall;
    private final static String TAG = "SettingsFragment";
    @InjectView(R.id.logout_button)
    public LoginButton logoutButton;
    @InjectView(R.id.settingsNameTextView)
    public TextView settingsUserName;
    @InjectView(R.id.settingsCityName)
    public TextView settingsCityName;
    @InjectView(R.id.settingProfileImage)
    public CircleImageView settingProfileImage;
    @InjectView(R.id.changeCityTextView)
    public TextView changeCityTextView;

    private SharedPreferences prefs;

    public SettingsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreateView");
        if (getArguments() != null) {

        }
        prefs = getActivity().getApplicationContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.inject(this,view);
        TextView toolbarTitle = (TextView) getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(R.string.settings);
        logoutButton.setOnClickListener(this);
        changeCityTextView.setOnClickListener(this);
        //Toast.makeText(getActivity().getApplicationContext(), "Clicked ", Toast.LENGTH_LONG).show();
        JSONObject userJson = null;
        try {
            userJson = new JSONObject(prefs.getString("loggedInUserJson",""));
            User user  = new User(userJson);
            settingsUserName.setText(user.getName());
            Picasso.with(getActivity()).load(user.getProfileImageUrl()).into(settingProfileImage);
            String city = prefs.getString("userHomeCity","");
            settingsCityName.setText(city);

        } catch (JSONException e) {
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
        Log.d(TAG,"onAttach");
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Log.d(TAG,"onDetach");
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == logoutButton.getId()){
            LoginManager.getInstance().logOut();
            Intent i = new Intent(getActivity(), LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else if (v.getId() == changeCityTextView.getId()) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            ChangeMyCityFragment frag = ChangeMyCityFragment.newInstance();
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).add(R.id.selectCityContainer, frag,TAG).show(frag).commit();
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
