package com.rossconnacher.setgov.models;

import android.media.Image;

import java.io.Serializable;

/**
 * Created by Ross on 6/14/2017.
 */

public class City implements Serializable{

    private String cityName;
    private String state;

    public City(String cityName,String state){
        this.cityName = cityName;
        this.state = state;
    }
    public String getCityName(){
        return cityName;
    }
    public String getState() {return state;}
    public String toString(){
        return getCityName() + ", " + getState();
    }
}
