package com.qinggan.mybookkeepingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.qinggan.mybookkeepingapplication.model.Record;
import com.qinggan.mybookkeepingapplication.utils.DBUtil;

public class ListActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.fab).setOnClickListener(this);
        recyclerView = findViewById(R.id.recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        for (Record record : DBUtil.getInstance().loadAllRecord()) {
            Log.w("CJM", "Record:" + record);
        }
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


                break;
            case R.id.fix:
                startActivityWithType(DetailActivity.TYPE_FIX,2);
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

    public void startActivityWithType(int type) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.TYPE_KEY, type);
        startActivity(intent);
    }

    public void startActivityWithType(int type, long id) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.TYPE_KEY, type);
        intent.putExtra(DetailActivity.ID_KEY, id);
        startActivity(intent);
    }
}
