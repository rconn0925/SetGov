package com.setgov.android.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ross on 6/18/2017.
 */

public class User implements Serializable{

    private String full_name;
    private int id;
    private int facebook_id;
    private String profileImageUrl;
    private String home_city;
    private String userJsonStr;
    private ArrayList<Integer> eventsAttending;



    private int mImageResID;

    public User(JSONObject json){
        userJsonStr = json.toString();
        try{
            eventsAttending = new ArrayList<>();
            if(json.has("id")) id =  json.getInt("id");
            if(json.has("full_name")) full_name =  json.getString("full_name");
            if(json.has("facebook_id")) facebook_id =  json.getInt("facebook_id");
            if(json.has("profileImage")) profileImageUrl =  json.getJSONObject("profileImage").getString("url");
            if(json.has("home_city")) home_city =  json.getString("home_city");
            if(json.has("eventsAttending")){
                JSONArray eventsAttendingJson =  json.getJSONArray("eventsAttending");
               // eventsAttending = new int[events.length()];
                for(int i = 0; i <eventsAttendingJson.length();i++){
                    eventsAttending.add(eventsAttendingJson.getJSONObject(i).getInt("id"));
                }
            }
        } catch (JSONException e) {

        }
    }

    public User(String name, int imageResID){
        this.full_name = name;
        this.mImageResID = imageResID;
    }

    public void unattendEvent(int eventID){
        for(int i = 0; i < eventsAttending.size();i++){
            if(eventsAttending.get(i) == eventID){
                eventsAttending.remove(i);
            }
        }
    }
    public void attendEvent(int eventID){
        if(!eventsAttending.contains(eventID)) {
            eventsAttending.add(eventID);
        }
    }
    public boolean isAttendingEvent(int eventID){
        for(int i = 0; i<eventsAttending.size();i++){
            if(eventsAttending.get(i) == eventID){
                return true;
            }
        }
        return false;
    }
    public int getID(){return id;}
    public void setHomeCity(String city){
        home_city = city;
    }
    public String getProfileImageUrl(){return profileImageUrl;}
    public String getUserJsonStr(){return userJsonStr;}
    public String getName(){
        return full_name;
    }
    public int getImageResID(){
        return mImageResID;
    }
    public ArrayList<Integer> getAttendingEvents(){return eventsAttending;}
}
