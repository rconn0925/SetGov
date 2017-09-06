package com.rossconnacher.setgov.activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.rossconnacher.setgov.R;
import com.rossconnacher.setgov.networking.ApiGraphRequestTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;

import static android.R.attr.data;

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
                "{id},comments{id,event{id},user{id,full_name,facebook_id,profileImage{url},home_city},text,karma,timestamp," +
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
                    JSONArray upcomingEvents = (JSONArray) jsonResponse.getJSONObject("data").get("upcomingEvents");
                    for(int i = 0; i < upcomingEvents.length();i++){
                        Log.d(TAG, "upcoming events: "+i+" " + upcomingEvents.get(i).toString());
                    }
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(city+"Events",upcomingEvents.toString());
                    editor.apply();
                    Log.d(TAG, "upcoming events str: " + upcomingEvents.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
