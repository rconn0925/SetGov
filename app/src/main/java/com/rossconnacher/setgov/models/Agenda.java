package com.rossconnacher.setgov.models;

import java.io.Serializable;

/**
 * Created by Ross on 6/14/2017.
 */

public class Agenda implements Serializable {

    private String title;
    private String category;
    private String comments;
    private City city;

    public Agenda(String title, String category,String comments, City city){
        this.title = title;
        this.category = category;
        this.comments = comments;
        this.city = city;
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
