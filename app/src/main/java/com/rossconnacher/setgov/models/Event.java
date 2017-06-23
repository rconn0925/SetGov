package com.rossconnacher.setgov.models;

import android.graphics.Bitmap;
import android.media.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ross on 6/14/2017.
 */

public class Event implements Serializable{

    private String name;
    private String type;
    private Date date;
    private String address;
    private String[] tags;
    private ArrayList<Person> attendees;
    private int imageResID;
    private City city;
    private String dateStr;


    public Event(String name,String type, City city, Date date, String address, String[] tags,ArrayList<Person> attendees,int resID){
        this.name = name;
        this.type = type;
        this.date = date;
        this.address = address;
        this.tags = tags;
        this.attendees = attendees;
        this.imageResID = resID;
        this.city = city;
    }
    public Event(String name,String type, City city, String date, String address){
        this.name = name;
        this.type = type;
        this.dateStr = date;
        this.address = address;
        this.city = city;
    }


    public int getImageResID(){
        return imageResID;
    }
    public City getCity(){
        return city;
    }
    public String getName(){
        return name;
    }
    public String getType(){
        return type;
    }
    public Date getDate(){
        return date;
    }
    public String getDateStr(){
        return dateStr;
    }
    public String getAddress(){
        return address;
    }
    public String[] getTags(){
        return tags;
    }
    public ArrayList<Person> getAttendees(){
        return attendees;
    }
}
