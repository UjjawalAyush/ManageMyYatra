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

import java.util.ArrayList;

public class Places extends AppCompatActivity {
    Toolbar toolbar;
    String m, trip = "", e, t, category, description, username;
    android.support.v7.widget.SearchView searchView;
    RecyclerView recyclerView;
    RecyclerAdapter7 mAdapter, gAdapter;
    ArrayList<RecyclerData7> myList = new ArrayList<>();
    ArrayList<RecyclerData7> newList;
    int b = 0;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference, databaseReference1, dr, alpha,r;
    RecyclerData7 recyclerData7;
    RatingBar ratingBar;
    String uid;
    EditText editText;
    float y = 0, z = 0;
    Button button;
    float n = 0;
    TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places);
        toolbar = (Toolbar) findViewById(R.id.toolbar12);
        toolbar.setTitle("See Places of interest");
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
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView12);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            recyclerView.setBackground(drawable);
        }
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent data=getIntent();
        trip=data.getBundleExtra("extras").getString("trip");
        uid=data.getBundleExtra("extras").getString("uid");
        firebaseAuth=FirebaseAuth.getInstance();
        mAdapter = new RecyclerAdapter7(myList);
        recyclerView.setAdapter(mAdapter);
        a();
    }
    public void a(){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(trip).child("Places");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myList.clear();
                newList = new ArrayList<>();newList.clear();
                gAdapter = new RecyclerAdapter7(newList);
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    final String m=dataSnapshot1.getKey();
                    databaseReference1=FirebaseDatabase.getInstance().getReference().child(trip).child("Places").child(m);
                    databaseReference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if ((dataSnapshot.hasChild("Category")) && (dataSnapshot.hasChild("Description")) && (dataSnapshot.hasChild("Time"))&&(dataSnapshot.hasChild("Person"))) {

                                RecyclerData7 mLog=new RecyclerData7();
                                mLog.setCategory(dataSnapshot.child("Category").getValue().toString());
                                mLog.setComment(dataSnapshot.child("Description").getValue().toString());
                                if(dataSnapshot.child("Time").getValue().toString().equals(""))
                                {
                                    mLog.setDate("Date not entered");
                                }
                                mLog.setDate(dataSnapshot.child("Time").getValue().toString());
                                mLog.setName(dataSnapshot.child("Person").getValue().toString());
                                myList.add(mLog);
                                mAdapter.notifyData(myList);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.placestoolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.addplaces1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Places.this, R.style.Theme_AppCompat_DayNight_Dialog);
            builder.setTitle("Add a recommended Place");
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            final EditText ed = new EditText(Places.this);
            ed.setHint("Enter Category");
            layout.addView(ed);
            final EditText ed1 = new EditText(Places.this);
            ed1.setHint("Enter Description");
            layout.addView(ed1);
            final EditText ed2 = new EditText(Places.this);
            ed2.setHint("Enter Date of visit");
            layout.addView(ed2);
            builder.setView(layout);
            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String e = ed.getText().toString();
                    String f = ed1.getText().toString();
                    String g = ed2.getText().toString();

                    if (e.equals("")) {
                        ed.setError("Please enter a Category");
                        ed.requestFocus();
                        return;
                    } else {
                        if (f.equals("")) {
                            ed1.setError("Please give the description");
                            ed1.requestFocus();
                            return;
                        } else {
                            alpha = FirebaseDatabase.getInstance().getReference().child(trip).child("Places").child(e + f);
                            alpha.child("Category").setValue(e);
                            alpha.child("Description").setValue(f);
                            alpha.child("Time").setValue(g);
                            DatabaseReference monkey = FirebaseDatabase.getInstance().getReference().child(trip).child("members");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            monkey.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    final String f = dataSnapshot.getValue().toString();
                                    alpha.child("Person").setValue(f);
                                    Toast.makeText(Places.this, "Successfully added", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
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
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
