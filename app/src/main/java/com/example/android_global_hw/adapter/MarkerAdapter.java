package com.example.android_global_hw.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_global_hw.DBHelper;
import com.example.android_global_hw.Marker;
import com.example.android_global_hw.MarkerListFragment;
import com.example.android_global_hw.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MarkerAdapter extends RecyclerView.Adapter<MarkerAdapter.MarkerViewHolder> {
    private List<Marker> markerList;
    private final List<Marker> markerListCopy = new ArrayList<>();

    private static boolean[] selects;
    private static int selectsCount = 0;

    private int lastPosition = -1;
    private final Context context;


    private static AlphabetMode orderMode;

    /*
        Enum для установки состояния сортировки
     */
    private enum AlphabetMode {
        AtoZ, ZtoA, Normal;

        public static AlphabetMode getPrevious(AlphabetMode mode) {
            if (mode.equals(AtoZ)) {
                return Normal;
            } else if (mode.equals(ZtoA)) {
                return AtoZ;
            } else
                return ZtoA;
        }
    }

    /*
        Метод, сортирующий лист заметок в выбранном порядке
     */
    public void orderByAlphabet() {
        if(markerList != null && markerList.size() > 0) {
            if (orderMode.equals(AlphabetMode.AtoZ)) {
                orderByAtoZMode(new ArrayList<>(markerList));
                orderMode = AlphabetMode.ZtoA;
            } else if (orderMode.equals(AlphabetMode.ZtoA)) {
                orderByZtoAMode(new ArrayList<>(markerList));
                orderMode = AlphabetMode.Normal;
            } else if (orderMode.equals(AlphabetMode.Normal)) {
                markerList = new ArrayList<>(markerListCopy);
                orderMode = AlphabetMode.AtoZ;
            }
        }
    }

    public void filer(String text) {
        if(markerList!= null && markerList.size() > 0) {
            markerList.clear();
            if (text.isEmpty()) {
                markerList.addAll(markerListCopy);
            } else {
                text = text.toLowerCase();
                for (Marker marker : markerListCopy) {
                    if (marker.getHeader().toLowerCase().contains(text) || marker.getLink().toLowerCase().contains(text)) {
                        markerList.add(marker);
                    }
                }
            }

            notifyDataSetChanged();
        }
    }

    public interface onClickListener {

        void onMarkerHolderClick(Marker marker);

        void updateToolBarItems(boolean isMarkersSelected);

        DBHelper getDataBaseMarker();

        void setOnSearchViewListeners(MarkerAdapter adapter);
    }

    private onClickListener listener;

    public void setOnClickListener(onClickListener listener) {
        this.listener = listener;
        listener.setOnSearchViewListeners(this);
    }

    public void setItems(Collection<Marker> markers) {
        markerList.clear();
        markerList.addAll(markers);

        selects = new boolean[markerList.size()];
        selectsCount = 0;
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
                if (result != 0) {
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
        if (markers != null) {
            this.markerList = new ArrayList<>(markers);
            this.markerListCopy.addAll(markerList);
            if (selects == null) {
                selects = new boolean[markerList.size()];
            }
        }
        if (orderMode == null)
            orderMode = AlphabetMode.AtoZ;
        else {
            orderMode = AlphabetMode.getPrevious(orderMode);
            orderByAlphabet();
        }
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
    public void onBindViewHolder(@NonNull final MarkerAdapter.MarkerViewHolder holder, final int position) {
        final Marker marker = markerList.get(position);
        if (markerList.size() > position) {
            holder.setChanges(marker);
            if (selects[position])
                holder.itemView.setBackgroundColor(Color.LTGRAY);
            else
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }


        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMarkerHolderClick(marker);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    selects[position] = !selects[position];
                    if (selects[position]) {
                        holder.itemView.setBackgroundColor(Color.LTGRAY);
                        selects[position] = true;
                        if (selectsCount == 0) {
                            listener.updateToolBarItems(true);
                        }
                        selectsCount++;
                    } else {
                        holder.itemView.setBackgroundColor(Color.TRANSPARENT);
                        selects[position] = false;
                        selectsCount--;
                        if (selectsCount == 0) {
                            listener.updateToolBarItems(false);
                        }
                    }
                    return false;
                }
            });
        }
        setAnimation(holder.rootView, position);
    }

    @Override
    public int getItemCount() {
        if (markerList != null)
            return markerList.size();
        return 0;
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

    public void clearSelection() {
        selects = new boolean[markerList.size()];
        notifyDataSetChanged();
    }

    public ArrayList<Marker> deleteSelectedItems() {
        markerListCopy.clear();
        ArrayList<Marker> deletedMarkers = new ArrayList<>();
        for (int i = 0; i < markerList.size(); i++) {
            if (!selects[i]) {
                markerListCopy.add(markerList.get(i));
            } else {
                deletedMarkers.add(markerList.get(i));
            }
        }
        markerList.clear();
        markerList.addAll(markerListCopy);
        clearSelection();
        return deletedMarkers;
    }
}
