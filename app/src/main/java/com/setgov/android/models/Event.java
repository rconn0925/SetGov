package com.setgov.android.models;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ross on 6/14/2017.
 */

public class Event implements Serializable{

    private int id;
    private String name;
    private String type;
   // private Date date;
    private String address;
    private String[] tags;
    private ArrayList<User> attendees;
    private ArrayList<Agenda> agendaItems;
    private ArrayList<Comment> comments;
    private int imageResID;
    private City city;
    private String cityStr;
    private String dateStr;
    private String time;
    private String description;


    public Event(JSONObject json){
        attendees = new ArrayList<>();
        agendaItems = new ArrayList<>();
        comments = new ArrayList<>();
        try {
            if(json.has("id")) id =  json.getInt("id");
            if (json.has("name")) name =  json.getString("name");
            if (json.has("city")) cityStr =  json.getString("city");
            if(cityStr.equals("Boston")){
                city = new City("Boston","Massachusetts");
            } else if(cityStr.equals("Fort Lauderdale")){
                city = new City("Fort Lauderdale","Florida");
            } else if(cityStr.equals("Phoenix")){
                city = new City("Phoenix","Arizona");
            } else if(cityStr.equals("San Jose")){
                city = new City("San Jose","California");
            } else if(cityStr.equals("New York")){
                city = new City("New York","New York");
            } else if(cityStr.equals("Miami")){
                city = new City("Miami","Florida");
            }  else if(cityStr.equals("Austin")){
                city = new City("Austin","Texas");
            }
            if (json.has("address")) address =  json.getString("address");
            if (json.has("date")){
                dateStr =  json.getString("date");
                String day,month,year;
                String[] dateParts = dateStr.split("/");
                month = dateParts[0];
                int monthInt = Integer.parseInt(month);
                String[] str = {"January",
                        "February",
                        "March",
                        "April",
                        "May",
                        "June",
                        "July",
                        "August",
                        "September",
                        "October",
                        "November",
                        "December"};
                if(monthInt<=str.length){
                    month = str[monthInt-1];
                }
                else{
                    month = "Invalid month";
                }
                day = dateParts[1];
                year = "20"+dateParts[2];
                dateStr = month + " " + day + ", "+ year;
            }
            if (json.has("time")){
                time =  json.getString("time");
                if(time.startsWith("0")){
                    time = time.substring(1);
                }
                if(time != null &&time.length()>0){
                    time = time.substring(0,time.length()-3);
                    time+="PM";
                }
            }
            if (json.has("description")) description =  json.getString("description");
            if (json.has("attendingUsers")){
                JSONArray usersJson =  json.getJSONArray("attendingUsers");
                for (int i = 0; i < usersJson.length();i++){
                    User user = new User(usersJson.getJSONObject(i));
                    attendees.add(user);
                }
            }
            if (json.has("comments")){
                JSONArray commentsJson =  json.getJSONArray("comments");
                for (int i = 0; i < commentsJson.length();i++){
                    Comment comment = new Comment(commentsJson.getJSONObject(i));
                    comments.add(comment);
                }
            }
            if (json.has("agendaItems")){
                JSONArray agendaItemsJson =  json.getJSONArray("agendaItems");
                for (int i = 0; i < agendaItemsJson.length();i++){
                    Agenda agenda = new Agenda(agendaItemsJson.getJSONObject(i));
                    agendaItems.add(agenda);
                }
            }
        } catch (JSONException e){

        }
    }

    public Event(String name, String type, City city, Date date, String address, String[] tags, ArrayList<User> attendees, int resID){
        this.name = name;
        this.type = type;
      //  this.date = date;
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

    public ArrayList<Comment> getComments(){return comments;}
    public int getId() {return id;}
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
    public String getDateStr(){
        return dateStr;
    }
    public String getAddress(){
        return address;
    }
    public String[] getTags(){
        return tags;
    }
    public ArrayList<User> getAttendees(){
        return attendees;
    }
    public String getDescription() {return description;}
    public String getTime() {return time;}

}
