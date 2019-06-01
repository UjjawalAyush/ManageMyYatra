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

public class RecyclerAdapter6 extends RecyclerView.Adapter<RecyclerAdapter6.RecyclerItemViewHolder6> {

    private ArrayList<RecyclerData6> myList;
    int mLastPosition = 0, request_code = 1;
    long id2;
    String m, n, friend;
    FirebaseAuth firebaseAuth;
    Context context;
    DatabaseReference databaseReference;
    FirebaseUser user;
    public RecyclerAdapter6(ArrayList<RecyclerData6> myList) {
        this.myList = myList;
    }

    public RecyclerItemViewHolder6 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row6, parent, false);
        RecyclerItemViewHolder6 holder = new RecyclerItemViewHolder6(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerItemViewHolder6 holder, final int position) {
        Log.d("onBindViewHolder ", myList.size() + "");
        holder.etTitleTextView.setText("Category " + myList.get(position).getCategory());
        holder.etFriendTextView.setText("Comment:" + myList.get(position).getComment());
        holder.etTripTextView.setText("Money Spent: " + myList.get(position).getMoney());
        Picasso.get().load(Uri.parse(myList.get(position).getImage())).into(holder.imageView);
        mLastPosition = position;
    }

    @Override
    public int getItemCount() {
        return (null != myList ? myList.size() : 0);
    }
    public void notifyData1(ArrayList<RecyclerData6> myList){
        this.myList = myList;
        notifyItemRangeRemoved(0,myList.size());
    }
    public void notifyData(ArrayList<RecyclerData6> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.myList = myList;
        notifyDataSetChanged();
    }

    public class RecyclerItemViewHolder6 extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView etTitleTextView,etFriendTextView,etTripTextView;
        LinearLayout mainLayout;
        public String title, description, date;
        private final ImageView imageView;

        public RecyclerItemViewHolder6(View parent) {
            super(parent);
            etTitleTextView = (TextView) parent.findViewById(R.id.txtTitle6);
            etTripTextView = (TextView) parent.findViewById(R.id.txtTrip6);
            etFriendTextView = (TextView) parent.findViewById(R.id.txtFriend6);
            imageView=(ImageView)parent.findViewById(R.id.imageView3);
            mainLayout = (LinearLayout) parent.findViewById(R.id.mainLayout6);
            parent.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        context=v.getContext();
        int i=getAdapterPosition();
         DownloadTask downloadTask =new DownloadTask();
        downloadTask.execute(myList.get(i).getImage());
        downloadTask.getName(myList.get(i).getName());
        }
    }
    class DownloadTask extends AsyncTask<String, Integer, String> {
        String e;
        ProgressDialog progressDialog;
        public void getName(String d){
            e=d;
        }

        @Override
        protected void onPreExecute() {
            progressDialog=new ProgressDialog(context);
            progressDialog.setTitle("Downloading...Please Wait");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String path=params[0];
            int file;
            try{
            URL url=new URL(path);
            URLConnection urlConnection=url.openConnection();
            urlConnection.connect();
            file=urlConnection.getContentLength();
                File new_folder=new File(Environment.getExternalStorageDirectory().toString()+"/ManageMyTrip");
                if(!new_folder.exists()){
                    new_folder.mkdirs();
                }
                File input_file=new File(new_folder,e);
                InputStream inputStream=new BufferedInputStream(url.openStream(),8192);
                byte[] data=new byte[1024];
                int total=0;
                int count=0;
                OutputStream outputStream=new FileOutputStream(input_file);
                while((count=inputStream.read(data))!=-1){
                    total += count;
                    outputStream.write(data,0,count);
                    int progress=(int)total*100/file;
                    publishProgress(progress);
                }
                inputStream.close();
                outputStream.close();
            }
            catch(MalformedURLException e){
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Downloading Completed";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String aVoid) {
            progressDialog.dismiss();
            Toast.makeText(context,aVoid,Toast.LENGTH_SHORT).show();
        }
    }
}


