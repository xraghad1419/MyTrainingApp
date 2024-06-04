package com.example.mytraining;

import android.app.AlertDialog;
import android.app.admin.DelegatedAdminReceiver;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Adapter_admin_users extends RecyclerView.Adapter<Adapter_admin_users.MyViewHolder> {

    ArrayList<Account>list;
    private RecycleViewClickListener listener;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Accounts");
    DatabaseReference DB_A = FirebaseDatabase.getInstance().getReference().child("Applicants");
    DatabaseReference DB_O = FirebaseDatabase.getInstance().getReference().child("Organizations");

    public Adapter_admin_users(ArrayList<Account>list, RecycleViewClickListener listener)
    {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_view_users_card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       holder.username.setText(list.get(position).getName());
       holder.email.setText(list.get(position).getEmail());
       holder.password.setText(list.get(position).getPassword());

       holder.Delete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               AlertDialog.Builder builder = new AlertDialog.Builder(holder.username.getContext());
               builder.setMessage("Are sure you want delete this account?");
               builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       ref.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                               String email = list.get(position).getEmail();
                               for(DataSnapshot ds: snapshot.getChildren()){
                                   if(ds.child("email").getValue().toString().equals(email)){

                                       String pk_account = ds.getKey();
                                       String userType = ds.child("userType").getValue().toString();

                                       if(userType.equals("applicant")){
                                           DB_A.addValueEventListener(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                   for(DataSnapshot ds: snapshot.getChildren()){
                                                       if(ds.child("accountID").getValue().toString().equals(pk_account)){
                                                           String key_applicant = ds.getKey();
                                                           DB_A.child(key_applicant).removeValue();
                                                           System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&    applicant delete it    &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+pk_account);

                                                       }
                                                   }
                                               }
                                               @Override
                                               public void onCancelled(@NonNull DatabaseError error) {
                                               }
                                           });
                                       }
                                       else if(userType.equals("organization")){
                                           DB_O.addValueEventListener(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                   for(DataSnapshot ds: snapshot.getChildren()){
                                                       if(ds.child("accountID").getValue().toString().equals(pk_account)){
                                                           String key_organization = ds.getKey();
                                                           DB_O.child(key_organization).removeValue();
                                                           System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&    org delete it    &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+pk_account);

                                                       }
                                                   }
                                               }

                                               @Override
                                               public void onCancelled(@NonNull DatabaseError error) {

                                               }
                                           });
                                       }

                                       System.out.println("&&&&&&&&&&&&&&&&&&&&&   Delete account    &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+pk_account);
                                       ref.child(pk_account).removeValue();
                                   }
                               }
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {

                           }
                       });

                       Toast.makeText(holder.username.getContext(),"Delete it successfully", Toast.LENGTH_SHORT).show();
                   }
               }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       Toast.makeText(holder.username.getContext(),"No changes", Toast.LENGTH_SHORT).show();
                   }
               });
               builder.show();
           }
       });


    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface RecycleViewClickListener{
        void onClick(View v, int position);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView username, password, email;
        Button Delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.user_name);
            password  = itemView.findViewById(R.id.user_pass);
            email = itemView.findViewById(R.id.user_email);
            Delete = itemView.findViewById(R.id.delete_users);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }
}
