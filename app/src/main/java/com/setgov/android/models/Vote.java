package com.setgov.android.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ross on 9/6/2017.
 */

public class Vote {

    private Comment comment;
    private User user;
    private int id;
    private int vote_value;

    public Vote(JSONObject json){
        try {
            if (json.has("id")) id = json.getInt("id");
            if (json.has("vote_value")) vote_value = json.getInt("vote_value");
            if (json.has("user")){
                user = new User(json.getJSONObject("user"));
            }
            if (json.has("comment")){
                comment = new Comment(json.getJSONObject("comment"));
            }
        } catch ( JSONException e){

        }
    }

    public int getID(){return id;}
    public int getVoteValue(){return  vote_value;}
    public User getUser(){return user;}
    public Comment getComment(){return comment;}
}
