package com.ujjawalayush.example.managemyyatra;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class AddFriends extends AppCompatActivity implements SearchView.OnQueryTextListener {
    RecyclerView recyclerView;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    android.support.v7.widget.SearchView searchView;
    DatabaseReference databaseReference,reference;
    private RecyclerAdapter3 mAdapter,gAdapter;
    String m,n;
    ArrayList<RecyclerData3> newList;
    ArrayList<RecyclerData3> myList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfriends);
        toolbar=(Toolbar)findViewById(R.id.toolbar3);
        toolbar.setTitle("Find friends");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView1);
        Resources res = getResources();
        Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.myimage1, null);
        setSupportActionBar(toolbar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mAdapter=new RecyclerAdapter3(myList);
        myList.clear();

        recyclerView.setAdapter(mAdapter);
        firebaseAuth=FirebaseAuth.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            recyclerView.setBackground(drawable);
        }
        recyclerView.setLayoutManager(linearLayoutManager);
        Intent data=getIntent();
        m=data.getBundleExtra("extras").getString("m");
        n=data.getBundleExtra("extras").getString("trip");
        a();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void a() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newList = new ArrayList<>();newList.clear();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Iterator iterator = dataSnapshot.getChildren().iterator();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final String k = snapshot.getKey();
                    if (k.equals(user.getUid()) == false) {
                        reference = FirebaseDatabase.getInstance().getReference().child("Users");
                        reference.child(k).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Iterator iterator1 = dataSnapshot.getChildren().iterator();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    RecyclerData3 mLog;
                                    gAdapter = new RecyclerAdapter3(newList);
                                    if (dataSnapshot1.getKey().equals("Username")) {

                                        mLog = new RecyclerData3();
                                        mLog.setGroup(dataSnapshot1.getValue().toString());
                                        if(n.equals("")){
                                        mLog.setTrip(m);}
                                        else{
                                            mLog.setTrip(n);
                                        }
                                        mLog.setFriend(reference.child(k).getKey());
                                        newList.add(mLog);
                                        gAdapter.notifyData(newList);
                                    }
                                }
                                myList.clear();
                                myList.addAll(newList);
                                mAdapter.notifyData(myList);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.addfriendstoolbar,menu);
        MenuItem menuItem = menu.findItem(R.id.search1);
        searchView = (android.support.v7.widget.SearchView)menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String t = s.toLowerCase();
        ArrayList<RecyclerData3> newList = new ArrayList<>();
        for(RecyclerData3 n : myList)
        {
            if(n.getGroup().toLowerCase().contains(t)) {
                newList.add(n);
            }
        }
        mAdapter.UpdateList(newList);
        return true;
    }

}
