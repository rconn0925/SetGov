package com.setgov.android.models;

import java.io.Serializable;

/**
 * Created by Ross on 9/18/2017.
 */

public class Representative implements Serializable {
    private Office office;
    private String name;
    private String party;
    private String photoUrl;
    private String url;

    public Representative(String officialName, String officialParty, String officialPhotoUrl,String officialUrl){
        this.name = officialName;
        this.party = officialParty;
        this.photoUrl = officialPhotoUrl;
        this.url = officialUrl;
    }
    public Representative(Office office,String officialName, String officialParty, String officialPhotoUrl,String officialUrl){
        this.office = office;
        this.name = officialName;
        this.party = officialParty;
        this.photoUrl = officialPhotoUrl;
        this.url = officialUrl;
    }
    public void setOffice(Office o){
        this.office = o;
    }
    public String getUrl(){return url;}
    public Office getOffice(){
        return office;
    }
    public String getName(){
        return name;
    }
    public String getParty(){
        return party;
    }
    public String getPhotoUrl(){
        return photoUrl;
    }
}
