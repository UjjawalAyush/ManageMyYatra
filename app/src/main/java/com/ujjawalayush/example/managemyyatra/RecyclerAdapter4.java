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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecyclerAdapter4 extends RecyclerView.Adapter<RecyclerAdapter4.RecyclerItemViewHolder4> {

    private ArrayList<RecyclerData4> myList;
    int mLastPosition = 0, request_code = 1;
    long id2;
    String m, n, friend;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseUser user;
    public RecyclerAdapter4(ArrayList<RecyclerData4> myList) {
        this.myList = myList;
    }

    public RecyclerItemViewHolder4 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row4, parent, false);
        RecyclerItemViewHolder4 holder = new RecyclerItemViewHolder4(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerItemViewHolder4 holder, final int position) {
        Log.d("onBindViewHolder ", myList.size() + "");
        holder.etTitleTextView.setText("Category: " + myList.get(position).getGroup());
        holder.etFriendTextView.setText("Description: " + myList.get(position).getFriend());
        holder.etTripTextView.setText("Recommended by " + myList.get(position).getTrip());

        mLastPosition = position;
        m = myList.get(position).getGroup();
        n = myList.get(position).getTrip();
        friend = myList.get(position).getFriend();
    }

    @Override
    public int getItemCount() {
        return (null != myList ? myList.size() : 0);
    }
    public void notifyData1(ArrayList<RecyclerData4> myList){
        this.myList = myList;
        notifyItemRangeRemoved(0,myList.size());
    }
    public void notifyData(ArrayList<RecyclerData4> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.myList = myList;
        notifyDataSetChanged();
    }

    public class RecyclerItemViewHolder4 extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView etTitleTextView,etFriendTextView,etTripTextView;
        LinearLayout mainLayout;
        public String title, description, date;

        public RecyclerItemViewHolder4(View parent) {
            super(parent);
            etTitleTextView = (TextView) parent.findViewById(R.id.txtTitle4);
            etTripTextView = (TextView) parent.findViewById(R.id.txtTrip4);
            etFriendTextView = (TextView) parent.findViewById(R.id.txtFriend4);

            mainLayout = (LinearLayout) parent.findViewById(R.id.mainLayout4);
            parent.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int i=getAdapterPosition();
            Context context=v.getContext();
            Toast.makeText(context,"View Reactions",Toast.LENGTH_SHORT).show();
            Intent data = new Intent(context,Reactions.class);
            Bundle extras = new Bundle();
            extras.putString("category",myList.get(i).getGroup());
            extras.putString("description", myList.get(i).getFriend());
            extras.putString("username", myList.get(i).getTrip());
            extras.putString("trip", myList.get(i).getFlip());

            extras.putString("Class","B");
            data.putExtra("extras", extras);
            context.startActivity(data);
        }
    }
}


