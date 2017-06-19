package com.rossconnacher.setgov.models;

import java.io.Serializable;

/**
 * Created by Ross on 6/14/2017.
 */

public class Agenda implements Serializable {

    private String title;
    private String category;
    private String comments;
    private Event event;

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
