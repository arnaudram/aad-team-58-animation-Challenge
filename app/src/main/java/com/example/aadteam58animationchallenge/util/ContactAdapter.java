package com.example.aadteam58animationchallenge.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aadteam58animationchallenge.AddContact;
import com.example.aadteam58animationchallenge.R;
import com.example.aadteam58animationchallenge.Request;
import com.example.aadteam58animationchallenge.model.Contact;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private static final String TAG = "ContactAdapter";
     private Context context;

   private List<Contact> contacts;
   public ContactAdapter(final Context context){
       this.context= context;
       contacts= new ArrayList<Contact>();
       FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
       DatabaseReference databaseReference= firebaseDatabase.getReference().child("contacts");

       ChildEventListener childEventListener= new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

              Contact contact =dataSnapshot.getValue(Contact.class);
               Log.d(TAG, "onChildAdded: "+contact.getName());
               contact.setId(dataSnapshot.getKey());
              contacts.add(contact);

              notifyItemInserted(contacts.size()-1);
           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {


           }

           @Override
           public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       };
       databaseReference.addChildEventListener(childEventListener);
   }





    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View view= LayoutInflater.from(parent.getContext())
               .inflate(R.layout.item_contact_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.tvContactPhone.setText(contacts.get(position).getPhone());
            holder.tvContactName.setText(contacts.get(position).getName());
        Picasso.get().load(contacts.get(position).getImage())
               .placeholder(R.drawable.ic_launcher_background)
                .resize(80,80)
                .centerCrop()
                .into(holder.circleImageViewContactProfile);

          // holder.circleImageViewContactProfile.setImageURI(Uri.parse(contacts.get(position).getImage()));

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tvContactName;
        private final TextView tvContactPhone;
        private final CircleImageView circleImageViewContactProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvContactName = (TextView)itemView.findViewById(R.id.textView_name);
            tvContactPhone = (TextView)itemView.findViewById(R.id.textView_phone);
            circleImageViewContactProfile = (CircleImageView)itemView.findViewById(R.id.circleimageProfile_item);
        }

        @Override
        public void onClick(View view) {
            int position= getAdapterPosition();
            Intent intent = new Intent(context.getApplicationContext(), Request.class);
            Contact contact= contacts.get(position);
            intent.putExtra("contact",contact);
            context.startActivity(intent);
        }
    }
}
