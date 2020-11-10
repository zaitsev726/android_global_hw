package com.example.android_global_hw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.widget.Toolbar;

import com.example.android_global_hw.adapter.MarkerAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MarkerAdapter.onClickListener, MarkerInfoFragment.itemClickListener {
    //  private MarkerListFragment markerListFragment;
    private DBHelper dbHelper;
    private String SAVED_STATE = "INSTANCE_SAVED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mActionBarToolBar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolBar);
        dbHelper = new DBHelper(this);
        if (savedInstanceState == null)
            addFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //addFragment();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putByte(SAVED_STATE, (byte) 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    private void addFragment() {
        // markerListFragment = MarkerListFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_fragment, MarkerListFragment.newInstance())
                // .addToBackStack(null)
                .commit();
    }

    @Override
    public void onMarkerHolderClick(Marker marker) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MarkerInfoFragment markerInfoFragment = MarkerInfoFragment.newInstance();
        ft.replace(R.id.main_fragment, markerInfoFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
        markerInfoFragment.updateMarker(marker);
    }

    @Override
    public DBHelper getDataBaseMarker() {
        return dbHelper;
    }

    @Override
    public void itemClicked(int markerId, String typeOfEditText, String newText) {
        dbHelper.updateTable(markerId, typeOfEditText, newText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }


}