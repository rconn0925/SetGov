package com.setgov.android.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ross on 9/6/2017.
 */

public class Comment {
    private int id;
    private int eventID;
    private User user;
    private String text;
    private int karma;
    private String timestamp;

    public Comment(JSONObject json){
        try{
            if(json.has("id")) id =  json.getInt("id");
            if (json.has("event")) eventID = json.getJSONObject("event").getInt("id");
            if (json.has("user")) user = new User(json.getJSONObject("user"));
            if (json.has("text")) text = json.getString("text");
            if (json.has("karma")) karma = json.getInt("karma");
            if (json.has("timestamp")) timestamp = json.getString("timestamp");
        }
        catch (JSONException e){

        }
    }
}
