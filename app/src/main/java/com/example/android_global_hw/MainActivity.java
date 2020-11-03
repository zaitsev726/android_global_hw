package com.example.android_global_hw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.android_global_hw.adapter.MarkerAdapter;

public class MainActivity extends AppCompatActivity implements MarkerAdapter.onClickListener {
    private MarkerFragment markerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        addFragment();
    }

    private void addFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_fragment, MainFragment.newInstance())
                //.addToBackStack(null)
                .commit();
    }

    @Override
    public void onMarkerHolderClick(Marker marker) {
        View container = findViewById(R.id.detailed_info);
        if(container != null){
            if(markerFragment == null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                markerFragment = MarkerFragment.newInstance();
                ft.replace(R.id.detailed_info, markerFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
            markerFragment.updateMarker(marker);
        }
    }
}