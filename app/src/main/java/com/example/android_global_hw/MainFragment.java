package com.example.android_global_hw;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android_global_hw.adapter.MarkerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    private List<Marker> markerList;
    private MarkerAdapter.onClickListener listener;

    public MainFragment() {
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

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //  initData();

        if (listener.getDataBaseMarker() != null) {
            SQLiteDatabase database = listener.getDataBaseMarker().getReadableDatabase();
            Cursor cursor = database.query(DBHelper.TABLE_MARKER, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                markerList = new ArrayList<>();
                int linkIndex = cursor.getColumnIndex(DBHelper.KEY_LINK);
                int headerIndex = cursor.getColumnIndex(DBHelper.KEY_HEADER);
                do {
                    Marker marker = new Marker(cursor.getString(linkIndex), cursor.getString(headerIndex), null);
                    if(!markerList.contains(marker))
                        markerList.add(marker);
                } while (cursor.moveToNext());
            }
        }

        RecyclerView.LayoutManager manager = new LinearLayoutManager(view.getContext());
        RecyclerView recyclerView = view.findViewById(R.id.marker_list);
        MarkerAdapter markerAdapter = new MarkerAdapter(view.getContext(), markerList);
        if (listener != null) {
            markerAdapter.setOnClickListener(listener);
        }
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(markerAdapter);
        super.onViewCreated(view, savedInstanceState);
    }

    private void initData() {
        markerList = new ArrayList<>();
        markerList.add(new Marker("google.com", "it's google", null));
        markerList.add(new Marker("yandex.com", "it's yandex", null));
        markerList.add(new Marker("yahooo.com", "it's yahooo", null));
        markerList.add(new Marker("youtube.com", "it's youtube", null));
    }
}