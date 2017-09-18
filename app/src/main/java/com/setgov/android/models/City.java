package com.setgov.android.models;

import java.io.Serializable;

/**
 * Created by Ross on 6/14/2017.
 */

public class City implements Serializable{

    private String cityName;
    private String state;
    private String stateShort;
    private String county;

    public City(String cityName,String state, String county, String stateShort){
        this.cityName = cityName;
        this.state = state;
        this.stateShort = stateShort;
        this.county = county;
    }
    public String getStateShort(){return stateShort;}
    public String getCounty(){return county;}
    public String getCityName(){
        return cityName;
    }
    public String getState() {return state;}
    public String toString(){
        return getCityName() + ", " + getState();
    }
}
