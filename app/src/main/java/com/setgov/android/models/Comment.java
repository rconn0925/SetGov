package com.setgov.android.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Ross on 9/6/2017.
 */

public class Comment implements Serializable {
    private static final String TAG= "Comment";
    private String eventCity;
    private int id;
    private int eventID;
    private User user;
    private String text;
    private int karma;
    private String timestamp;
    private ArrayList<Vote> votes;
    private ArrayList<Integer> replies;
    private int parentCommentID;

    public Comment(JSONObject json){ Log.d(TAG,"comment constructor");
        try{
            votes = new ArrayList<>();
            replies = new ArrayList<>();
            if(json.has("id")) id =  json.getInt("id");
            if (json.has("event")) eventID = json.getJSONObject("event").getInt("id");
            if (json.has("event")) eventCity = json.getJSONObject("event").getString("city");
            if (json.has("user")) user = new User(json.getJSONObject("user"));
            if (json.has("text")) text = json.getString("text");
            if (json.has("karma")) karma = json.getInt("karma");
            if (json.has("timestamp")){
                timestamp = json.getString("timestamp");
            }
            if (json.has("votes")){
                JSONArray votesJsonArray = json.getJSONArray("votes");
                for(int i = 0; i < votesJsonArray.length();i++){
                    if(votesJsonArray.length()>0) {
                        JSONObject voteJson = votesJsonArray.getJSONObject(i);
                        Vote vote = new Vote(voteJson);
                        votes.add(vote);
                    }
                }
            }
           // if (json.has("parentComment")) parentCommentID = json.getJSONObject("parentComment").getInt("id");
            if (json.has("replies")){
                JSONArray repliesJson = json.getJSONArray("replies");
                Log.d(TAG,"comment has replies: " + repliesJson.toString());
                for(int i = 0; i<repliesJson.length();i++) {
                    Log.d(TAG,"comment reply: " + repliesJson.get(i).toString());
                    replies.add(repliesJson.getJSONObject(i).getInt("id"));
                }
            }
        }
        catch (JSONException e){
            Log.w(TAG,"comment error: " + e.getLocalizedMessage());
        }
    }

    public ArrayList<Integer> getReplies(){return replies;}
    public int getParentCommentID(){return parentCommentID;}
    public ArrayList<Vote> getVotes(){return votes;}
    public int getId() {return id;}
    public int getEventID(){return eventID;}
    public User getUser(){return user;}
    public String getText(){return text;}
    public int getKarma(){return karma;}
    public String getTimestamp(){return timestamp;}
    public String getEventCity(){return eventCity;}

}
