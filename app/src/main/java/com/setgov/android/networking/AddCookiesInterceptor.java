package com.setgov.android.networking;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;

/**
 * Created by oscarlafarga on 9/4/15.
 */

/**
 * This interceptor put all the Cookies in Preferences in the Request.
 * Your implementation on how to get the Preferences MAY VARY.
 * <p>
 * Created by tsuharesu on 4/1/15.
 */
public class AddCookiesInterceptor implements Interceptor {

    private Context context;

    public AddCookiesInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        okhttp3.Request.Builder builder = chain.request().newBuilder();
        SharedPreferences sp = context.getSharedPreferences("cookies", 0);
        HashSet<String> preferences = (HashSet<String>) sp.getStringSet("cookies", new HashSet<String>());
        Log.d("AddCookies: ", preferences.toString());

        String cookieString = "";

        for (String cookie : preferences) {
            Log.d("AddCookies: ", cookie);

            cookieString += cookie;
        }

        builder.addHeader("Cookie", cookieString);

        okhttp3.Request request = builder.build();

        Log.d("AddCookies: ", request.headers().toString());

        return chain.proceed(request);
    }
}