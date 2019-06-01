package com.ujjawalayush.example.managemyyatra;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private EditText editText,editText1;
    private Button button,b2;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth =FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(isNe()==false){
            Toast.makeText(MainActivity.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        if(user!=null){
            if(user.getEmail()==null) {
                Toast.makeText(this, "Welcome ", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, "Welcome " + user.getEmail(), Toast.LENGTH_LONG).show();

            }
            Intent data=new Intent(MainActivity.this,MainPage.class);
            startActivity(data);
        }
        setContentView(R.layout.activity_main);
        button=(Button)findViewById(R.id.button);
        editText=(EditText)findViewById(R.id.editText);
        editText1=(EditText)findViewById(R.id.editText2);
        progressDialog = new ProgressDialog(this);
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
    public void onClick(View v){
        String email,password;
        email=editText.getText().toString().trim();
        password=editText1.getText().toString().trim();
        if(email.equals(""))
        {
            editText.setError("E-Mail entry is mandatory");
            editText.requestFocus();
            return;
        }
        if(password.equals(""))
        {
            editText1.setError("Password entry is mandatory");
            editText1.requestFocus();
            return;
        }
        progressDialog.setMessage("Logging in!Please wait...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>(){

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(isNe()==false)
                {
                    Toast.makeText(MainActivity.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if(task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this,"Successfully Logged In",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Intent data = new Intent(MainActivity.this, MainPage.class);
                    FirebaseUser user=firebaseAuth.getCurrentUser();
                    String user_id=user.getUid();
                    data.putExtra("user_id",user_id);
                    startActivity(data);
                }
                else
                {
                        Toast.makeText(MainActivity.this, "Invalid E-Mail Id or Password", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                }
            }
        });
    }
    public void onClick1(View v) {
        Intent data = new Intent(MainActivity.this, Signup.class);
        startActivity(data);
    }
    public void onClick2(View v){
        Intent data=new Intent(MainActivity.this,Phone.class);
        startActivity(data);
    }
}
