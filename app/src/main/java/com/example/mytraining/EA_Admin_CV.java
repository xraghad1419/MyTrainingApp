package com.example.mytraining;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class EA_Admin_CV extends RecyclerView.Adapter<EA_Admin_CV.EmptyHolder> {
    @Override
    public EA_Admin_CV.EmptyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(EA_Admin_CV.EmptyHolder holder, int position) {

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
