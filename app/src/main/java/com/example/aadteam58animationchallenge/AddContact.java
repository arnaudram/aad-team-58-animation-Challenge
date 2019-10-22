package com.example.aadteam58animationchallenge;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aadteam58animationchallenge.model.Contact;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddContact extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AddContact";
    public static final int REQUEST_CODE_PROFILE = 55;


    private CircleImageView circleProfile;
    private ImageButton imageButton;
    private EditText contactName;
    private EditText contactEmail;
    private EditText contactPhone;

    private Uri uri;
    private Contact contact;
    private Animation animationSlideDown;
    private TextView demo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        circleProfile = (CircleImageView) findViewById(R.id.circleimageProfile);
        imageButton = (ImageButton) findViewById(R.id.imageButton);

        contactName = (EditText) findViewById(R.id.edit_name);
        contactEmail = (EditText) findViewById(R.id.edit_email);
        contactPhone = (EditText) findViewById(R.id.edit_phone);
        animationSlideDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down);


        demo = (TextView)findViewById(R.id.textView_demo);

        contact = new Contact();
        contactName.setOnClickListener(this);
        contactPhone.setOnClickListener(this);
        contactEmail.setOnClickListener(this);
        demo.setOnClickListener(this);


    }

    public void saveContact(View view) {

        if (contactName.getText().toString().isEmpty() || contactPhone.getText().toString().isEmpty()) {
            showAlert();
        } else {
            createNewContact();
        }

    }

    private void createNewContact() {



        contact.setName(contactName.getText().toString());
        contact.setPhone(contactPhone.getText().toString());
        contact.setEmail(contactEmail.getText().toString());

        saveInDatabase();
             finish();
            }

    private void saveInDatabase() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference= firebaseDatabase.getReference().child("contacts");
        databaseReference.push().setValue(contact).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "onComplete: task is sucessfully completed"+""+task.getResult());


                    Toast.makeText(AddContact.this, "SAVED", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }







    private void saveImage(Uri uri) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference imageReference = storageReference.child("images");
        final StorageReference ref = imageReference.child(uri.getLastPathSegment());
        UploadTask uploadTask = ref.putFile(uri);

        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: task is succesfull");
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            contact.setImage(url);


                        }
                    });
                }
            }
        });


    }

    private void showAlert() {
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
        alertdialog.setMessage("The name and telephone are required. Try again")
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                })
                .setCancelable(false);
        AlertDialog alert = alertdialog.create();
        alert.show();
    }

    public void pickImage(View view) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PROFILE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PROFILE && resultCode == RESULT_OK) {
            uri = data.getData();
           Picasso.get().load(uri).fit().into(circleProfile);
           saveImage(uri);
    }

        }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case(R.id.edit_name):
                contactName.startAnimation(animationSlideDown);
                break;
            case(R.id.edit_email):
                contactEmail.startAnimation(animationSlideDown);
                break;
            case(R.id.edit_phone):
                contactPhone.startAnimation(animationSlideDown);
                break;
            case(R.id.textView_demo):
                contactName.startAnimation(animationSlideDown);

                    contactEmail.startAnimation(animationSlideDown);
                contactPhone.startAnimation(animationSlideDown);
                break;

        }
    }
}
