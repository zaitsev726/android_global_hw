package com.example.android_global_hw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.android_global_hw.adapter.MarkerAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MarkerAdapter.onClickListener, MarkerInfoFragment.itemClickListener {
    private MarkerListFragment markerListFragment;
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
       // initializeDataBase();
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
      //  database = dbHelper.getWritableDatabase();
    }

    private void addFragment() {
        markerListFragment = MarkerListFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_fragment, markerListFragment)
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
       /* Cursor cursor = database.rawQuery("select * from " + DBHelper.TABLE_MARKER + " where " +
                DBHelper.KEY_ID + "=?", new String[markerId]);
        cursor.moveToFirst();*/
      /*  ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_ID, markerId);
        contentValues.put(typeOfEditText, newText);
        database.update(DBHelper.TABLE_MARKER, contentValues, DBHelper.KEY_ID + "=?", new String[]{String.valueOf(markerId)});*/
        dbHelper.updateTable(markerId,typeOfEditText,newText);
        //TODO remove this function
        markerListFragment.updateMarkerList(markerId,typeOfEditText,newText);
     //   cursor.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}