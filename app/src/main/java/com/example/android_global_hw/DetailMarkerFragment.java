package com.example.android_global_hw;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Objects;

public class DetailMarkerFragment extends Fragment {

    interface itemClickListener {
        void itemClicked(int markerId, String typeOfEditText, String newText);
    }

    private itemClickListener listener;
    private Marker marker;

    private ImageView detMarkerIcon;
    private EditText detMarkerLink;
    private EditText detMarkerHeader;
    private EditText detMarkerDescription;

    private Button startButton;
    private Button closeButton;
    private Button deleteButton;

    private boolean isCreated = false;

    public DetailMarkerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof itemClickListener) {
            this.listener = (itemClickListener) context;
        } else
            throw new ClassCastException();
    }

    public void updateMarker(Marker marker) {
        this.marker = marker;
        if (isCreated) {
            if (marker.getIcon() != null)
                detMarkerIcon.setImageBitmap(marker.getIcon());
            detMarkerLink.setText(marker.getLink());
            detMarkerDescription.setText(marker.getDescription());
        }

    }

    public static DetailMarkerFragment newInstance() {
        DetailMarkerFragment fragment = new DetailMarkerFragment();
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
        return inflater.inflate(R.layout.fragment_detail_marker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        detMarkerIcon = view.findViewById(R.id.det_marker_icon);
        detMarkerLink = view.findViewById(R.id.det_marker_link);
        detMarkerHeader = view.findViewById(R.id.det_marker_header);
        detMarkerDescription = view.findViewById(R.id.det_marker_description);
        startButton = view.findViewById(R.id.start_button);
        closeButton = view.findViewById(R.id.close_button);
        deleteButton = view.findViewById(R.id.delete_button);

        isCreated = true;
        if (marker != null) {
            if (marker.getIcon() != null)
                detMarkerIcon.setImageBitmap(marker.getIcon());
            detMarkerLink.setText(marker.getLink());
            detMarkerDescription.setText(marker.getDescription());
        }

        setEditTextListeners(detMarkerLink);
        setEditTextListeners(detMarkerHeader);
    }

    private void setEditTextListeners(@NonNull final EditText editText) {
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String newText = editText.getText().toString();
                    if (editText.getId() == R.id.det_marker_link) {
                        listener.itemClicked(marker.getMarkerID(), DBHelper.KEY_LINK, newText);
                    } else if (editText.getId() == R.id.det_marker_header) {
                        listener.itemClicked(marker.getMarkerID(), DBHelper.KEY_HEADER, newText);
                    } else
                        throw new NullPointerException();

                    final InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity())
                            .getApplicationContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    editText.clearFocus();
                    editText.setCursorVisible(false);
                    return true;
                }
                return false;
            }
        });
    }
}