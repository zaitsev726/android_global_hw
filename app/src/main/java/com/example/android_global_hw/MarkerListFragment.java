package com.example.android_global_hw;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private static AlphabetMode orderMode;

    public MarkerAdapter getAdapter() {
        return adapter;
    }

    private enum AlphabetMode {
        AtoZ, ZtoA, Normal;

    }
    public MarkerListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        orderMode = AlphabetMode.AtoZ;
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




        /*RecyclerView.LayoutManager manager = new LinearLayoutManager(view.getContext());
        RecyclerView recyclerView = view.findViewById(R.id.marker_list);
        adapter = new MarkerAdapter(view.getContext(), markerList);
        if (listener != null) {
            adapter.setOnClickListener(listener);
        }
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);*/
        initializeAdapter(view);
       /* Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);

        MenuItem searchItem = toolbar.findViewById(R.id.action_search);
        SearchView searchMenuItem = (SearchView) searchItem.getActionView();
        searchMenuItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getAdapter().filer(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getAdapter().filer(newText);
                return false;
            }
        });*/

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

    public void updateMarkerList(int markerId, String typeOfEditText, String newText) {
        adapter.clearItems();
       // Marker marker2Update = deleteItem(markerId);
       // marker2Update.updateField(typeOfEditText, newText);
       // insertNewElement(marker2Update);
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

    public void orderByAlphabet() {
        if(orderMode.equals(AlphabetMode.AtoZ)){
            adapter.orderByAtoZMode(new ArrayList<>(markerList));
            orderMode = AlphabetMode.ZtoA;
        }else if(orderMode.equals(AlphabetMode.ZtoA)){
            adapter.orderByZtoAMode(new ArrayList<>(markerList));
            orderMode = AlphabetMode.Normal;
        }else if(orderMode.equals(AlphabetMode.Normal)){
            adapter.setItems(new ArrayList<>(markerList));
            orderMode = AlphabetMode.AtoZ;
        }

    }
}