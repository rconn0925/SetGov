package com.setgov.android.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.setgov.android.R;
import com.setgov.android.models.User;
import com.setgov.android.networking.ApiGraphRequestTask;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import okhttp3.Call;
import okhttp3.Callback;


import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    public static final String TAG = "LoginActivity";

    // UI references.
    @InjectView(R.id.login_button)
    public LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken;
    private String facebookAccessTokenString;
    private AccessToken facebookAccessToken;
    private ApiGraphRequestTask activeApiCall;
    private User user;
    final Handler handler = new Handler();
    final Runnable initiateSetGovLogin = new Runnable() {
        @Override
        public void run() {
            kickOffSetGovLoginApiRequest();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        facebookAccessToken = AccessToken.getCurrentAccessToken();
        if(facebookAccessToken != null) {
            Log.d(TAG, Integer.toString(facebookAccessToken.describeContents()));
            Log.d(TAG, facebookAccessToken.getApplicationId());
            Log.d(TAG, facebookAccessToken.getUserId());
        } else {
            Log.d(TAG, "facebookAccessToken is null");
        }
        callbackManager = CallbackManager.Factory.create();
        configureFacebookButton();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
   //     accessTokenTracker.stopTracking();
    }


    private void goToMainActivity(User user) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("facebookAccessToken", facebookAccessTokenString);
        intent.putExtra("User",user);
        startActivity(intent);
        finishAffinity();
    }
    private void configureFacebookButton() {

        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email"));
        loginButton.refreshDrawableState();

        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "loginresult str: "+loginResult.toString());
                facebookAccessTokenString = loginResult.getAccessToken().getToken();
                Log.d(TAG, facebookAccessTokenString);
                Log.d(TAG, AccessToken.getCurrentAccessToken().toString());
                AccessToken.setCurrentAccessToken(AccessToken.getCurrentAccessToken());
                SharedPreferences sp = getApplicationContext().getSharedPreferences
                        ("auth", 0);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("fbToken", AccessToken.getCurrentAccessToken().getToken());
                editor.putString("fbTokenExpires", (new DateTime(AccessToken.getCurrentAccessToken
                        ().getExpires())).toString());
                editor.apply();

                handler.post(initiateSetGovLogin);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel");

            }

            @Override
            public void onError(FacebookException e) {
                Log.d(TAG, "onError");
                e.printStackTrace();
            }
        });
    }

    private void kickOffSetGovLoginApiRequest() {
        String postDataJson = "mutation{authenticateUser(facebook_token: \"" + facebookAccessTokenString +"\"){id,full_name,facebook_id,profileImage{url},home_city,eventsAttending{id}}}";
        Log.d(TAG, postDataJson);
        activeApiCall = new ApiGraphRequestTask(getApplicationContext());
        activeApiCall.run(postDataJson, new
                Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "ApiPostRequestTask: onFailure: "+ e.toString());
                        activeApiCall = null;
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        activeApiCall = null;
                        try {
                            JSONObject jsonResponse = new JSONObject(response.body().string());

                            Log.d(TAG, jsonResponse.toString());
                            JSONObject userLoginJSON = jsonResponse.getJSONObject("data").getJSONObject("authenticateUser");

                            Log.d(TAG, jsonResponse.toString());
                            user = new User(userLoginJSON);

                            SharedPreferences sp = getApplicationContext().getSharedPreferences
                                    ("auth", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("loggedInUserJson", user.getUserJsonStr());
                            editor.putBoolean("isLoggedIn",true);
                            editor.apply();

                            goToMainActivity(user);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
    }
}

