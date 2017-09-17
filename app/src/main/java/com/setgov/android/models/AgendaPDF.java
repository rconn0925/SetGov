package com.setgov.android.models;

/**
 * Created by Ross on 9/16/2017.
 */

public class AgendaPDF {
    private Event event;
    private String pdfURL;

    public AgendaPDF(Event event, String pdfURL){
        this.event = event;
        this.pdfURL = pdfURL;
    }

    public String getPdfURL(){
        return pdfURL;
    }
    public Event getEvent(){
        return event;
    }
}
