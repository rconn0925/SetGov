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
    public City(String cityStr){
        if(cityStr.equals("Boston")){
            this.cityName = "Boston";
            this.state = "Massachusetts";
            this.stateShort = "MA";
            this.county = "Suffolk";
        } else if(cityStr.equals("Fort Lauderdale")){
            this.cityName = "Fort Lauderdale";
            this.state = "Florida";
            this.stateShort = "FL";
            this.county = "Broward";
        } else if(cityStr.equals("Phoenix")){
            this.cityName = "Phoenix";
            this.state = "Arizona";
            this.stateShort = "AZ";
            this.county = "Maricopa";
        } else if(cityStr.equals("San Jose")){
            this.cityName = "San Jose";
            this.state = "California";
            this.stateShort = "CA";
            this.county = "Santa Carla";
        } else if(cityStr.equals("New York")){
            this.cityName = "New York";
            this.state = "New York";
            this.stateShort = "NY";
            this.county = "New York";
        } else if(cityStr.equals("Miami")){
            this.cityName = "Miami";
            this.state = "Florida";
            this.stateShort = "FL";
            this.county = "Miami-Dade";
        }  else if(cityStr.equals("Austin")){
            this.cityName = "Austin";
            this.state = "Texas";
            this.stateShort = "TX";
            this.county = "Travis";
        }
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
