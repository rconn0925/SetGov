package com.setgov.android.activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.setgov.android.R;
import com.setgov.android.networking.ApiGraphRequestTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private ApiGraphRequestTask activeApiCall;
    private String[] allCities = {"Boston","Fort Lauderdale","Phoenix","Miami","San Jose","Austin","New York"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        for(String city:allCities){
            kickoffGetEvents(city);
        }
    }

    public void kickoffGetEvents(final String city){
        activeApiCall = new ApiGraphRequestTask(getApplicationContext());

        String jsonQuery="query{upcomingEvents(city: \""+city+"\"){id,name,city,address,date,time,description,type,attendingUsers" +
                "{id,profileImage{url}},comments{id,event{id},user{id,full_name,facebook_id,profileImage{url},home_city,eventsAttending{id}},text,karma,timestamp," +
                "replies{id},parentComment{id}},agendaItems{id,name,description,type,event{id}}}}";

        ((ApiGraphRequestTask)activeApiCall).run(jsonQuery,new Callback() {
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
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(city+"Events",data.toString());
                    editor.apply();
                    Log.d(TAG, "upcoming events str: " + data.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
