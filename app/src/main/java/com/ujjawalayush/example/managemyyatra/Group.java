package com.ujjawalayush.example.managemyyatra;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Group extends AppCompatActivity implements SearchView.OnQueryTextListener {
    Toolbar toolbar;
    String m,username="",uid="",trip="",x;
    FirebaseUser user;
    android.support.v7.widget.SearchView searchView;
    private RecyclerView recyclerView;
    private RecyclerAdapter2 mAdapter,gAdapter;
    ArrayList<RecyclerData2> myList = new ArrayList<>();
    ArrayList<RecyclerData2> newList;
    int b=0,requestCode1=1;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference,databaseReference1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group);
        Intent data=getIntent();
        if(data.getBundleExtra("extras1").getString("Class").equals("A")) {
            m = data.getBundleExtra("extras1").getString("m");
        }
        else if(data.getBundleExtra("extras1").getString("Class").equals("B")){
            username = data.getBundleExtra("extras1").getString("username");
            uid = data.getBundleExtra("extras1").getString("uid");
            trip = data.getBundleExtra("extras1").getString("trip");
        }
        toolbar=(Toolbar)findViewById(R.id.toolbar1);
        toolbar.setTitle(m);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        setSupportActionBar(toolbar);

        Resources res = getResources();
        Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.nature2, null);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView2);
        mAdapter=new RecyclerAdapter2(myList);
        recyclerView.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            recyclerView.setBackground(drawable);
        }
        recyclerView.setLayoutManager(linearLayoutManager);
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference();
        a();
    }
    public void remove(){
        if(m.equals("")){
        DatabaseReference zombie=FirebaseDatabase.getInstance().getReference().child(trip);
        zombie.child("members").child(user.getUid()).removeValue();
        DatabaseReference zombie1=FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Trip");
        zombie1.child(trip).removeValue();
        onBackPressed();}
        else{
                DatabaseReference zombie=FirebaseDatabase.getInstance().getReference().child(m);
                zombie.child("members").child(user.getUid()).removeValue();
                DatabaseReference zombie1=FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Trip");
                zombie1.child(m).removeValue();
                onBackPressed();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void a(){
        user = firebaseAuth.getCurrentUser();

        if((username.equals("")==false)||(trip.equals("")==false)) {
            databaseReference.child(trip).child("members").child(uid).setValue(username);
            databaseReference.child("Users").child(uid).child("Trip").child(trip).setValue("");
        }
        databaseReference1=FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    if (s.getKey().equals("Username")) {

                        if (trip.equals("")) {
                            databaseReference.child(m).child("members").child(user.getUid()).setValue(s.getValue().toString());
                            x=s.getValue().toString();
                            databaseReference.child(user.getUid()).setValue(x);
                            newList = new ArrayList<>();
                            newList.clear();
                            gAdapter = new RecyclerAdapter2(newList);
                            databaseReference.child(m).child("members").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        RecyclerData2 mLog = new RecyclerData2();
                                        mLog.setGroup(dataSnapshot1.getValue().toString());
                                        mLog.setTrip(m);
                                        mLog.setFriend(dataSnapshot1.getKey());
                                        newList.add(mLog);
                                        gAdapter.notifyData(newList);
                                    }
                                    myList.addAll(newList);
                                    mAdapter.notifyData(myList);
                                    RecyclerData2 recyclerData2 = new RecyclerData2();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {
                            if (s.getKey().equals("Username")) {
                                databaseReference.child(trip).child("members").child(user.getUid()).setValue(s.getValue().toString());
                            }
                            myList.clear();
                            newList = new ArrayList<>();
                            newList.clear();
                            gAdapter = new RecyclerAdapter2(newList);
                            databaseReference.child(trip).child("members").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        RecyclerData2 mLog = new RecyclerData2();
                                        mLog.setGroup(dataSnapshot1.getValue().toString());
                                        mLog.setTrip(trip);
                                        mLog.setFriend(dataSnapshot1.getKey());
                                        newList.add(mLog);
                                        gAdapter.notifyData(newList);
                                    }
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.groupbarmenu,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        searchView = (android.support.v7.widget.SearchView)menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.addFriends:
                Intent Data=new Intent(Group.this,AddFriends.class);
                Bundle extras=new Bundle();
                extras.putString("m",m);
                extras.putString("trip",trip);
                Data.putExtra("extras",extras);
                startActivity(Data);
                break;
            case R.id.pictures:
                Intent data=new Intent(Group.this,Pictures.class);
                Bundle extras8=new Bundle();
                if(trip.equals(""))
                {
                    extras8.putString("trip",m);
                }
                else{
                    extras8.putString("trip",trip);
                }
                extras8.putString("Class","B");
                data.putExtra("extras",extras8);
                startActivity(data);

                break;
            case R.id.recommend:
                Intent data8=new Intent(Group.this,Recommend.class);
                if(trip.equals(""))
                {
                    data8.putExtra("trip",m);
                }
                else{
                    data8.putExtra("trip",trip);
                }
                startActivity(data8);
                break;
            case R.id.expense:
                Intent data2=new Intent(Group.this,Expense.class);
                Bundle extras2=new Bundle();
                if(trip.equals(""))
                {
                    extras2.putString("trip",m);
                }
                else{
                    extras2.putString("trip",trip);
                }
                extras2.putString("uid",user.getUid());
                data2.putExtra("extras",extras2);
                startActivityForResult(data2,requestCode1);
                break;
            case R.id.places:
                Intent data3=new Intent(Group.this,Places.class);
                Bundle extras3=new Bundle();
                if(trip.equals(""))
                {
                    extras3.putString("trip",m);
                }
                else{
                    extras3.putString("trip",trip);
                }
                extras3.putString("uid",user.getUid());
                data3.putExtra("extras",extras3);
                startActivity(data3);
                break;
            case R.id.groupChat:
                Intent data4=new Intent(Group.this,Chat.class);
                Bundle extras4=new Bundle();
                if(trip.equals(""))
                {
                    extras4.putString("trip",m);
                }
                else{
                    extras4.putString("trip",trip);
                }
                extras4.putString("uid",user.getUid());
                data4.putExtra("extras",extras4);
                startActivity(data4);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String t = s.toLowerCase();
        ArrayList<RecyclerData2> newList = new ArrayList<>();
        for(RecyclerData2 n : myList)
        {
            if(n.getGroup().toLowerCase().contains(t)) {
                newList.add(n);
            }
        }
        mAdapter.UpdateList(newList);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent data=new Intent(Group.this,MainPage.class);
        startActivity(data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK){
            trip=data.getStringExtra("trip");
            Toast.makeText(Group.this,"Data successfully added",Toast.LENGTH_SHORT).show();
        }
    }
}
