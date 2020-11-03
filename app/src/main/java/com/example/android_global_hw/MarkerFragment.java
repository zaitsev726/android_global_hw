package com.example.android_global_hw;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MarkerFragment extends Fragment {
    private Marker marker;

    public MarkerFragment() {
        // Required empty public constructor
    }
    public void updateMarker(Marker marker){
        this.marker = marker;
    }
    public static MarkerFragment newInstance() {
        MarkerFragment fragment = new MarkerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_marker, container, false);
    }
}