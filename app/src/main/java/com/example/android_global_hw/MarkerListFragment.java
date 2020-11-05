package com.example.android_global_hw;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.util.ArrayList;
import java.util.Iterator;
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

        if (listener.getDataBaseMarker() != null) {
            SQLiteDatabase database = listener.getDataBaseMarker().getReadableDatabase();
            Cursor cursor = database.query(DBHelper.TABLE_MARKER, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                markerList = new ArrayList<>();
                int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                int linkIndex = cursor.getColumnIndex(DBHelper.KEY_LINK);
                int headerIndex = cursor.getColumnIndex(DBHelper.KEY_HEADER);
                do {
                    Marker marker = new Marker(cursor.getString(linkIndex), cursor.getString(headerIndex), null);
                    marker.setMarkerID(cursor.getInt(idIndex));
                    if (!markerList.contains(marker))
                        markerList.add(marker);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        RecyclerView.LayoutManager manager = new LinearLayoutManager(view.getContext());
        RecyclerView recyclerView = view.findViewById(R.id.marker_list);
        adapter = new MarkerAdapter(view.getContext(), markerList);
        if (listener != null) {
            adapter.setOnClickListener(listener);
        }
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        super.onViewCreated(view, savedInstanceState);
    }

    public void updateMarkerList(int markerId, String typeOfEditText, String newText) {
        adapter.clearItems();
        Marker marker2Update = deleteItem(markerId);
        marker2Update.updateField(typeOfEditText, newText);
        insertNewElement(marker2Update);
    }

    public Marker deleteItem(int markerId) {
        Iterator<Marker> iterator = markerList.iterator();
        while (iterator.hasNext()) {
            Marker next = iterator.next();
            if (next.getMarkerID() == markerId) {
                iterator.remove();
                return next;
            }
        }
        throw new NullPointerException();
    }

    public void insertNewElement(Marker marker) {
        markerList.add(marker);
        adapter.setItems(markerList);
    }
}