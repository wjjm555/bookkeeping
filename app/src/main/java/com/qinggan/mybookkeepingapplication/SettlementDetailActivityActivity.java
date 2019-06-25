package com.qinggan.mybookkeepingapplication;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.qinggan.mybookkeepingapplication.adapters.DetailAdapter;
import com.qinggan.mybookkeepingapplication.model.Member;
import com.qinggan.mybookkeepingapplication.model.Record;
import com.qinggan.mybookkeepingapplication.utils.CalculationUtil;
import com.qinggan.mybookkeepingapplication.utils.MemberUtil;

import java.util.List;

public class SettlementDetailActivityActivity extends AppCompatActivity {

    private final int SPANCOUNT = 2;

    private float total;

    private TextView totalText;
    private RecyclerView averageRecycler, advanceRecycler, collectionsRecycler, paymentRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement_detail_activity);
        if (getIntent() != null)
            total = getIntent().getFloatExtra("total", -1);
        if (total < 0) {
            finish();
            return;
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(R.string.title_settlement_detail);

        totalText = findViewById(R.id.total);
        averageRecycler = findViewById(R.id.averageRecycler);
        advanceRecycler = findViewById(R.id.advanceRecycler);
        collectionsRecycler = findViewById(R.id.collectionsRecycler);
        paymentRecycler = findViewById(R.id.paymentRecycler);

        totalText.setText(String.format(getString(R.string.total), total));

        averageRecycler.setLayoutManager(new GridLayoutManager(this, SPANCOUNT, GridLayoutManager.VERTICAL, false));
        advanceRecycler.setLayoutManager(new GridLayoutManager(this, SPANCOUNT, GridLayoutManager.VERTICAL, false));
        collectionsRecycler.setLayoutManager(new GridLayoutManager(this, SPANCOUNT, GridLayoutManager.VERTICAL, false));
        paymentRecycler.setLayoutManager(new GridLayoutManager(this, SPANCOUNT, GridLayoutManager.VERTICAL, false));

        averageRecycler.setAdapter(new DetailAdapter(DetailAdapter.TYPE.AVERAGE));
        advanceRecycler.setAdapter(new DetailAdapter(DetailAdapter.TYPE.ADVANCE));
        collectionsRecycler.setAdapter(new DetailAdapter(DetailAdapter.TYPE.COLLECTIONS));
        paymentRecycler.setAdapter(new DetailAdapter(DetailAdapter.TYPE.PAYMENT));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return true;
    }

}
