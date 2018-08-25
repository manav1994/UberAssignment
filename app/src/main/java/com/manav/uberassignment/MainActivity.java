package com.manav.uberassignment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.manav.uberassignment.Interfaces.MainInterface;
import com.manav.uberassignment.adapter.GridAdapter;
import com.manav.uberassignment.model.AddToAdapter;
import com.manav.uberassignment.presenter.MainPresenter;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, MainInterface.view {

    RecyclerView recyclerView;
    GridLayoutManager staggeredGridLayoutManager;
    GridAdapter gridApdapter;
    ArrayList<AddToAdapter> addToAdapters;
    ProgressBar progressbottom;
    private int pastVisibleItem, visibleItemCount, totalItemCount, previousTotal = 0;
    private int viewThreshold = 9;
    int pageNo = 1; //page number for pagination
    private String globalText = "";
    MainInterface.Actions mainInterfaceAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainInterfaceAction = new MainPresenter(this);
        progressbottom = (ProgressBar) findViewById(R.id.progressbottom);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        staggeredGridLayoutManager = new GridLayoutManager(this, 3);
        staggeredGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        addToAdapters = new ArrayList<>();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = staggeredGridLayoutManager.getChildCount();
                totalItemCount = staggeredGridLayoutManager.getItemCount();
                pastVisibleItem = staggeredGridLayoutManager.findFirstCompletelyVisibleItemPosition();

                if (dy > 0) {
                    if (totalItemCount > previousTotal) {
                        previousTotal = totalItemCount;
                    }
                    if ((totalItemCount - visibleItemCount) <= (pastVisibleItem + viewThreshold)) {
                        pageNo++;
                        mainInterfaceAction.callNextPages(pageNo, globalText);
                    }
                }
            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_item, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);
        searchView.setQuery("flower",true); // default for first time
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String newText) {
        hideSoftKey();
        if(!isNetworkAvailable()){
            ToastMessage("connect to network");
        }else {
            if (!newText.equalsIgnoreCase(globalText) && newText.trim().length() >= 3) {
                if (gridApdapter != null) {
                    gridApdapter.clearList(); // clearing list to add fresh data.
                    pastVisibleItem = 0;
                    visibleItemCount = 0;
                    previousTotal = 0;
                    totalItemCount = 0;
                    globalText = newText;
                    mainInterfaceAction.callNextPages(pageNo, globalText);
                } else {
                    globalText = newText;
                    mainInterfaceAction.callNextPages(pageNo, globalText);// for loading data first time as instance of gridAdapter will bw null.
                }
            }
            globalText = newText;
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    @Override
    public void showPrgress() {
        progressbottom.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressbottom.setVisibility(View.GONE);
    }

    @Override
    public void initializeAdapter(ArrayList<AddToAdapter> list) {
        gridApdapter = new GridAdapter(MainActivity.this, list);
        recyclerView.setAdapter(gridApdapter);
    }

    @Override
    public void notifydataChanges(ArrayList<AddToAdapter> list) {
        gridApdapter.setNewData(list);
    }

    @Override
    public void ToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void hideSoftKey() {
        try {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}

