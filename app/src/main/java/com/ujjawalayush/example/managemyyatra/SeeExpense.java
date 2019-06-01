package com.ujjawalayush.example.managemyyatra;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SeeExpense extends AppCompatActivity {
    Toolbar toolbar;
    float x=0;
    String m, username,trip,e,r,t,uid;
    android.support.v7.widget.SearchView searchView;
    private RecyclerView recyclerView;
    TextView textView;
    RecyclerAdapter6 mAdapter, gAdapter;
    ArrayList<RecyclerData6> myList = new ArrayList<>();
    ArrayList<RecyclerData6> newList;
    int b = 0;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference, databaseReference1, dr,alpha;
    EditText editText, editText1;
    String category, des;
    RecyclerData6 recyclerData6;
    StorageReference storageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see);
        Intent data=getIntent();
        username=data.getBundleExtra("extras").getString("username");
        trip=data.getBundleExtra("extras").getString("trip");
        uid=data.getBundleExtra("extras").getString("uid");
        toolbar = (Toolbar) findViewById(R.id.toolbar11);
        toolbar.setTitle("Expense Summary of "+username);
        toolbar.setSubtitle("Click to download");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));

        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView11);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        firebaseAuth = FirebaseAuth.getInstance();
        mAdapter = new RecyclerAdapter6(myList);
        recyclerView.setAdapter(mAdapter);
        storageReference=FirebaseStorage.getInstance().getReference();
        textView=(TextView)findViewById(R.id.textView9);

        oncreate();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void oncreate() {
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child(trip).child("expenses").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myList.clear();
                newList = new ArrayList<>();newList.clear();
                gAdapter = new RecyclerAdapter6(newList);
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                     final String comment = dataSnapshot1.getKey();
                    databaseReference1 = FirebaseDatabase.getInstance().getReference().child(trip).child("expenses").child(uid);
                    databaseReference1.child(comment).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if ((dataSnapshot.hasChild("Category")) && (dataSnapshot.hasChild("Money")) && (dataSnapshot.hasChild("Image"))) {
                                RecyclerData6 mLog = new RecyclerData6();
                                mLog.setCategory(dataSnapshot.child("Category").getValue().toString());
                                mLog.setMoney(dataSnapshot.child("Money").getValue().toString());
                                x=x+Float.parseFloat(dataSnapshot.child("Money").getValue().toString());
                                mLog.setImage(dataSnapshot.child("Image").getValue().toString());
                                mLog.setName(dataSnapshot.child("Name").getValue().toString());
                                mLog.setComment(comment);
                                myList.add(mLog);
                                mAdapter.notifyData(myList);
                            }
                            textView.setText("Total Money Spent: "+Float.toString(x));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
