package com.example.android_global_hw;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MarkerFragment extends Fragment {
    private Marker marker;
    private ImageView detMarkerIcon;
    private EditText detMarkerLink;
    private EditText detMarkerDescription;
    private Button startButton;
    private boolean isCreated = false;

    public MarkerFragment() {
        // Required empty public constructor
    }

    public void updateMarker(Marker marker) {
        this.marker = marker;
        if(isCreated){
        if (marker.getIcon() != null)
            detMarkerIcon.setImageBitmap(marker.getIcon());
        detMarkerLink.setText(marker.getLink());
        detMarkerDescription.setText(marker.getDescription());}

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        detMarkerIcon = view.findViewById(R.id.det_marker_icon);
        detMarkerLink = view.findViewById(R.id.det_marker_link);
        detMarkerDescription = view.findViewById(R.id.det_marker_description);
        startButton = view.findViewById(R.id.start_button);
        isCreated = true;
        if(marker!= null){
            if (marker.getIcon() != null)
                detMarkerIcon.setImageBitmap(marker.getIcon());
            detMarkerLink.setText(marker.getLink());
            detMarkerDescription.setText(marker.getDescription());
        }
    }
}