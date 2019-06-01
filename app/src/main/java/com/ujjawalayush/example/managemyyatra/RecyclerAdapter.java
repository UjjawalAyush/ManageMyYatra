package com.ujjawalayush.example.managemyyatra;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerItemViewHolder> {

        private ArrayList<RecyclerData> myList;
        int mLastPosition=0,request_code=1;
        long id2;
        String m;
        public RecyclerAdapter(ArrayList<RecyclerData> myList) {
            this.myList = myList;
        }
        public RecyclerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
            RecyclerItemViewHolder holder = new RecyclerItemViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder( RecyclerItemViewHolder holder, final int position) {
            Log.d("onBindViewHolder ", myList.size() + "");
            holder.etTitleTextView.setText("Trip Name: "+myList.get(position).getGroup());
            mLastPosition =position;
            m=myList.get(position).getGroup();
        }

        @Override
        public int getItemCount() {
            return (null != myList?myList.size():0);
        }
        public void notifyData(ArrayList<RecyclerData> myList) {
            Log.d("notifyData ", myList.size() + "");
            this.myList = myList;
            notifyDataSetChanged();
        }
        public class RecyclerItemViewHolder extends RecyclerView.ViewHolder {
            private final TextView etTitleTextView;
            private LinearLayout mainLayout;
            public String title,description,date;
            public RecyclerItemViewHolder(final View parent) {
                super(parent);
                etTitleTextView = (TextView) parent.findViewById(R.id.txtTitle);
                mainLayout = (LinearLayout) parent.findViewById(R.id.mainLayout);
                title=etTitleTextView.getText().toString();

                mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i=getAdapterPosition();
                        Context context = v.getContext();
                        Intent data = new Intent(context,Group.class);
                        Bundle extras=new Bundle();
                        extras.putString("m",myList.get(i).getGroup());
                        extras.putString("Class","A");
                        data.putExtra("extras1",extras);
                        context.startActivity(data);
                    }
                });
            }
        }
        public void UpdateList(ArrayList<RecyclerData> newList){
            myList = new ArrayList<>();
            myList.addAll(newList);
            notifyDataSetChanged();
        }
    }

