package com.ujjawalayush.example.managemyyatra;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class Pictures extends AppCompatActivity {
    Toolbar toolbar;
    float x=0;
    String m, username,trip,e,r,t,uid;
    android.support.v7.widget.SearchView searchView;
    private RecyclerView recyclerView;
    TextView textView;
    RecyclerAdapter9 mAdapter, gAdapter;
    ArrayList<RecyclerData9> myList = new ArrayList<>();
    ArrayList<RecyclerData9> newList;
    int b = 0;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference, databaseReference1, dr,alpha;
    EditText editText, editText1;
    String category, des;
    RecyclerData9 recyclerData9;
    FirebaseUser user;
    StorageReference storageReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pictures);
        Intent data=getIntent();
        if(data.getBundleExtra("extras").getString("Class").equals("A")){
        trip=data.getBundleExtra("extras").getString("trip");}
        else if(data.getBundleExtra("extras").getString("Class").equals("B")){
            trip=data.getBundleExtra("extras").getString("trip");}
        toolbar = (Toolbar) findViewById(R.id.toolbar19);
        toolbar.setTitle("View Pictures");
        toolbar.setSubtitle("Click to download");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView19);
        recyclerView.setLayoutManager(linearLayoutManager);
        firebaseAuth = FirebaseAuth.getInstance();
        mAdapter = new RecyclerAdapter9(myList);
        recyclerView.setAdapter(mAdapter);
        clear();
        mAdapter.notifyData(myList);
        recycler();
    }
    public void Restart(){this.recreate();}
    public void recycler(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(trip).child("pictures").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myList.clear();
                newList = new ArrayList<>();newList.clear();
                gAdapter = new RecyclerAdapter9(newList);
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    final String comment = dataSnapshot1.getKey();
                    databaseReference1 = FirebaseDatabase.getInstance().getReference().child(trip).child("pictures");
                    databaseReference1.child(comment).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if ((dataSnapshot.hasChild("Date")) && (dataSnapshot.hasChild("Time")) && (dataSnapshot.hasChild("Focus"))) {
                                final String Comment=dataSnapshot.child("Comment").getValue().toString();
                                final String Date=dataSnapshot.child("Date").getValue().toString();
                                final String Time=dataSnapshot.child("Time").getValue().toString();
                                final String Focus=dataSnapshot.child("Focus").getValue().toString();
                                final String Image=dataSnapshot.child("Image").getValue().toString();

                                alpha=FirebaseDatabase.getInstance().getReference().child("Users").child(dataSnapshot.child("Username").getValue().toString());
                                alpha.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        RecyclerData9 mLog = new RecyclerData9();
                                        mLog.setComment(Comment);
                                        mLog.setDate(Date);
                                        mLog.setImage(Image);
                                        mLog.setTime(Time);
                                        mLog.setName(dataSnapshot.child("Username").getValue().toString());
                                        mLog.setFocus(Focus);
                                        myList.add(mLog);
                                        mAdapter.notifyData(myList);
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.picturestoolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.photo){
            Intent data1=new Intent(Pictures.this,Add.class);
            data1.putExtra("trip",trip);
            startActivityForResult(data1,8);
        }
        return true;

    }
    public void clear() {
        int size = myList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                myList.remove(0);
            }

            mAdapter.notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==8&&resultCode==RESULT_OK){
            clear();
            mAdapter.notifyData(myList);
            Restart();
        }
    }
}
