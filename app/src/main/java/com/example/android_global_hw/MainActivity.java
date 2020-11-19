package com.example.android_global_hw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.example.android_global_hw.adapter.MarkerAdapter;

public class MainActivity extends AppCompatActivity implements MarkerAdapter.onClickListener, MarkerInfoFragment.itemClickListener {
  //  private MarkerListFragment markerListFragment;
    private DBHelper dbHelper;
    private final String SAVED_STATE = "INSTANCE_SAVED";
    private SearchView searchMenuItem;
    private MenuItem sortAbMenuItem;
    private MenuItem removeMenuItem;
    private MarkerAdapter adapter;


    private static ApplicationMode mode;

    private enum ApplicationMode {
        NORMAL, REMOVE, SEARCH
    }

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
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchMenuItem = (SearchView) searchItem.getActionView();
        searchMenuItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filer(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filer(newText);
                return false;
            }
        });
        sortAbMenuItem = menu.findItem(R.id.action_sort);
        removeMenuItem = menu.findItem(R.id.action_remove);

        if (mode == null)
            mode = ApplicationMode.NORMAL;
        startSpecificApplicationMode();
        return true;
    }

    private void startSpecificApplicationMode() {

        if (mode.equals(ApplicationMode.NORMAL)) {
            searchMenuItem.setVisibility(View.VISIBLE);
            searchMenuItem.setIconified(false);
            sortAbMenuItem.setVisible(true);
            removeMenuItem.setVisible(false);
        } else if (mode.equals(ApplicationMode.REMOVE)) {
            searchMenuItem.setVisibility(View.INVISIBLE);
            searchMenuItem.setIconified(true);
            sortAbMenuItem.setVisible(false);
            removeMenuItem.setVisible(true);
        } else if (mode.equals(ApplicationMode.SEARCH)) {
            sortAbMenuItem.setVisible(false);
            removeMenuItem.setVisible(false);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mode == ApplicationMode.REMOVE && !item.equals(removeMenuItem)) {
            adapter.clearSelection();
        } else if (mode == ApplicationMode.REMOVE && item.equals(removeMenuItem)) {
            //TODO add service to delete from database
        }

        if (item.equals(searchMenuItem)) {
            mode = ApplicationMode.SEARCH;
        } else if (item.equals(sortAbMenuItem)) {
            adapter.orderByAlphabet();
            mode = ApplicationMode.NORMAL;
        }
        startSpecificApplicationMode();
        return super.onOptionsItemSelected(item);
    }

    private void addFragment() {
       // markerListFragment = MarkerListFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_fragment,  MarkerListFragment.newInstance())
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
    public void updateToolBarItems(boolean isMarkersSelected) {
        if (isMarkersSelected) {
            mode = ApplicationMode.REMOVE;
        } else {
            mode = ApplicationMode.NORMAL;
        }
        startSpecificApplicationMode();
    }

    @Override
    public void setOnSearchViewListeners(MarkerAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public DBHelper getDataBaseMarker() {
        return dbHelper;
    }

    @Override
    public void markerItemClicked(int markerId, String typeOfEditText, String newText) {
        dbHelper.updateTable(markerId, typeOfEditText, newText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }


}