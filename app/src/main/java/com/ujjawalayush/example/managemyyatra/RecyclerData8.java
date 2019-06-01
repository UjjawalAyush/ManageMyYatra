package com.ujjawalayush.example.managemyyatra;

public class RecyclerData8 {
    String title,trip,friend,flip,name,username;
    public String getMessage(){
        return title;
    }
    public String getTime(){return trip;}
    public String getDate(){return friend;}
    public String getName(){return name;}
    public String getUsername(){return username;}

    public void setMessage(String title){
        this.title=title;
    }
    public void setTime(String trip){
        this.trip=trip;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setDate(String friend){
        this.friend=friend;
    }
    public void setUsername(String username){
        this.username=username;
    }


}
