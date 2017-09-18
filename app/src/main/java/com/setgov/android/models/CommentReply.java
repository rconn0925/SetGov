package com.setgov.android.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ross on 9/17/2017.
 */

public class CommentReply implements Serializable {
    private static final String TAG= "CommentReply";
    private String eventCity;
    private int id;
    private int eventID;
    private User user;
    private String text;
    private int karma;
    private String timestamp;
    private ArrayList<Vote> votes;
    private ArrayList<Comment> replies;
    private int parentCommentID;

    public CommentReply(JSONObject json){
        Log.d(TAG,"comment constructor");
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
                    replies.add(new Comment(repliesJson.getJSONObject(i)));
                }
            }
        }
        catch (JSONException e){
            Log.w(TAG,"comment error: " + e.getLocalizedMessage());
        }
    }

    public ArrayList<Comment> getReplies(){return replies;}
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
