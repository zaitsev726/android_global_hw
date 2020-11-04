package com.example.android_global_hw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.example.android_global_hw.adapter.MarkerAdapter;

public class MainActivity extends AppCompatActivity implements MarkerAdapter.onClickListener {
    private MarkerFragment markerFragment;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        initializeDataBase();
    }

    private void initializeDataBase() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_LINK, "www.google.com");
                contentValues.put(DBHelper.KEY_HEADER, "it's google!");
                database.insert(DBHelper.TABLE_MARKER, null, contentValues);

                contentValues.clear();
                contentValues.put(DBHelper.KEY_LINK, "www.yandex.ru");
                contentValues.put(DBHelper.KEY_HEADER, "it's yandex!");
                database.insert(DBHelper.TABLE_MARKER, null, contentValues);

                contentValues.clear();
                contentValues.put(DBHelper.KEY_LINK, "www.google.com");
                contentValues.put(DBHelper.KEY_HEADER, "it's second google!");
                database.insert(DBHelper.TABLE_MARKER, null, contentValues);

                contentValues.clear();
                contentValues.put(DBHelper.KEY_LINK, "www.youtube.com");
                contentValues.put(DBHelper.KEY_HEADER, "it's youtube!!");
                database.insert(DBHelper.TABLE_MARKER, null, contentValues);
            }
        });
        thread.start();
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
                // .addToBackStack(null)
                .commit();
    }

    @Override
    public void onMarkerHolderClick(Marker marker) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        markerFragment = MarkerFragment.newInstance();
        ft.replace(R.id.main_fragment, markerFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
        markerFragment.updateMarker(marker);
    }

    @Override
    public DBHelper getDataBaseMarker() {
        return dbHelper;
    }
}