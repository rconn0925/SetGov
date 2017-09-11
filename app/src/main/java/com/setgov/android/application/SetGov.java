package com.setgov.android.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.setgov.android.activities.LoginActivity;
import com.setgov.android.activities.MainActivity;

/**
 * Created by Ross on 6/19/2017.
 */

public class SetGov extends Application {
    @Override
    public void onCreate() {

        // TODO Auto-generated method stub
        super.onCreate();

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
        if(prefs.getBoolean("isLoggedIn", false)){
            //Login activity
            Log.d("SetGov","login from past session");
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else {
            //Main Activity
            Log.d("SetGov","new login session");
            Intent i = new Intent(this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }
}
