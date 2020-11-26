package com.example.android_global_hw;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

public class MarkerInfoFragment extends Fragment {

    interface itemClickListener {
        void markerSaveChanges(int markerId, String newDetMarkerLink, String newDetMarkerHeader,
                               String newDetMarkerDescription);
        void setAddMode();
    }

    private itemClickListener listener;
    private static Marker marker;
    private ImageView detMarkerIcon;
    private EditText detMarkerLink;
    private EditText detMarkerHeader;
    private EditText detMarkerDescription;

    private Button startLinkButton;
    private Button closeMarkerButton;
    private Button saveMarkerButton;
    private boolean isCreated = false;
    private static boolean textWasChanged = false;

    public MarkerInfoFragment() {
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
        MarkerInfoFragment.marker = marker;
        if (isCreated) {
            if (marker.getIcon() != null)
                detMarkerIcon.setImageBitmap(marker.getIcon());
            detMarkerLink.setText(marker.getLink());
            detMarkerDescription.setText(marker.getDescription());
        }

    }

    public static MarkerInfoFragment newInstance() {
        MarkerInfoFragment fragment = new MarkerInfoFragment();
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
        return inflater.inflate(R.layout.fragment_marker_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(listener != null)
            listener.setAddMode();
        detMarkerIcon = view.findViewById(R.id.det_marker_icon);
        detMarkerLink = view.findViewById(R.id.det_marker_link);
        detMarkerHeader = view.findViewById(R.id.det_marker_header);
        detMarkerDescription = view.findViewById(R.id.det_marker_description);
        startLinkButton = view.findViewById(R.id.start_button);
        closeMarkerButton = view.findViewById(R.id.close_button);
        saveMarkerButton = view.findViewById(R.id.save_button);

        isCreated = true;
        if (marker != null) {
            if (marker.getIcon() != null)
                detMarkerIcon.setImageBitmap(marker.getIcon());
            detMarkerLink.setText(marker.getLink());
            detMarkerHeader.setText(marker.getHeader());
            detMarkerDescription.setText(marker.getDescription());
        }

        setEditTextListeners(detMarkerLink);
        setEditTextListeners(detMarkerHeader);
        setEditTextListeners(detMarkerDescription);
        setStartLinkListener(startLinkButton);
        setCloseMarkerListener(closeMarkerButton);
        setSaveMarkerListener(saveMarkerButton);
    }


    private void setEditTextListeners(@NonNull final EditText editText) {
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    textWasChanged = true;

                   /* String newText = editText.getText().toString();
                    if (editText.getId() == R.id.det_marker_link) {
                        listener.markerItemClicked(marker.getMarkerID(), DBHelper.KEY_LINK, newText);
                    } else if (editText.getId() == R.id.det_marker_header) {
                        listener.markerItemClicked(marker.getMarkerID(), DBHelper.KEY_HEADER, newText);
                    } else if (editText.getId() == R.id.det_marker_description) {
                        listener.markerItemClicked(marker.getMarkerID(), DBHelper.KEY_DESCRIPTION, newText);
                    } else
                        throw new NullPointerException();*/

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

    private void setStartLinkListener(@NonNull final Button startButton) {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (marker != null) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                    if (!marker.getLink().contains("https://") && !marker.getLink().contains("http://"))
                        browserIntent.setData(Uri.parse("https://" + marker.getLink()));
                    else
                        browserIntent.setData(Uri.parse(marker.getLink()));
                    String title = (String) getResources().getText(R.string.chooser_title);
                    Intent chooser = Intent.createChooser(browserIntent, title);
                    startActivity(chooser);
                }
            }
        });
    }

    private void setCloseMarkerListener(Button closeMarkerButton) {
        closeMarkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textWasChanged) {
                    AlertDialog diaBox = AskOption();
                    diaBox.show();
                } else {
                    getActivity().onBackPressed();
                }
            }
        });
    }

    private void setSaveMarkerListener(Button saveMarkerButton) {
        saveMarkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = String.valueOf(detMarkerLink.getText());
                String header = String.valueOf(detMarkerHeader.getText());
                String description = String.valueOf(detMarkerDescription.getText());
                if (marker == null)
                    marker = new Marker(link, header, description);
                else {
                    marker.setLink(link);
                    marker.setHeader(header);
                    marker.setDescription(description);
                }
                listener.markerSaveChanges(marker.getMarkerID(),
                        marker.getLink(),
                        marker.getHeader(),
                        marker.getDescription());
                textWasChanged = false;
            }
        });
    }

    private AlertDialog AskOption() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle(getResources().getString(R.string.save_title));
        builder.setMessage(getResources().getString(R.string.save_message));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                //your deleting code
                getActivity().onBackPressed();
                dialog.dismiss();
            }

        });
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog myQuittingDialogBox = builder
                .create();
        return myQuittingDialogBox;

    }
}