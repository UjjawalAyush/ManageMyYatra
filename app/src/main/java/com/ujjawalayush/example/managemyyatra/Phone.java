package com.ujjawalayush.example.managemyyatra;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Phone extends AppCompatActivity {
    private EditText editText;
    private FirebaseAuth firebaseAuth;
    String b, a, c;
    ProgressDialog progressDialog;
    Query NumberQuery, UsernameQuery;
    EditText editText1;
    Button b1;
    String mPhoneNumber;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone);
        getIntent();
        editText =(EditText)findViewById(R.id.editText5);
        firebaseAuth=FirebaseAuth.getInstance();
        editText1 =(EditText)findViewById(R.id.txt);
        progressDialog = new ProgressDialog(this);
    }

    public boolean isNe() {
        try {
            NetworkInfo networkInfo = null;
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                networkInfo = connectivityManager.getActiveNetworkInfo();
            }
            return networkInfo != null && networkInfo.isConnected();
        } catch (NullPointerException e) {
            return false;
        }
    }
    public void onClick5(View v) {
        a = editText.getText().toString().trim();
        c=editText1.getText().toString().trim();
        if (a.length() != 10) {
            editText.setError("Please enter a valid mobile no.");
            editText.requestFocus();
            return;
        }
        if(c.equals(""))
        {
            editText1.setError("Please enter a Username");
            editText1.requestFocus();
            return;
        }
        progressDialog.setMessage("Logging in!Please wait...");
        progressDialog.show();
        if (isNe() == false) {
            Toast.makeText(Phone.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
        b = "+91" + a;
        NumberQuery = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("Number").equalTo(b);
        NumberQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()>0) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(Phone.this,R.style.Theme_AppCompat_DayNight_Dialog);
                    builder.setTitle("Do you want to login to the existing account?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent data=new Intent(Phone.this,Verify.class);
                            Bundle extras=new Bundle();
                            extras.putString("b",b);
                            progressDialog.dismiss();

                            data.putExtra("extras",extras);
                            startActivity(data);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editText.setError("Choose another mobile no.");
                            editText.requestFocus();
                            progressDialog.dismiss();
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
                else {
                    UsernameQuery =FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("Username").equalTo(c);
                    UsernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() > 0) {
                                editText1.setError("Choose another Username");
                                editText1.requestFocus();
                                progressDialog.dismiss();
                            }
                            else{
                                Intent data=new Intent(Phone.this,Verify.class);
                                Bundle extras=new Bundle();
                                extras.putString("b",b);
                                extras.putString("c",c);
                                data.putExtra("extras",extras);
                                startActivity(data);
                                progressDialog.dismiss();
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
}
