package com.ujjawalayush.example.managemyyatra;

import  android.app.ProgressDialog;
import android.arch.core.executor.TaskExecutor;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Verify extends AppCompatActivity {
    EditText editText;
    ProgressDialog progressDialog;
    String b,verificationId,user_id;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify);
        editText=(EditText)findViewById(R.id.editText6);
        progressDialog=new ProgressDialog(this);
        Intent data= getIntent();
        Bundle extras=data.getBundleExtra("extras");
        b=extras.getString("b");
        user_id=extras.getString("c");
        sendVerificationCode(b);
        mAuth=FirebaseAuth.getInstance();
    }
    private void verifyCode(String code)
    {
        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(verificationId,code);
        signInWithCredential(credential);
    }
    private void signInWithCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                try {
                if (task.isSuccessful()) {
                    Toast.makeText(Verify.this, "Successfully Signed In", Toast.LENGTH_SHORT).show();
                    databaseReference=FirebaseDatabase.getInstance().getReference();
                    FirebaseUser user=mAuth.getCurrentUser();
                    databaseReference.child("Users").child(user.getUid()).child("user_id").setValue(user.getUid());
                    databaseReference.child("Users").child(user.getUid()).child("Username").setValue(user_id);
                    databaseReference.child("Users").child(user.getUid()).child("Number").setValue(b);
                    Intent data1 = new Intent(Verify.this, MainPage.class);
                    data1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(data1);
                }
                else{
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(Verify.this, "This Mobile No. has already been registered", Toast.LENGTH_LONG).show();
                            return;
                        }
                        else {
                            Toast.makeText(Verify.this, "Wrong Code", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                catch(NullPointerException e){
                    Toast.makeText(Verify.this, "Wrong Code", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }
    public void onClick4(View v){
        String code=editText.getText().toString();
        if(code.length()<6)
        {
            editText.setError("Wrong code");
            editText.requestFocus();
            return;
        }
        verifyCode(code);
    }
    private void sendVerificationCode(String b)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(b,60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mCallback);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId=s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code=phoneAuthCredential.getSmsCode();
            if(code!=null)
            {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Verify.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };
}
