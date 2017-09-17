package com.setgov.android.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Ross on 9/6/2017.
 */

public class Vote implements Serializable {

    private int commentID;
    private int userID;
    private int id;
    private int vote_value;

    public Vote(JSONObject json){
        try {
            if (json.has("id")) id = json.getInt("id");
            if (json.has("vote_value")) vote_value = json.getInt("vote_value");
            if (json.has("user")){
                userID = json.getJSONObject("user").getInt("id");
            }
            if (json.has("comment")){
                commentID = json.getJSONObject("comment").getInt("id");
            }
        } catch ( JSONException e){

        }
    }

    public int getID(){return id;}
    public int getVoteValue(){return  vote_value;}
    public int getUser(){return userID;}
    public int getComment(){return commentID;}
}
