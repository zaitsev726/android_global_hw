package com.example.android_global_hw;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android_global_hw.adapter.MarkerAdapter;
import com.example.android_global_hw.database.DBHelper;
import com.example.android_global_hw.database.Marker;

import java.util.ArrayList;
import java.util.List;

public class MarkerListFragment extends Fragment {
    private List<Marker> markerList;
    private MarkerAdapter adapter;
    private MarkerAdapter.onClickListener listener;

    public MarkerListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MarkerAdapter.onClickListener) {
            listener = (MarkerAdapter.onClickListener) context;
        }
    }

    public static MarkerListFragment newInstance() {
        Bundle args = new Bundle();
        MarkerListFragment fragment = new MarkerListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_marker_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        if (listener.findAllMarkersInDataBase() != null) {
            listener.setNormalMode();
            Cursor cursor = listener.findAllMarkersInDataBase();
            if (cursor.moveToFirst()) {
                markerList = new ArrayList<>();
                int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                int linkIndex = cursor.getColumnIndex(DBHelper.KEY_LINK);
                int headerIndex = cursor.getColumnIndex(DBHelper.KEY_HEADER);
                int descriptionIndex = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION);
                do {
                    //TODO save bitmap
                    Marker marker = new Marker(cursor.getInt(idIndex),
                            cursor.getString(linkIndex),
                            cursor.getString(headerIndex),
                            cursor.getString(descriptionIndex),
                            null);
                    if (!markerList.contains(marker))
                        markerList.add(marker);
                } while (cursor.moveToNext());
            }

            cursor.close();
        }

        initializeAdapter(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void initializeAdapter(View view){
            RecyclerView.LayoutManager manager = new LinearLayoutManager(view.getContext());
            RecyclerView recyclerView = view.findViewById(R.id.marker_list);
            adapter = new MarkerAdapter(view.getContext(), markerList);
            if (listener != null) {
                adapter.setOnClickListener(listener);
            }
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);

    }
}