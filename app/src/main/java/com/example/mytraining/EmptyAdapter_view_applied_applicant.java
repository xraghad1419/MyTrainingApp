package com.example.mytraining;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class EmptyAdapter_view_applied_applicant extends RecyclerView.Adapter<EmptyAdapter_view_applied_applicant.EmptyHolder> {
    @Override
    public EmptyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(EmptyHolder holder, int position) {

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