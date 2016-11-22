package com.netizenbd.netichecker;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.netizenbd.netichecker.adapter.MyAdapter;
import com.netizenbd.netichecker.sqlitedatabase.DataEntity;
import com.netizenbd.netichecker.sqlitedatabase.DataService;

import java.util.List;

public class ParticipantList extends AppCompatActivity {

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

//        ActionBar ab = getActionBar();
//        ab.setHomeButtonEnabled(true);
//        ab.setDisplayHomeAsUpEnabled(true);
//        ab.setTitle(R.string.app_name);
//        ab.show();

        textView_amount = (TextView) findViewById(R.id.textView_amount);

        DataService dataService = new DataService();
        List<DataEntity> dataEntityList = dataService.getDataEntityList(this);
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
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

}
