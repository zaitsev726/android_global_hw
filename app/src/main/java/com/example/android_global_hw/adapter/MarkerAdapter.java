package com.example.android_global_hw.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MarkerAdapter extends RecyclerView.Adapter<MarkerAdapter.MarkerViewHolder> {

    class MarkerViewHolder extends RecyclerView.ViewHolder{
        private ImageView markerIcon;
        private TextView markerDescription;

        public MarkerViewHolder(View view){
            super(view);

        }
    }

    @NonNull
    @Override
    public MarkerAdapter.MarkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MarkerAdapter.MarkerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
