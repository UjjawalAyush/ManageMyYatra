package com.ujjawalayush.example.managemyyatra;

public class RecyclerData7 {
    String title,trip,friend,flip,name;
    public String getCategory(){
        return title;
    }
    public String getComment(){return trip;}
    public String getDate(){return friend;}
    public String getName(){return name;}

    public void setCategory(String title){
        this.title=title;
    }
    public void setComment(String trip){
        this.trip=trip;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setDate(String friend){
        this.friend=friend;
    }


}
