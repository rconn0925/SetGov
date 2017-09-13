package com.setgov.android.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ross on 9/6/2017.
 */

public class Comment {
    private String eventCity;
    private int id;
    private int eventID;
    private User user;
    private String text;
    private int karma;
    private String timestamp;
    private ArrayList<Vote> votes;

    public Comment(JSONObject json){
        try{
            if(json.has("id")) id =  json.getInt("id");
            if (json.has("event")) eventID = json.getJSONObject("event").getInt("id");
            if (json.has("event")) eventCity = json.getJSONObject("event").getString("city");
            if (json.has("user")) user = new User(json.getJSONObject("user"));
            if (json.has("text")) text = json.getString("text");
            if (json.has("karma")) karma = json.getInt("karma");
            if (json.has("timestamp")) timestamp = json.getString("timestamp");
            if (json.has("votes")){
                JSONArray votesJsonArray = json.getJSONArray("votes");
                for(int i = 0; i < votesJsonArray.length();i++){
                    JSONObject voteJson = votesJsonArray.getJSONObject(i);
                    Vote vote = new Vote(voteJson);
                    votes.add(vote);
                }
            }
        }
        catch (JSONException e){

        }
    }

    public ArrayList<Vote> getVotes(){return votes;}
    public int getId() {return id;}
    public int getEventID(){return eventID;}
    public User getUser(){return user;}
    public String getText(){return text;}
    public int getKarma(){return karma;}
    public String getTimestamp(){return timestamp;}
    public String getEventCity(){return eventCity;}

}
