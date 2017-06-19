package com.rossconnacher.setgov.models;

import java.io.Serializable;

/**
 * Created by Ross on 6/18/2017.
 */

public class Person implements Serializable{

    private String mName;
    private int mImageResID;
    public Person(String name, int imageResID){
        this.mName = name;
        this.mImageResID = imageResID;
    }

    public String getName(){
        return mName;
    }
    public int getImageResID(){
        return mImageResID;
    }
}
