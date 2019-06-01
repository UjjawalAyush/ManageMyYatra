package com.ujjawalayush.example.managemyyatra;

public class RecyclerData6 {
    String title,trip,friend,flip,name;
    public String getCategory(){
        return title;
    }
    public String getComment(){return trip;}
    public String getMoney(){return friend;}
    public String getImage(){return flip;}
    public String getName(){return name;}

    public void setCategory(String title){
        this.title=title;
    }
    public void setComment(String trip){
        this.trip=trip;
    }
    public void setImage(String flip){
        this.flip=flip;
    }
    public void setName(String name){
        this.name=name;
    }

    public void setMoney(String friend){
        this.friend=friend;
    }


}
