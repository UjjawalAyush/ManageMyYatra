package com.ujjawalayush.example.managemyyatra;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static java.security.AccessController.getContext;

public class MainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    DrawerLayout drawerLayout;
    String f;
    private RecyclerView recyclerView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    private RecyclerAdapter mAdapter,gAdapter;
    ArrayList<RecyclerData> myList = new ArrayList<>();
    Button button,b1;
    FirebaseAuth firebaseAuth;
    String user_id;
    String b="";
    NavigationView navigationView;
    DatabaseReference databaseReference,reference;
    android.support.v7.widget.SearchView searchView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
        navigationView=(NavigationView)findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Manage My Yatra");
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        Resources res = getResources();
        Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.myimage, null);
        setSupportActionBar(toolbar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        drawerLayout= (DrawerLayout)findViewById(R.id.drawer_layout);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        mAdapter=new RecyclerAdapter(myList);
        recyclerView.setAdapter(mAdapter);
        Intent data=getIntent();
        user_id=data.getStringExtra("user_id");
        firebaseAuth=FirebaseAuth.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            recyclerView.setBackground(drawable);
        }
        recyclerView.setLayoutManager(linearLayoutManager);
        a();
    }
    public void a(){
        FirebaseUser user=firebaseAuth.getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Trip");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<RecyclerData> newList = new ArrayList<>();
                gAdapter=new RecyclerAdapter(newList);
                Iterator iterator=dataSnapshot.getChildren().iterator();
                while(iterator.hasNext()){
                    RecyclerData mLog=new RecyclerData();
                    mLog.setGroup(((DataSnapshot)iterator.next()).getKey());
                    newList.add(mLog);
                    gAdapter.notifyData(newList);
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



    public void AddGroup(){
        AlertDialog.Builder builder=new AlertDialog.Builder(MainPage.this,R.style.Theme_AppCompat_DayNight_Dialog);
        builder.setTitle("Add Trip");
        final EditText ed= new EditText(MainPage.this);
        ed.setHint("e.g My Trip");
        builder.setView(ed);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ed1=ed.getText().toString();
                if(ed1.equals(""))
                {
                    ed.setError("Please Enter a Trip Name");
                    ed.requestFocus();
                    return;
                }
                else
                {
                    FirebaseUser user=firebaseAuth.getCurrentUser();
                    databaseReference=FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("Users").child(user.getUid()).child("Trip").child(ed1).setValue("");
                    Toast.makeText(MainPage.this,"Trip Successfully Added",Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbarmenu,menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        searchView = (android.support.v7.widget.SearchView)menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        switch(item.getItemId()){
            case R.id.AddGroup:
                AddGroup();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.rateIt:
                break;
            case R.id.faq:
                break;
            case R.id.shareApp:
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent data=new Intent(getApplicationContext(),MainActivity.class);
                data.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(data);
                break;
            case R.id.about:
                Intent m=new Intent(MainPage.this,About.class);
                startActivity(m);
        }
        return true;
    }
    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }
    @Override
    public boolean onQueryTextChange(String s) {
        String t = s.toLowerCase();
        ArrayList<RecyclerData> newList = new ArrayList<>();
        for(RecyclerData n : myList)
        {
            if(n.getGroup().toLowerCase().contains(t)) {
                newList.add(n);
            }
        }
        mAdapter.UpdateList(newList);
        return true;
    }
}
