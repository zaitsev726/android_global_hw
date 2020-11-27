package com.example.android_global_hw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentTransaction;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.widget.Toolbar;

import com.example.android_global_hw.adapter.MarkerAdapter;
import com.example.android_global_hw.database.DBHelper;
import com.example.android_global_hw.database.Marker;

public class MainActivity extends AppCompatActivity implements MarkerAdapter.onClickListener, MarkerInfoFragment.itemClickListener {
    //  private MarkerListFragment markerListFragment;
    private DBHelper dbHelper;
    private final String SAVED_STATE = "INSTANCE_SAVED";
    private SearchView searchViewInMenuItem;
    private MenuItem searchMenuItem;
    private MenuItem sortAbMenuItem;
    private MenuItem removeMenuItem;
    private MenuItem addMenuItem;
    private MarkerAdapter adapter;


    private static ApplicationMode mode;

    private enum ApplicationMode {
        NORMAL, REMOVE, SEARCH, ADD
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
        searchMenuItem = menu.findItem(R.id.action_search);
        searchViewInMenuItem = (SearchView) searchMenuItem.getActionView();
        searchViewInMenuItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        addMenuItem = menu.findItem(R.id.action_add);

        if (mode == null)
            mode = ApplicationMode.NORMAL;
        startSpecificApplicationMode();
        return true;
    }

    private void startSpecificApplicationMode() {

        if (mode.equals(ApplicationMode.NORMAL)) {
            searchMenuItem.setVisible(true);
            searchViewInMenuItem.setVisibility(View.VISIBLE);
            sortAbMenuItem.setVisible(true);
            removeMenuItem.setVisible(false);
            addMenuItem.setVisible(true);
            Toolbar mActionBarToolBar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            FrameLayout frameLayout = findViewById(R.id.main_fragment);
            mActionBarToolBar.setBackgroundColor(getResources().getColor(R.color.toolbar_orange));
            frameLayout.setBackgroundColor(getResources().getColor(R.color.refresh_progress_1));

        } else if (mode.equals(ApplicationMode.REMOVE)) {
            searchMenuItem.setVisible(false);
            searchViewInMenuItem.setVisibility(View.GONE);
            searchViewInMenuItem.setIconified(true);
            sortAbMenuItem.setVisible(false);
            removeMenuItem.setVisible(true);
            addMenuItem.setVisible(false);
        } else if (mode.equals(ApplicationMode.ADD)) {
            searchMenuItem.setVisible(false);
            searchViewInMenuItem.setVisibility(View.GONE);
            searchViewInMenuItem.setIconified(true);
            sortAbMenuItem.setVisible(false);
            removeMenuItem.setVisible(false);
            addMenuItem.setVisible(false);
            Toolbar mActionBarToolBar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            FrameLayout frameLayout = findViewById(R.id.main_fragment);
            mActionBarToolBar.setBackgroundColor(getResources().getColor(R.color.status_bar_red));
            frameLayout.setBackgroundColor(getResources().getColor(R.color.price_red));
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mode == ApplicationMode.REMOVE && !item.equals(removeMenuItem)) {
            adapter.clearSelection();
        } else if (mode == ApplicationMode.REMOVE && item.equals(removeMenuItem)) {
            dbHelper.deleteSelectedItems(adapter.deleteSelectedItems());
            mode = ApplicationMode.NORMAL;
        }

        if (item.equals(searchMenuItem)) {
            mode = ApplicationMode.SEARCH;
        } else if (item.equals(sortAbMenuItem)) {
            adapter.orderByAlphabet();
            mode = ApplicationMode.NORMAL;
        } else if (item.equals(addMenuItem)) {
            mode = ApplicationMode.ADD;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            MarkerInfoFragment markerInfoFragment = MarkerInfoFragment.newInstance();
            ft.replace(R.id.main_fragment, markerInfoFragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        }
        startSpecificApplicationMode();
        return super.onOptionsItemSelected(item);
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
    public Cursor findAllMarkersInDataBase() {
        if (dbHelper == null)
            return null;
        return dbHelper.findAllMarkersInDataBase();
    }

    @Override
    public void setNormalMode() {
        mode = ApplicationMode.NORMAL;
        if (searchMenuItem != null)
            startSpecificApplicationMode();
    }


    @Override
    public void setAddMode() {
        mode = ApplicationMode.ADD;
        if (searchMenuItem != null)
            startSpecificApplicationMode();
    }


    @Override
    public void markerSaveChanges(int markerId, String newDetMarkerLink, String newDetMarkerHeader, String newDetMarkerDescription) {
        if (markerId == 0) {
            dbHelper.insertNewMarker(newDetMarkerLink, newDetMarkerHeader, newDetMarkerDescription);
        } else {
            dbHelper.updateMarker(markerId, newDetMarkerLink, newDetMarkerHeader, newDetMarkerDescription);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

}