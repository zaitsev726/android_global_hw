package com.example.android_global_hw;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.android_global_hw.adapter.MarkerAdapter;

public class MainActivity extends AppCompatActivity implements MarkerAdapter.onClickListener {

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

    }
}