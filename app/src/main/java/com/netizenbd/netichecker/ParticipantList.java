package com.netizenbd.netichecker;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.netizenbd.netichecker.adapter.MyAdapter;
import com.netizenbd.netichecker.sqlitedatabase.DataEntity;
import com.netizenbd.netichecker.sqlitedatabase.DataService;

import java.util.ArrayList;
import java.util.List;

public class ParticipantList extends AppCompatActivity implements SearchView.OnQueryTextListener {

    TextView textView_amount;
    RecyclerView recyclerView_participant_list;
    CardView cardView_list;
    MyAdapter myAdapter;
    List<DataEntity> dataEntityList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Participant List");

        textView_amount = (TextView) findViewById(R.id.textView_amount);

        DataService dataService = new DataService(this);
        dataEntityList = dataService.getDataEntityList(this);
        if (dataEntityList.size() > 0) {
            textView_amount.setText("Total Participant: " + dataEntityList.size());
        } else {
            textView_amount.setText("No Participant");
        }


        /**
         * Recycler view
         */
        recyclerView_participant_list = (RecyclerView) findViewById(R.id.recyclerView_participant_list);
        cardView_list = (CardView) findViewById(R.id.cardView_list);
        myAdapter = new MyAdapter(this, dataEntityList); // on Click listener inside it
        recyclerView_participant_list.setAdapter(myAdapter);
        recyclerView_participant_list.setHasFixedSize(true);

        // Layout manager for Recycler view
        recyclerView_participant_list.setLayoutManager(new LinearLayoutManager(this));

    } // end of onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        /**
         * For search
         */
        final MenuItem item = menu.findItem(R.id.actionSearch);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        myAdapter.setFilter(dataEntityList);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    /**
     * onQueryTextSubmit() and onQueryTextChange()
     * For implementation of SearchView.OnQueryTextListener
     *
     * @param query
     * @return
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<DataEntity> filteredModelList = filter(dataEntityList, newText);

        myAdapter.setFilter(filteredModelList);
        Log.d("touhid Changed", "onQueryTextChange: ");
        return true;
    }

    private List<DataEntity> filter(List<DataEntity> dataEntities, String query) {
        query = query.toLowerCase();
        final List<DataEntity> filteredModelList = new ArrayList<>();
        for (DataEntity model : dataEntities) {
            final String name = model.getName().toLowerCase(); // to search by name
            final String type = model.getParticipateType().toLowerCase(); // to search by type
            final String phone = model.getPhone().toLowerCase(); // to search by phone
            final String area = model.getArea().toLowerCase(); // to search by area
            if (name.contains(query) || type.contains(query) || phone.contains(query) || area.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

}
