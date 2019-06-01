package com.ujjawalayush.example.managemyyatra;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Signup extends AppCompatActivity {
    private EditText editText2,editText3,editText4;
    private Button b2;
    ProgressDialog progressDialog;
    EditText username,number;
    String Username,a;
    FirebaseAuth firebaseAuth;
    String email,password,m;
    String user_id,b;
    DatabaseReference databaseReference;
    Query UsernameQuery,NumberQuery;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        editText2=(EditText)findViewById(R.id.editText3);
        editText3=(EditText)findViewById(R.id.editText4);
        b2=(Button)findViewById(R.id.button2);
        number=(EditText)findViewById(R.id.editText8);
        username=(EditText)findViewById(R.id.editText7);
        progressDialog=new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();
    }
    public boolean isNe(){
        try {
            NetworkInfo networkInfo = null;
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                networkInfo = connectivityManager.getActiveNetworkInfo();
            }
            return networkInfo != null && networkInfo.isConnected();
        }
        catch(NullPointerException e){
            return false;
        }
    }
    public void onClick2(View v){
        email=editText2.getText().toString().trim();
        password=editText3.getText().toString().trim();
        a=number.getText().toString().trim();
        Username=username.getText().toString().trim();
        if(email.equals(""))
        {
            editText2.setError("Email not entered");
            editText2.requestFocus();
            return;

        }
        if(password.equals(""))
        {
            editText3.setError("Password not entered");
            editText3.requestFocus();
            return;

        }
        if(password.length()<6){
            editText3.setError("Minimum Password length required 6");
            editText3.requestFocus();
            return;
        }
        if(Username.equals(""))
        {
            username.setError("Please enter a valid Username");
            username.requestFocus();
            return;
        }
        if(a.length()!=10)
        {
            number.setError("Please enter a valid mobile no.");
            number.requestFocus();
            return;
        }
        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        if (isNe() == false) {
            Toast.makeText(Signup.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
        UsernameQuery=FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("Username").equalTo(Username);
        UsernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>0)
                {
                    username.setError("Choose another Username");
                    username.requestFocus();
                    progressDialog.dismiss();
                }
                else{
                    b = "+91"+a;
                    NumberQuery=FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("Number").equalTo(b);
                    NumberQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getChildrenCount()>0)
                            {
                                number.setError("Choose another mobile no.");
                                number.requestFocus();
                                progressDialog.dismiss();
                            }
                            else{
                                Activity activity=Signup.this;
                                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(activity,new OnCompleteListener<AuthResult>(){
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user=firebaseAuth.getCurrentUser();
                                         user_id=user.getUid();
                                         databaseReference=FirebaseDatabase.getInstance().getReference();
                                         databaseReference.child("Users").child(user_id).child("user_id").setValue(user_id);
                                         databaseReference.child("Users").child(user_id).child("Username").setValue(Username);
                                         databaseReference.child("Users").child(user_id).child("Number").setValue(b);
                                         Toast.makeText(Signup.this,"Successfully Registered", Toast.LENGTH_LONG).show();
                                         progressDialog.dismiss();
                                         Intent data = new Intent(Signup.this, MainPage.class);
                                         startActivity(data);
                                        } else {
                                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                                Toast.makeText(Signup.this, "You have already registered", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            } else {
                                                Toast.makeText(Signup.this, "You didn't get registered", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        }
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
}
