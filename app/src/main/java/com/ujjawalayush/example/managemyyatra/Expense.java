package com.ujjawalayush.example.managemyyatra;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class Expense extends AppCompatActivity {
    EditText editText,editText1,Text1;
    Button b1,b2;
    Toolbar toolbar;
    TextView textView;
    String trip,uid;
    int x=1;
    String horse;
    String category,comment,money,image;
    Uri uri;
    ImageView imageView;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense);
        toolbar = (Toolbar) findViewById(R.id.toolbar10);
        toolbar.setTitle("Add Expense");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));

        setSupportActionBar(toolbar);
        Intent data=getIntent();
        trip=data.getBundleExtra("extras").getString("trip");
        uid=data.getBundleExtra("extras").getString("uid");
        editText=(EditText)findViewById(R.id.editText12);
        editText1=(EditText)findViewById(R.id.editText13);

        imageView=(ImageView) findViewById(R.id.imageView);
        Text1=(EditText)findViewById(R.id.editText14);
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference().child(trip);
        progressDialog=new ProgressDialog(this);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void choose(View v){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,x);
    }
    public String getFileExtension(Uri uri1){
        ContentResolver cR=getContentResolver();
        MimeTypeMap mine=MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(cR.getType(uri1));
    }
    public void onClick21(View v){
        category=editText.getText().toString().trim();
        comment=editText1.getText().toString().trim();
        money=Text1.getText().toString().trim();
        if(category.equals(""))
        {
            editText.setError("Please add Category");
            editText.requestFocus();
            return;
        }
        if(comment.equals(""))
        {
            editText1.setError("Please add Commnent");
            editText1.requestFocus();
            return;
        }
        if(Text1.equals(""))
        {
            Text1.setError("Please add the amount of money spent");
            Text1.requestFocus();
            return;
        }
        if(uri==null){
            Toast.makeText(Expense.this,"Please add receipt/bill",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Uploading...Please Wait");
        progressDialog.show();
        storageReference=FirebaseStorage.getInstance().getReference().child(trip).child(uid).child("receipts").child(System.currentTimeMillis()+"."+getFileExtension(uri));
        final String axe=getFileExtension(uri);
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        databaseReference.child("expenses").child(uid).child(comment).child("Category").setValue(category);
                        databaseReference.child("expenses").child(uid).child(comment).child("Money").setValue(money);
                        databaseReference.child("expenses").child(uid).child(comment).child("Image").setValue(uri.toString());
                        databaseReference.child("expenses").child(uid).child(comment).child("Name").setValue(Long.toString(System.currentTimeMillis())+"."+axe);
                        progressDialog.dismiss();
                        Intent bata=new Intent();
                        bata.putExtra("trip",trip);
                        setResult(RESULT_OK,bata);
                        finish();
                    }

                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Expense.this,"Uploading Failure",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==x&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            uri=data.getData();
            Picasso.get().load(uri).into(imageView);
        }
    }

}
