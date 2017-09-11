package com.setgov.android.networking;

import android.content.Context;
import android.util.Log;

import com.setgov.android.util.Constants;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by oscarlafarga on 6/15/16.
 */

public class ApiGraphRequestTask extends ApiRequestTask {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public ApiGraphRequestTask(Context context) {super(context);
    }

    public void run(String query, Callback callback) {
        String validatedJson = validateGraphQuery(query);

        RequestBody body = RequestBody.create(JSON, validatedJson);

        Request request = new Request.Builder()
                .url(Constants.API_ROOT)
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
        this.call = call;
    }

    private String validateGraphQuery(String query) {
        Log.d("validateGraphQuery", query);
        String encodedQuery = query.replaceAll("\"", "\\\\\"");
        Log.d(" replaced", encodedQuery);

        String validatedQuery = "{\"query\":\"" + encodedQuery + "\"}";

        Log.d("validateGraphQuery", validatedQuery);

        return validatedQuery;
    }
}
