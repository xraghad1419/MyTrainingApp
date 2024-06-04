package com.example.mytraining;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class EA_Admin_Users extends RecyclerView.Adapter<EA_Admin_Users.EmptyHolder> {
    @Override
    public EA_Admin_Users.EmptyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(EA_Admin_Users.EmptyHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class EmptyHolder extends RecyclerView.ViewHolder {
        public EmptyHolder(View itemView) {
            super(itemView);
        }
    }
}
