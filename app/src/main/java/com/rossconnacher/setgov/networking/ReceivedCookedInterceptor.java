package com.rossconnacher.setgov.networking;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;

/**
 * Created by oscarlafarga on 9/4/15.
 */
public class ReceivedCookedInterceptor implements Interceptor {
    private Context context;

    public ReceivedCookedInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        okhttp3.Response originalResponse = chain.proceed(chain.request());
        Log.d("ReceivedCookedIntor", originalResponse.toString());

        Log.d("ReceivedCookedIntor", originalResponse.headers().toString());
        Log.d("ReceivedCookedIntor", originalResponse.cacheControl().toString());

        Log.d("ReceivedCookedIntor", originalResponse.headers("Set-Cookie").toString());


        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            Log.d("ReceivedCookedIntor", "storing cookies");

            HashSet<String> cookies = new HashSet<>();

            for (String header : originalResponse.headers("set-cookie")) {
                Log.d("ReceivedCookedIntor", header.split(";")[0]);
                cookies.add(header.split(";")[0] + ";");
            }

            SharedPreferences sp = context.getSharedPreferences("cookies", 0);
            SharedPreferences.Editor editor = sp.edit();
            editor.putStringSet("cookies", cookies);
            editor.apply();
        }

        return originalResponse;
    }
}
