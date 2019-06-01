package com.ujjawalayush.example.managemyyatra;

public class RecyclerData9 {
    String title,trip,friend,flip,name,username,focus;
    public String getComment(){
        return title;
    }
    public String getTime(){return trip;}
    public String getDate(){return friend;}
    public String getName(){return name;}
    public String getImage(){return username;}
    public String getFocus(){return focus;}


    public void setComment(String title){
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
    public void setImage(String username){
        this.username=username;
    }
    public void setFocus(String focus){
        this.focus=focus;
    }



}
