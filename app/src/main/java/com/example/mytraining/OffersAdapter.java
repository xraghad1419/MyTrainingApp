package com.example.mytraining;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import okhttp3.internal.cache.DiskLruCache;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.MyViewHolder>{

    ArrayList<TrainingOffer>list;
    private RecycleViewClickListener listener;

    public OffersAdapter(ArrayList<TrainingOffer>list, RecycleViewClickListener listener)
    {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_offer,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.offerName.setText(list.get(position).getOfferName());
        Glide.with(holder.offer_img.getContext()).load(list.get(position).getImageUrl()).placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop().error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.offer_img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public interface RecycleViewClickListener{
        void onClick(View v, int position);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView offerName;
        private ImageView offer_img;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            offerName = itemView.findViewById(R.id.offer_name);
            offer_img = itemView.findViewById(R.id.img_org_offer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }

    }


}