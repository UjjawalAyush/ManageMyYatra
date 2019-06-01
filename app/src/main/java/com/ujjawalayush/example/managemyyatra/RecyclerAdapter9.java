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

public class RecyclerAdapter9 extends RecyclerView.Adapter<RecyclerAdapter9.RecyclerItemViewHolder9> {

    private ArrayList<RecyclerData9> myList;
    int mLastPosition = 0, request_code = 1;
    long id2;
    String m, n, friend;
    FirebaseAuth firebaseAuth;
    Context context;
    DatabaseReference databaseReference;
    FirebaseUser user;
    public RecyclerAdapter9(ArrayList<RecyclerData9> myList) {
        this.myList = myList;
    }

    public RecyclerItemViewHolder9 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row9, parent, false);
        RecyclerItemViewHolder9 holder = new RecyclerItemViewHolder9(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerItemViewHolder9 holder, final int position) {
        Log.d("onBindViewHolder ", myList.size() + "");
        holder.etTitleTextView.setText(myList.get(position).getDate()+" "+myList.get(position).getTime());
        holder.etFriendTextView.setText("Description:" + myList.get(position).getComment());
        holder.etTripTextView.setText("Uploaded by: " + myList.get(position).getName());
        Picasso.get().load(Uri.parse(myList.get(position).getImage())).into(holder.imageView);
        mLastPosition = position;
    }

    @Override
    public int getItemCount() {
        return (null != myList ? myList.size() : 0);
    }
    public void notifyData1(ArrayList<RecyclerData9> myList){
        this.myList = myList;
        notifyItemRangeRemoved(0,myList.size());
    }
    public void notifyData(ArrayList<RecyclerData9> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.myList = myList;
        notifyDataSetChanged();
    }

    public class RecyclerItemViewHolder9 extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView etTitleTextView,etFriendTextView,etTripTextView;
        LinearLayout mainLayout;
        public String title, description, date;
        private final ImageView imageView;

        public RecyclerItemViewHolder9(View parent) {
            super(parent);
            etTitleTextView = (TextView) parent.findViewById(R.id.time);
            etTripTextView = (TextView) parent.findViewById(R.id.name);
            etFriendTextView = (TextView) parent.findViewById(R.id.comment);
            imageView=(ImageView)parent.findViewById(R.id.imageView5);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            context=v.getContext();
            int i=getAdapterPosition();
            DownloadTask downloadTask =new DownloadTask();
            downloadTask.execute(myList.get(i).getImage());
            downloadTask.getName(myList.get(i).getFocus());
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


