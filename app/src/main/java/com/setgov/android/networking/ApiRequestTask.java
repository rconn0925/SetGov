package com.setgov.android.networking;

import android.content.Context;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.util.concurrent.TimeUnit;

import okhttp3.CookieJar;

public class ApiRequestTask {
    public okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
    public okhttp3.Call call;
    public Context context;

    public ApiRequestTask(Context context) {
        this.context = context;

        CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new
                SharedPrefsCookiePersistor(context));


        client = client.newBuilder()
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
//                .cookieJar(cookieJar)
                .addInterceptor(new ReceivedCookedInterceptor(context))
                .addInterceptor(new AddCookiesInterceptor(context))
                .followSslRedirects(true)
                .build();
    }

    public void cancel() {
        call.cancel();
    }
}
