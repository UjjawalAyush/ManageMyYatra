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

public class RecyclerAdapter3 extends RecyclerView.Adapter<RecyclerAdapter3.RecyclerItemViewHolder3> {

    private ArrayList<RecyclerData3> myList;
    int mLastPosition = 0, request_code = 1;
    long id2;
    String m, n, friend;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseUser user;
    public RecyclerAdapter3(ArrayList<RecyclerData3> myList) {
        this.myList = myList;
    }

    public RecyclerItemViewHolder3 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row1, parent, false);
        RecyclerItemViewHolder3 holder = new RecyclerItemViewHolder3(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerItemViewHolder3 holder, final int position) {
        Log.d("onBindViewHolder ", myList.size() + "");
        holder.etTitleTextView.setText("" + myList.get(position).getGroup());
        mLastPosition = position;
        m = myList.get(position).getGroup();
        n = myList.get(position).getTrip();
        friend = myList.get(position).getFriend();
    }

    @Override
    public int getItemCount() {
        return (null != myList ? myList.size() : 0);
    }
    public void notifyData1(ArrayList<RecyclerData3> myList){
        this.myList = myList;
        notifyItemRangeRemoved(0,myList.size());
    }
    public void notifyData(ArrayList<RecyclerData3> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.myList = myList;
        notifyDataSetChanged();
    }

    public class RecyclerItemViewHolder3 extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView etTitleTextView;
        LinearLayout mainLayout;
        public String title, description, date;

        public RecyclerItemViewHolder3(View parent) {
            super(parent);
            etTitleTextView = (TextView) parent.findViewById(R.id.txtTitle1);
            mainLayout = (LinearLayout) parent.findViewById(R.id.mainLayout1);
            title = etTitleTextView.getText().toString();
            parent.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int i=getAdapterPosition();
            firebaseAuth=FirebaseAuth.getInstance();
            user=firebaseAuth.getCurrentUser();
            databaseReference=FirebaseDatabase.getInstance().getReference().child("Users");
            Context context = v.getContext();
            Toast.makeText(context,"Friend Successfully added to the trip",Toast.LENGTH_LONG).show();
            Intent data = new Intent(context,Group.class);
            Bundle extras = new Bundle();
            extras.putString("username",myList.get(i).getGroup());
            extras.putString("uid", myList.get(i).getFriend());
            extras.putString("trip", myList.get(0).getTrip());
            extras.putString("Class","B");
            data.putExtra("extras1", extras);
            context.startActivity(data);
        }
    }

        public void UpdateList(ArrayList<RecyclerData3> newList) {
            myList = new ArrayList<>();
            myList.addAll(newList);
            notifyDataSetChanged();
        }
}


