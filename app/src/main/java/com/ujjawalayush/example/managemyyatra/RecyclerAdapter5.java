package com.ujjawalayush.example.managemyyatra;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecyclerAdapter5 extends RecyclerView.Adapter<RecyclerAdapter5.RecyclerItemViewHolder5> {

    private ArrayList<RecyclerData5> myList;
    int mLastPosition = 0, request_code = 1;
    long id2;
    String m, n, friend;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseUser user;
    public RecyclerAdapter5(ArrayList<RecyclerData5> myList) {
        this.myList = myList;
    }

    public RecyclerItemViewHolder5 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row5, parent, false);
        RecyclerItemViewHolder5 holder = new RecyclerItemViewHolder5(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerItemViewHolder5 holder, final int position) {
        Log.d("onBindViewHolder ", myList.size() + "");
        holder.etTitleTextView.setText("Rating: " + myList.get(position).getTrip());
        holder.etFriendTextView.setText("Comment:" + myList.get(position).getGroup());
        holder.etTripTextView.setText("Rating given by " + myList.get(position).getFriend());
        mLastPosition = position;
        m = myList.get(position).getGroup();
        n = myList.get(position).getTrip();
        friend = myList.get(position).getFriend();
    }

    @Override
    public int getItemCount() {
        return (null != myList ? myList.size() : 0);
    }
    public void notifyData1(ArrayList<RecyclerData5> myList){
        this.myList = myList;
        notifyItemRangeRemoved(0,myList.size());
    }
    public void notifyData(ArrayList<RecyclerData5> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.myList = myList;
        notifyDataSetChanged();
    }

    public class RecyclerItemViewHolder5 extends RecyclerView.ViewHolder{
        private final TextView etTitleTextView,etFriendTextView,etTripTextView;
        LinearLayout mainLayout;
        public String title, description, date;
        ImageView imageView;
        public RecyclerItemViewHolder5(View parent) {
            super(parent);
            etTitleTextView = (TextView) parent.findViewById(R.id.txtTitle5);
            etTripTextView = (TextView) parent.findViewById(R.id.txtTrip5);
            etFriendTextView = (TextView) parent.findViewById(R.id.txtFriend5);
            imageView=(ImageView) parent.findViewById(R.id.imageView3);
            mainLayout = (LinearLayout) parent.findViewById(R.id.mainLayout5);
        }

    }
}


