package com.example.android_global_hw.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_global_hw.DBHelper;
import com.example.android_global_hw.Marker;
import com.example.android_global_hw.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MarkerAdapter extends RecyclerView.Adapter<MarkerAdapter.MarkerViewHolder> {
    private List<Marker> markerList = new ArrayList<>();
    private int lastPosition = -1;
    private final Context context;

    public interface onClickListener {

        void onMarkerHolderClick(Marker marker);

        DBHelper getDataBaseMarker();
    }
    private onClickListener listener;
    public void setOnClickListener(onClickListener listener) {
        this.listener = listener;
    }

    public void setItems(Collection<Marker> markers) {
        markerList.clear();
        markerList.addAll(markers);
        notifyDataSetChanged();
    }

    public void clearItems() {
        markerList.clear();
        notifyDataSetChanged();
    }

    public void orderByAtoZMode(List<Marker> markerList) {
        Collections.sort(markerList, new Comparator<Marker>() {
            @Override
            public int compare(Marker o1, Marker o2) {
                return o1.getHeader().compareTo(o2.getHeader());
            }
        });
        this.markerList = markerList;
        notifyDataSetChanged();
    }

    public void orderByZtoAMode(List<Marker> markerList) {
        Collections.sort(markerList, new Comparator<Marker>() {
            @Override
            public int compare(Marker o1, Marker o2) {
                int result = o1.getHeader().compareTo(o2.getHeader());
                if(result != 0){
                    return -result;
                }
                return result;
            }
        });
        this.markerList = markerList;
        notifyDataSetChanged();
    }

    public MarkerAdapter(Context context, List<Marker> markers) {
        this.context = context;
        setItems(markers);
    }
    class MarkerViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;

        public MarkerViewHolder(View view) {
            super(view);
            rootView = view;
        }

        public void setChanges(Marker marker) {
            final TextView markerLink = rootView.findViewById(R.id.marker_link);
            setText(markerLink, marker.getLink());
            final TextView markerHeader = rootView.findViewById(R.id.marker_header);
            setText(markerHeader, marker.getHeader());

            final ImageView markerIcon = rootView.findViewById(R.id.marker_icon);
            if (marker.getIcon() != null) {
                setIcon(markerIcon, marker.getIcon());
            }
        }

        private void setText(TextView target, String text) {
            if (target != null) {
                target.setText(text);
            }
        }

        private void setIcon(ImageView target, Bitmap icon) {
            if (target != null) {
                target.setImageBitmap(icon);
            }
        }

        public void clearAnimation() {
            rootView.clearAnimation();
        }
    }

    @NonNull
    @Override
    public MarkerAdapter.MarkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.marker_item_view, parent, false);
        return new MarkerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarkerAdapter.MarkerViewHolder holder, final int position) {
        final Marker marker = markerList.get(position);
        if (markerList.size() > position) {
            holder.setChanges(marker);
        }
        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMarkerHolderClick(marker);
                }
            });
        }
        setAnimation(holder.rootView, position);
    }

    @Override
    public int getItemCount() {
        return markerList.size();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MarkerViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.clearAnimation();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            final Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
