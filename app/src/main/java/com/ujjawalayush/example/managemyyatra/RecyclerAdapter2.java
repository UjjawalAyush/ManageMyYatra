package com.ujjawalayush.example.managemyyatra;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerAdapter2.RecyclerItemViewHolder2> {

    private ArrayList<RecyclerData2> myList;
    int mLastPosition = 0, request_code = 1;
    long id2;
    String m, n, friend;
    FirebaseAuth firebaseAuth;
    Context context;
    DatabaseReference databaseReference;
    FirebaseUser user;
    public RecyclerAdapter2(ArrayList<RecyclerData2> myList) {
        this.myList = myList;
    }

    public RecyclerItemViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row2, parent, false);
        RecyclerItemViewHolder2 holder = new RecyclerItemViewHolder2(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerItemViewHolder2 holder, final int position) {
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

    public void notifyData(ArrayList<RecyclerData2> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.myList = myList;
        notifyDataSetChanged();
    }

    public class RecyclerItemViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView etTitleTextView;
        ImageButton b;
        LinearLayout mainLayout,mainLayout2;
        public String title, description, date;

        public RecyclerItemViewHolder2(View parent) {
            super(parent);
            etTitleTextView = (TextView) parent.findViewById(R.id.txtTitle2);
            mainLayout = (LinearLayout) parent.findViewById(R.id.mainLayout2);
            mainLayout2 = (LinearLayout) parent.findViewById(R.id.m);
            b=(ImageButton) parent.findViewById(R.id.b);
            title = etTitleTextView.getText().toString();
            mainLayout.setOnClickListener(this);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    context=v.getContext();
                    DatabaseReference r=FirebaseDatabase.getInstance().getReference().child("Users").child(myList.get(getAdapterPosition()).getFriend());
                    r.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild("Number")){
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:"+dataSnapshot.child("Number").getValue().toString()));
                            context.startActivity(callIntent);}
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
        }

        @Override
        public void onClick(View v) {
            int i=getAdapterPosition();
            Context context1=v.getContext();
            Intent data=new Intent(context1,SeeExpense.class);

            Bundle extras=new Bundle();
            extras.putString("username",myList.get(i).getGroup());
            extras.putString("uid",myList.get(i).getFriend());
            extras.putString("trip",myList.get(i).getTrip());
            data.putExtra("extras",extras);

            context1.startActivity(data);
        }
    }

    public void UpdateList(ArrayList<RecyclerData2> newList) {
        myList = new ArrayList<>();
        myList.addAll(newList);
        notifyDataSetChanged();
    }
}


