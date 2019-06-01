package com.ujjawalayush.example.managemyyatra;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;


public class Reactions extends AppCompatActivity {
    Toolbar toolbar;
    String m, trip = "", e, t, category, description, username;
    android.support.v7.widget.SearchView searchView;
    RecyclerView recyclerView;
    RecyclerAdapter5 mAdapter, gAdapter;
    ArrayList<RecyclerData5> myList = new ArrayList<>();
    ArrayList<RecyclerData5> newList;
    int b = 0;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference, databaseReference1, dr, alpha,r;
    RecyclerData5 recyclerData5;
    RatingBar ratingBar;
    EditText editText;
    float y = 0, z = 0;
    Button button;
    float n = 0;
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reactions);
        Intent data = getIntent();
        username = data.getBundleExtra("extras").getString("username");
        description = data.getBundleExtra("extras").getString("description");
        category = data.getBundleExtra("extras").getString("category");
        trip = data.getBundleExtra("extras").getString("trip");
        toolbar = (Toolbar) findViewById(R.id.toolbar9);
        button = (Button) findViewById(R.id.button6);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        DatabaseReference monkey=FirebaseDatabase.getInstance().getReference().child(trip).child("members");
        monkey.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String f=dataSnapshot.getValue().toString();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        e = editText.getText().toString().trim();
                        if (e.equals("")) {
                            editText.setError("Enter Comment");
                            editText.requestFocus();
                            return;
                        }
                        databaseReference = FirebaseDatabase.getInstance().getReference().child(trip).child("recommendations").child(description).child("rating");
                        databaseReference.child(f).removeValue();
                        databaseReference.child(f).child(e).setValue(ratingBar.getRating());
                        Toast.makeText(Reactions.this, "Idea Successfully Saved/Updated", Toast.LENGTH_SHORT).show();
                        editText.setText("");
                        editText.setHint("Enter Comment");
                        Restart();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        toolbar.setTitle("View Reactions");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Resources res = getResources();
        Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.ima, null);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView9);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            recyclerView.setBackground(drawable);
        }
        recyclerView.setLayoutManager(linearLayoutManager);


        mAdapter = new RecyclerAdapter5(myList);
        recyclerView.setAdapter(mAdapter);
        editText = (EditText) findViewById(R.id.editText11);
        textView = (TextView) findViewById(R.id.textView10);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        a();
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
    public void a() {
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child(trip).child("recommendations").child(description).child("rating");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                z=0;
                y=0;
                newList = new ArrayList<>();
                newList.clear();
                gAdapter = new RecyclerAdapter5(newList);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final String uid = snapshot.getKey();
                    alpha = FirebaseDatabase.getInstance().getReference().child(trip).child("recommendations").child(description).child("rating").child(uid);
                    alpha.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                RecyclerData5 mLog = new RecyclerData5();
                                z=z+Float.parseFloat(dataSnapshot1.getValue().toString());
                                y++;
                                mLog.setTrip(dataSnapshot1.getValue().toString());
                                mLog.setGroup(dataSnapshot1.getKey());
                                mLog.setFriend(uid);
                                mLog.setFlip(trip);
                                newList.add(mLog);
                                gAdapter.notifyData(newList);
                            }
                            n=0;
                            n=z/y;
                            textView.setText("Average Rating : "+n);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reactionstoolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addplaces) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Reactions.this, R.style.Theme_AppCompat_DayNight_Dialog);
            builder.setTitle("Do you want to add this to the recommended places?");
            final EditText ed = new EditText(Reactions.this);
            ed.setHint("Enter the date of visit");
            builder.setView(ed);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final String time = ed.getText().toString();
                    r = FirebaseDatabase.getInstance().getReference().child(trip).child("Places").child(category + description);
                    r.child("Category").setValue(category);
                    r.child("Description").setValue(description);
                    r.child("Time").setValue(time);
                    DatabaseReference monkey = FirebaseDatabase.getInstance().getReference().child(trip).child("members");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    monkey.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String f = dataSnapshot.getValue().toString();
                            r.child("Person").setValue(f);
                            Toast.makeText(Reactions.this, "Successfully added to the Recommended Places Section", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();


        }
        return true;

    }
}
