package com.rossconnacher.setgov.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Ross on 6/18/2017.
 */

public class User implements Serializable{

    private String full_name;
    private int id;
    private int facebook_id;
    private String profileImageUrl;
    private String home_city;


    private int mImageResID;

    public User(JSONObject json){
        try{
            if(json.has("id")) id =  json.getInt("id");
            if(json.has("full_name")) full_name =  json.getString("full_name");
            if(json.has("facebook_id")) facebook_id =  json.getInt("facebook_id");
            if(json.has("profileImage")) profileImageUrl =  json.getJSONObject("profileImage").getString("url");
            if(json.has("home_city")) home_city =  json.getString("home_city");
        } catch (JSONException e) {

        }
    }

    public User(String name, int imageResID){
        this.full_name = name;
        this.mImageResID = imageResID;
    }

    public String getName(){
        return full_name;
    }
    public int getImageResID(){
        return mImageResID;
    }
}
