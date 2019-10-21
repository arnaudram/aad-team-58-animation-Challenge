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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class Request extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Request";
    public static final int REQUEST_CODE_PROFILE1=100;
    private EditText contactName;
    private EditText contactEmail;
    private EditText contactPhone;
    private Button buttonEdit;
    private Button buttonDelete;
    private CircleImageView circleProfile;
    private ImageButton imageButton;
    private Contact contact1;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        contactName = (EditText) findViewById(R.id.edit_name);
        contactEmail = (EditText) findViewById(R.id.edit_email);
        contactPhone = (EditText) findViewById(R.id.edit_phone);
        buttonEdit = (Button)findViewById(R.id.button_edit);
        buttonDelete = (Button)findViewById(R.id.button_delete);

        circleProfile = (CircleImageView) findViewById(R.id.circleimageProfile);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        buttonDelete.setOnClickListener(this);
        buttonEdit.setOnClickListener(this);
        imageButton.setOnClickListener(this);


        Intent intent= getIntent();
        contact1 = intent.getParcelableExtra("contact");
        contactName.setText(contact1.getName());
        contactEmail.setText(contact1.getEmail());
        contactPhone.setText(contact1.getPhone());
        Picasso.get().load(contact1.getImage()).into(circleProfile);

    }





    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case(R.id.button_edit):

                updateValue();
               break;


            case(R.id.button_delete):
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference= firebaseDatabase.getReference().child("contacts");
                databaseReference.child(contact1.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Request.this, "Item removed", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
                   break;
            case   (R.id.imageButton):
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_PROFILE1);
                break;

        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PROFILE1 && resultCode == RESULT_OK) {
            uri = data.getData();
            Picasso.get().load(uri).fit().into(circleProfile);
            saveImage(uri);
        }

    }
    private void updateValue() {
        if (contactName.getText().toString().isEmpty() || contactPhone.getText().toString().isEmpty()) {
            showAlert();
        } else{
            createNewContact();
        }
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


    private void createNewContact() {



        contact1.setName(contactName.getText().toString());
        contact1.setPhone(contactPhone.getText().toString());
        contact1.setEmail(contactEmail.getText().toString());

        saveInDatabase();
        finish();
    }

    private void saveInDatabase() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference= firebaseDatabase.getReference().child("contacts");
        databaseReference.child(contact1.getId()).setValue(contact1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Request.this, "Updated", Toast.LENGTH_SHORT).show();
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
                            contact1.setImage(url);


                        }
                    });
                }
            }
        });


        }
}
