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

public class RecyclerAdapter7 extends RecyclerView.Adapter<RecyclerAdapter7.RecyclerItemViewHolder7> {

    private ArrayList<RecyclerData7> myList;
    int mLastPosition = 0, request_code = 1;
    long id2;
    String m, n, friend;
    FirebaseAuth firebaseAuth;
    Context context;
    DatabaseReference databaseReference;
    FirebaseUser user;
    public RecyclerAdapter7(ArrayList<RecyclerData7> myList) {
        this.myList = myList;
    }

    public RecyclerItemViewHolder7 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row7, parent, false);
        RecyclerItemViewHolder7 holder = new RecyclerItemViewHolder7(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerItemViewHolder7 holder, final int position) {
        Log.d("onBindViewHolder ", myList.size() + "");
        holder.etCategoryTextView.setText("Category " + myList.get(position).getCategory());
        holder.etCommentTextView.setText("Description:" + myList.get(position).getComment());
        holder.etDateTextView.setText("Date of visit: " + myList.get(position).getDate());
        holder.etNameTextView.setText("Added by: " + myList.get(position).getName());

        mLastPosition = position;
    }

    @Override
    public int getItemCount() {
        return (null != myList ? myList.size() : 0);
    }
    public void notifyData1(ArrayList<RecyclerData7> myList){
        this.myList = myList;
        notifyItemRangeRemoved(0,myList.size());
    }
    public void notifyData(ArrayList<RecyclerData7> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.myList = myList;
        notifyDataSetChanged();
    }

    public class RecyclerItemViewHolder7 extends RecyclerView.ViewHolder{
        private final TextView etCategoryTextView, etCommentTextView, etDateTextView,etNameTextView;
        LinearLayout mainLayout;
        public String title, description, date;

        public RecyclerItemViewHolder7(View parent) {
            super(parent);
            etCategoryTextView = (TextView) parent.findViewById(R.id.txtCategory7);
            etCommentTextView = (TextView) parent.findViewById(R.id.txtComment7);
            etDateTextView = (TextView) parent.findViewById(R.id.txtDate7);
            etNameTextView = (TextView) parent.findViewById(R.id.txtName7);

            mainLayout = (LinearLayout) parent.findViewById(R.id.mainLayout7);
        }
    }
}


