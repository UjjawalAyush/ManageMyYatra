package com.ujjawalayush.example.managemyyatra;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class RecyclerAdapter8 extends RecyclerView.Adapter<RecyclerAdapter8.RecyclerItemViewHolder8> {

    private ArrayList<RecyclerData8> myList;
    int mLastPosition = 0, request_code = 1;
    long id2;
    String m, n, friend;
    FirebaseAuth firebaseAuth;
    Context context;
    DatabaseReference databaseReference;
    FirebaseUser user;
    public RecyclerAdapter8(ArrayList<RecyclerData8> myList) {
        this.myList = myList;
    }

    public RecyclerItemViewHolder8 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row8, parent, false);
        RecyclerItemViewHolder8 holder = new RecyclerItemViewHolder8(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerItemViewHolder8 holder, final int position) {
        Log.d("onBindViewHolder ", myList.size() + "");
        if(myList.get(position).getUsername().equals(myList.get(position).getName())) {
            holder.et11TextView.setVisibility(View.VISIBLE);
            holder.et12TextView.setVisibility(View.VISIBLE);
            holder.et13TextView.setVisibility(View.VISIBLE);

            holder.et11TextView.setText(myList.get(position).getMessage());
            holder.et13TextView.setText(myList.get(position).getDate() + " "+myList.get(position).getTime());
            holder.et12TextView.setText("By:" + myList.get(position).getName());
            holder.et21TextView.setVisibility(View.INVISIBLE);
            holder.et22TextView.setVisibility(View.INVISIBLE);

            holder.et23TextView.setVisibility(View.INVISIBLE);

        }
        else{
            holder.et21TextView.setVisibility(View.VISIBLE);
            holder.et22TextView.setVisibility(View.VISIBLE);

            holder.et23TextView.setVisibility(View.VISIBLE);
            holder.et21TextView.setText(myList.get(position).getMessage());
            holder.et23TextView.setText(myList.get(position).getDate() + " "+myList.get(position).getTime());
            holder.et22TextView.setText("By:" + myList.get(position).getName());
            holder.et11TextView.setVisibility(View.INVISIBLE);
            holder.et12TextView.setVisibility(View.INVISIBLE);
            holder.et13TextView.setVisibility(View.INVISIBLE);
        }

        mLastPosition = position;
    }

    @Override
    public int getItemCount() {
        return (null != myList ? myList.size() : 0);
    }
    public void notifyData1(ArrayList<RecyclerData8> myList){
        this.myList = myList;
        notifyItemRangeRemoved(0,myList.size());
    }
    public void notifyData(ArrayList<RecyclerData8> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.myList = myList;
        notifyDataSetChanged();
    }

    public class RecyclerItemViewHolder8 extends RecyclerView.ViewHolder{
        private final TextView et11TextView, et12TextView, et13TextView,et21TextView,et22TextView,et23TextView;
        public String title, description, date;

        public RecyclerItemViewHolder8(View parent) {
            super(parent);
            et11TextView = (TextView) parent.findViewById(R.id.textView11);
            et12TextView = (TextView) parent.findViewById(R.id.textView16);
            et13TextView = (TextView) parent.findViewById(R.id.textView18);
            et21TextView = (TextView) parent.findViewById(R.id.textView12);
            et22TextView = (TextView) parent.findViewById(R.id.textView17);
            et23TextView = (TextView) parent.findViewById(R.id.textView19);

        }
    }
}


