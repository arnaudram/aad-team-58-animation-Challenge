package com.example.aadteam58animationchallenge;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.aadteam58animationchallenge.model.Contact;
import com.example.aadteam58animationchallenge.util.ContactAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final int REQUEST_CODE_NEW_CONTACT = 56;
    private ContactAdapter contactAdapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.recycle_contact_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




    }

    @Override
    protected void onStart() {
        super.onStart();
        contactAdapter = new ContactAdapter(this);
        recyclerView.setAdapter(contactAdapter);
    }

    public void movetoAddContact(View view) {
        Intent intent = new Intent(MainActivity.this,AddContact.class);
        startActivityForResult(intent, REQUEST_CODE_NEW_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_NEW_CONTACT && resultCode==RESULT_OK){


            Toast.makeText(this, "Main Activity", Toast.LENGTH_SHORT).show();





        }
    }
}
