package com.example.mytraining;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collections;

public class Recommended_Adapter extends RecyclerView.Adapter<Recommended_Adapter.MyViewHolder>{

    ArrayList<TrainingOffer>list;
    private RecycleViewClickListener listener;
    ArrayList<String>plist;
    ArrayList<TypeVariable>arrayList=new ArrayList<>();



    public Recommended_Adapter(ArrayList<TrainingOffer>list, RecycleViewClickListener listener, ArrayList<String> plist)
    {
        this.list = list;
        this.listener = listener;
        this.plist = plist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.offerName.setText(list.get(position).getOfferName());
        Glide.with(holder.offer_img.getContext()).load(list.get(position).getImageUrl()).placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop().error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.offer_img);


        if(plist.get(position).equals("50%")){
            holder.percentage.setText("50%");
            holder.percentage.setTextColor(Color.parseColor("#FF9800"));
        }


        else if(plist.get(position).equals("25%")){
            holder.percentage.setText("25%");
            holder.percentage.setTextColor(Color.parseColor("#FF5722"));
        }


        else if(plist.get(position).equals("75%")){
            holder.percentage.setText("75%");
            holder.percentage.setTextColor(Color.parseColor("#BCEFBE"));
        }


        else if(plist.get(position).equals("100%")) {
            holder.percentage.setText("100%");
            holder.percentage.setTextColor(Color.parseColor("#147117"));
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public interface RecycleViewClickListener{
        void onClick(View v, int position);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView offerName, percentage;
        private ImageView offer_img;
        //private CardView cardView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            offerName = itemView.findViewById(R.id.offer_name);
            offer_img = itemView.findViewById(R.id.img_org_offer);
            percentage = itemView.findViewById(R.id.percentage);
            //cardView = itemView.findViewById(R.id.cv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }

    }


}