package com.example.mytraining;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Reviews_Adapter extends RecyclerView.Adapter<Reviews_Adapter.MyViewHolder>{

    ArrayList<Review>list;
    //private RecycleViewClickListener listener;
    DatabaseReference reviewDB = FirebaseDatabase.getInstance().getReference().child("Reviews");

    public Reviews_Adapter(ArrayList<Review>list)
    {
        this.list = list;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card_holder,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.app_review.setText(list.get(position).getReview());
        reviewDB.child(list.get(position).getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String rate = snapshot.child("ratingStar").getValue().toString();
                float rate_star = Float.parseFloat(rate);
                holder.ratingBar.setRating(rate_star);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.name.setText(list.get(position).getApplicantName());
        holder.date.setText(list.get(position).getRate_date());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public interface RecycleViewClickListener{
        void onClick(View v, int position);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RatingBar ratingBar;
        private TextView app_review, name, date;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            app_review = itemView.findViewById(R.id.app_rev);
            name = itemView.findViewById(R.id.name_a);
            date = itemView.findViewById(R.id.date);
            //itemView.setOnClickListener(this);
        }
/*
        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }

 */
    }
}