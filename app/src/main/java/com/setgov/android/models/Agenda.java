package com.setgov.android.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Ross on 6/14/2017.
 */

public class Agenda implements Serializable {

    private String title;
    private String category;
    private String comments;
    private Event event;
    private int agendaID;
    private int eventID;
    private String description;
    private String text;

    public Agenda (JSONObject json){
        try {
            if (json.has("id")) agendaID = json.getInt("id");
            if (json.has("name")) title = json.getString("title");
            if (json.has("description")) title = json.getString("description");
            if (json.has("text")) text = json.getString("text");
            if (json.has("event")) eventID = json.getJSONObject("event").getInt("id");

        } catch (JSONException e){

        }
    }

    public Agenda(String title, String category,String comments, Event event){
        this.title = title;
        this.category = category;
        this.comments = comments;
        this.event = event;
    }
    public Event getEvent(){
        return event;
    }
    public String getTitle(){
        return title;
    }
    public String getCategory(){
        return category;
    }
    public String getComments(){
        return comments;
    }
}
