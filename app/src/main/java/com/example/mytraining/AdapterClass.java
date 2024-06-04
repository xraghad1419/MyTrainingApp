package com.example.mytraining;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.MyViewHolder> {

    ArrayList<Organization>list;
    private RecycleViewClickListener listener;

    public AdapterClass(ArrayList<Organization>list, RecycleViewClickListener listener)
    {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.orgName.setText(list.get(position).getName());
        Glide.with(holder.orgImage.getContext()).load(list.get(position).getImageUrl()).placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop().error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.orgImage);

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface RecycleViewClickListener{
        void onClick(View v, int position);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView orgImage;
        private TextView orgName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            orgImage = itemView.findViewById(R.id.image_org);
            orgName = itemView.findViewById(R.id.Org_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }
}
