package com.rossconnacher.setgov.application;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.rossconnacher.setgov.activities.LoginActivity;
import com.rossconnacher.setgov.activities.MainActivity;

/**
 * Created by Ross on 6/19/2017.
 */

public class SetGov extends Application {
    @Override
    public void onCreate() {

        // TODO Auto-generated method stub
        super.onCreate();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getBoolean("isLoggedIn", false)){
            //Login activity
            Log.d("SetGov","login from past session");
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else {
            //Main Activity
            Log.d("SetGov","new login session");
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
    }
}
