package com.qinggan.mybookkeepingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.qinggan.mybookkeepingapplication.adapters.ListAdapter;
import com.qinggan.mybookkeepingapplication.model.Record;
import com.qinggan.mybookkeepingapplication.utils.DBUtil;
import com.qinggan.mybookkeepingapplication.utils.MemberUtil;
import com.qinggan.mybookkeepingapplication.views.MyRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements View.OnClickListener, MyRecyclerView.OnItemClickListener {

    private MyRecyclerView recyclerView;

    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.fab).setOnClickListener(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter = new ListAdapter());
        recyclerView.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChangedByDB(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.statistics:
                startActivity(new Intent(this, SettlementActivity.class));
                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                startActivityWithType(DetailActivity.TYPE_ADD);
                break;
        }
    }

    private void startActivityWithType(int type) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.TYPE_KEY, type);
        startActivity(intent);
    }

    private void startActivityWithType(int type, long id) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.TYPE_KEY, type);
        intent.putExtra(DetailActivity.ID_KEY, id);
        startActivity(intent);
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View item, int position) {
        Record record = adapter.getItem(position);
        if (record != null)
            startActivityWithType(DetailActivity.TYPE_DETAIL, record.getId());
    }

    @Override
    public void onItemLongClick(RecyclerView recyclerView, View item, int position) {

    }
}
