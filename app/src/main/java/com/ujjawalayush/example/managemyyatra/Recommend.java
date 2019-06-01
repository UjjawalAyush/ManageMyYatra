package com.ujjawalayush.example.managemyyatra;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

public class Recommend extends AppCompatActivity {
    Toolbar toolbar;
    String m, username = "", trip = "",e,r,t;
    android.support.v7.widget.SearchView searchView;
    private RecyclerView recyclerView;
    private RecyclerAdapter4 mAdapter, gAdapter;
    ArrayList<RecyclerData4> myList = new ArrayList<>();
    ArrayList<RecyclerData4> newList;
    int b = 0;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference, databaseReference1, dr,alpha;
    EditText editText, editText1;
    String category, des;
    RecyclerData4 recyclerData4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend);
        Intent data = getIntent();
        trip = data.getStringExtra("trip");
        toolbar = (Toolbar) findViewById(R.id.toolbar6);
        toolbar.setTitle("Recommendations");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));

        setSupportActionBar(toolbar);
        Resources res = getResources();
        Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.ima, null);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView3);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            recyclerView.setBackground(drawable);
        }
        recyclerView.setLayoutManager(linearLayoutManager);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        editText = (EditText) findViewById(R.id.editText9);
        editText1 = (EditText) findViewById(R.id.editText10);
        mAdapter = new RecyclerAdapter4(myList);
        recyclerView.setAdapter(mAdapter);
        myList.clear();
        mAdapter.notifyData(myList);
        a();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recommendtoolbar,menu);
        return true;

    }

    public void a() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child(trip).child("recommendations");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myList.clear();
                newList = new ArrayList<>();newList.clear();
                gAdapter = new RecyclerAdapter4(newList);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                     final String uid =snapshot.getKey();
                    alpha=FirebaseDatabase.getInstance().getReference().child(trip).child("recommendations");
                    alpha.child(uid).child("Category").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                                RecyclerData4 mLog = new RecyclerData4();
                                mLog.setTrip(dataSnapshot1.getValue().toString());
                                mLog.setGroup(dataSnapshot1.getKey());
                                mLog.setFriend(uid);
                                mLog.setFlip(trip);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onClick8(View v) {
        category = editText.getText().toString().trim();
        des = editText1.getText().toString().trim();
        if (category.equals("")) {
            editText.setError("Category not entered");
            editText.requestFocus();
            return;

        }
        if (des.equals("")) {
            editText1.setError("Description not entered");
            editText1.requestFocus();
            return;

        }
        FirebaseUser user = firebaseAuth.getCurrentUser();
        dr = FirebaseDatabase.getInstance().getReference().child(user.getUid());
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser user1=firebaseAuth.getCurrentUser();
                databaseReference = FirebaseDatabase.getInstance().getReference().child(trip).child("recommendations");
                databaseReference.child(des).child("Category").child(category).setValue(dataSnapshot.getValue().toString());
                editText.setText("");
                editText1.setText("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
}
