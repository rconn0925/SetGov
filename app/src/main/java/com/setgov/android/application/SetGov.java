package com.setgov.android.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.setgov.android.activities.LoginActivity;
import com.setgov.android.activities.MainActivity;
import com.setgov.android.activities.SplashActivity;

/**
 * Created by Ross on 6/19/2017.
 */

public class SetGov extends Application {
    @Override
    public void onCreate() {

        // TODO Auto-generated method stub
        super.onCreate();
     //   Intent i = new Intent(this, SplashActivity.class);
     //   i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
     //   startActivity(i);
    }
}
