package com.ujjawalayush.example.managemyyatra;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.provider.ContactsContract;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Chat extends AppCompatActivity {
    Toolbar toolbar;
    String m, trip = "", e, t, category, description, username,message;
    android.support.v7.widget.SearchView searchView;
    RecyclerView recyclerView;
    RecyclerAdapter8 mAdapter, gAdapter;
    ArrayList<RecyclerData8> myList = new ArrayList<>();
    ArrayList<RecyclerData8> newList;
    int b = 0;
    String mass;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference, databaseReference1, dr, alpha,r;
    RecyclerData8 recyclerData8;
    RatingBar ratingBar;
    String uid;
    EditText editText;LinearLayoutManager layoutManager;
    float y = 0, z = 0;
    Button button;
    float n = 0;
    TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        toolbar = (Toolbar) findViewById(R.id.toolbar13);
        toolbar.setTitle("Group Chat");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        setSupportActionBar(toolbar);
        Resources res = getResources();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.places, null);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView13);
        editText=(EditText)findViewById(R.id.editText16);
        Intent data=getIntent();
        layoutManager=new LinearLayoutManager(this);
        trip=data.getBundleExtra("extras").getString("trip");
        uid=data.getBundleExtra("extras").getString("uid");
        firebaseAuth=FirebaseAuth.getInstance();
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new RecyclerAdapter8(myList);
        recyclerView.setAdapter(mAdapter);
        a();
    }
    public void a(){
        alpha=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        alpha.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mass=dataSnapshot.child("Username").getValue().toString();
                databaseReference1=FirebaseDatabase.getInstance().getReference().child(trip).child("Chat");
                databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        newList = new ArrayList<>();newList.clear();
                        gAdapter = new RecyclerAdapter8(newList);
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            final String x=dataSnapshot1.getKey();
                            databaseReference1.child(x).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if ((dataSnapshot.hasChild("Date")) && (dataSnapshot.hasChild("Person")) && (dataSnapshot.hasChild("Time")) && (dataSnapshot.hasChild("Message"))) {
                                        RecyclerData8 mLog=new RecyclerData8();

                                        mLog.setMessage(dataSnapshot.child("Message").getValue().toString());
                                        mLog.setTime(dataSnapshot.child("Time").getValue().toString());
                                        mLog.setDate(dataSnapshot.child("Date").getValue().toString());
                                        mLog.setName(dataSnapshot.child("Person").getValue().toString());
                                        mLog.setUsername(mass);
                                        myList.add(mLog);
                                        mAdapter.notifyData(myList);
                                        recyclerView.scrollToPosition(myList.size() - 1);

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
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void on(View view) {
        message=editText.getText().toString();
        if(message.equals(""))
        {
            editText.setError("Enter a message");
            editText.requestFocus();
            return;
        }
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        Calendar cal = Calendar.getInstance();
        Date date=cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedTime=dateFormat.format(date);
        databaseReference=FirebaseDatabase.getInstance().getReference().child(trip).child("Chat");
        final String timelapse=Long.toString(System.currentTimeMillis());
        databaseReference.child(timelapse).child("Date").setValue(formattedDate);
        databaseReference.child(timelapse).child("Time").setValue(formattedTime);
        databaseReference.child(timelapse).child("Message").setValue(message);
        DatabaseReference monkey = FirebaseDatabase.getInstance().getReference().child(trip).child("members");
        FirebaseUser user = firebaseAuth.getCurrentUser();
        monkey.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String f = dataSnapshot.getValue().toString();
                databaseReference.child(timelapse).child("Person").setValue(f);
                Toast.makeText(Chat.this, "Successfully added", Toast.LENGTH_SHORT).show();
                editText.setText("");
                r=FirebaseDatabase.getInstance().getReference().child(trip).child("Chat").child(timelapse);
                r.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if((dataSnapshot.hasChild("Date"))){
                            RecyclerData8 mLog=new RecyclerData8();
                            mLog.setMessage(dataSnapshot.child("Message").getValue().toString());
                            mLog.setTime(dataSnapshot.child("Time").getValue().toString());
                            mLog.setDate(dataSnapshot.child("Date").getValue().toString());
                            mLog.setName(dataSnapshot.child("Person").getValue().toString());
                            mLog.setUsername(mass);
                            myList.add(mLog);
                            mAdapter.notifyData(myList);
                            recyclerView.scrollToPosition(myList.size() - 1);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void Restart() {

        this.recreate();}
    public void clear() {
        int size = myList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                myList.remove(0);
            }

            mAdapter.notifyItemRangeRemoved(0, size);
        }
    }
}
