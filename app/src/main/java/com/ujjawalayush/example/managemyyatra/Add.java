package com.ujjawalayush.example.managemyyatra;

import android.app.ProgressDialog;
import android.content.ContentResolver;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Add extends AppCompatActivity {
    EditText editText,editText1,Text1;
    Button b1,b2;
    Toolbar toolbar;
    TextView textView;
    String trip,uid;
    int x=1;
    String horse;
    String category,comment,money,image,timelapse,formattedDate,formattedTime,axe,xd;
    Uri uri;
    ImageView imageView;
    StorageReference storageReference;
    DatabaseReference databaseReference,alpha;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        toolbar = (Toolbar) findViewById(R.id.toolbar20);
        toolbar.setTitle("Upload Photo");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));

        setSupportActionBar(toolbar);
        Intent data=getIntent();
        trip=data.getStringExtra("trip");
        editText=(EditText)findViewById(R.id.editText15);
        imageView=(ImageView)findViewById(R.id.imageView4);
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        databaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        progressDialog=new ProgressDialog(Add.this);
        alpha=FirebaseDatabase.getInstance().getReference().child(trip);


    }
    public void onClick(View v){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,x);
    }
    public void Restart(){this.recreate();}
    public void onClick1(View v) {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);
        Calendar cal = Calendar.getInstance();
        Date date=cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        formattedTime=dateFormat.format(date);
        timelapse=Long.toString(System.currentTimeMillis());
        comment = editText.getText().toString().trim();
        if (comment.equals("")) {
            editText.setError("Please add Description");
            editText.requestFocus();
            return;
        }
        if (uri == null) {
            Toast.makeText(Add.this, "Please add receipt/bill", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Uploading...Please Wait");
        progressDialog.show();
        axe = getFileExtension(uri);


        storageReference = FirebaseStorage.getInstance().getReference().child(trip).child(Long.toString(System.currentTimeMillis()) + "." + getFileExtension(uri));
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri1) {
                        alpha.child("pictures").child(timelapse).child("Comment").setValue(comment);
                        alpha.child("pictures").child(timelapse).child("Date").setValue(formattedDate);
                        alpha.child("pictures").child(timelapse).child("Time").setValue(formattedTime);
                        alpha.child("pictures").child(timelapse).child("Image").setValue(uri1.toString());
                        alpha.child("pictures").child(timelapse).child("Focus").setValue(timelapse+"."+axe);
                        alpha.child("pictures").child(timelapse).child("Username").setValue(user.getUid());
                        Intent data=getIntent();
                        progressDialog.dismiss();
                        Toast.makeText(Add.this,"Picture successfully uploaded",Toast.LENGTH_SHORT).show();

                        setResult(RESULT_OK,data);
                        finish();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Add.this, "Uploading Failure", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public String getFileExtension(Uri uri1){
        ContentResolver cR=getContentResolver();
        MimeTypeMap mine=MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(cR.getType(uri1));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==x&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            uri=data.getData();
            Picasso.get().load(uri).into(imageView);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
