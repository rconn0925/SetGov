package com.setgov.android.eventscrapers;

import android.os.AsyncTask;
import android.util.Log;

import com.setgov.android.models.City;
import com.setgov.android.models.Event;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Ross on 6/22/2017.
 */

public class BostonEventScraper extends AsyncTask<String,Void,ArrayList<Event>>{

    @Override
    protected ArrayList<Event> doInBackground(String... urls) {

            ArrayList<String> endpoints,titles,categories,whens,wheres;
            endpoints = new ArrayList<>();
            titles = new ArrayList<>();
            whens = new ArrayList<>();
            wheres = new ArrayList<>();
            categories = new ArrayList<>();

            ArrayList<Event> mEvents = new ArrayList<>();

            for(int numPages = 0;numPages<5;numPages++){
                Document doc = null;
                try {
                    String url = "https://www.boston.gov/public-notices?title=&&page=" + numPages;
                    doc = Jsoup.connect(url).get();
                    //       Log.d("MAINactivity", doc.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Elements events = doc.select("div[class=g g--m0 n-li]");
                Elements title = events.select("a");

                for(int i =0;i<title.size();i++){
                    String[] data = title.get(i).toString().substring(10).split("\"");
                    if(i%2==0){
                        String endPoint = data[0];
                        String name = data[2].replace("&amp;","&");
                        endpoints.add(endPoint);
                        titles.add(name);
                        Log.d("MAINactivity", endPoint + " "+ name);
                    } else {
                        String category = data[2].replace("&amp;","&");
                        categories.add(category);
                    }
                }
            /*
            for(int j = 0;j<events.size();j++){
                Log.d("MAINactivity", events.toString());
            }
            */
                Elements whenwhere = events.select("li[class=dl-i]");
                Elements when = whenwhere.select("span[class=dl-d]");
                Elements where = whenwhere.select("div[class=thoroughfare]");
                for(int k = 0; k <when.size();k++ ){
                    // Log.d("MAINactivity", when.get(k).toString());
                    if(k%3==2){

                        String dateString = when.get(k).toString().split(">")[1].split("<")[0].replace("\n","");
                        Log.d("MAINactivity", dateString);
                        whens.add(dateString);
                    }
                }
                for(int l = 0; l <where.size();l++ ){
                    String address = where.get(l).toString().split(">")[1].split("<")[0].replace("\n","");
                    Log.d("MAINactivity", address);
                    wheres.add(address);
                }
            }

            for(int m = 0; m<endpoints.size();m++){

                /*
                Document agendaDoc = null;
                try {
                    String url = "https://www.boston.gov/"+endpoints.get(m);
                    agendaDoc = Jsoup.connect(url).get();
                    //       Log.d("MAINactivity", doc.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Elements agendas = agendaDoc.select("div[class=body]");
                Elements agendaTitles = agendas.select("strong").after("li").after("ol");

                Log.d("TAG",agendaTitles.toString());
                */

                City boston = new City("Boston","Massachusetts","Suffolk","MA");
                Event event = new Event(titles.get(m),categories.get(m),boston,whens.get(m),wheres.get(m));
                mEvents.add(event);



            }

        return mEvents;
    }
}
