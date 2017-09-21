package com.setgov.android.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ross on 9/18/2017.
 */

public class Office implements Serializable{
    private static final String TAG = "Office";
    
    private String name;
    private String divisionId;
    private ArrayList<Integer> officialIndicies;

    public Office(JSONObject jsonObject) {
        officialIndicies = new ArrayList<>();
        try {
            if (jsonObject.has("name")) {
                this.name = jsonObject.getString("name");
            }
            if (jsonObject.has("officialIndices")){
                JSONArray officialIndiciesJson = jsonObject.getJSONArray("officialIndices");
                for (int i = 0; i < officialIndiciesJson.length();i++){
                    officialIndicies.add(officialIndiciesJson.getInt(i));
                }
            }
            if(jsonObject.has("divisionId")){
                this.divisionId = jsonObject.getString("divisionId");
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public Office(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public String getDivisionId(){
        return divisionId;
    }
    public ArrayList<Integer> getOfficialIndicies(){
        return officialIndicies;
    }
}
