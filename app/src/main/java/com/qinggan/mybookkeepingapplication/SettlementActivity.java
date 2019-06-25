package com.qinggan.mybookkeepingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.qinggan.mybookkeepingapplication.adapters.ListAdapter;
import com.qinggan.mybookkeepingapplication.model.Member;
import com.qinggan.mybookkeepingapplication.model.Record;
import com.qinggan.mybookkeepingapplication.utils.CalculationUtil;
import com.qinggan.mybookkeepingapplication.utils.DBUtil;
import com.qinggan.mybookkeepingapplication.utils.DialogUtil;
import com.qinggan.mybookkeepingapplication.utils.MemberUtil;
import com.qinggan.mybookkeepingapplication.views.MyRecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SettlementActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, MyRecyclerView.OnItemClickListener {

    private final int REQUESTCODE_DETAIL = 0x1001;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private TextView startText, endText;

    private MyRecyclerView recyclerView;

    private ListAdapter adapter;

    private TextView title, detail;

    private RadioButton all, settlementButton, nosettlementButton;

    private long startDate, endDate;

    private float total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(R.string.title_settlement);

        startText = findViewById(R.id.start);
        endText = findViewById(R.id.end);
        title = findViewById(R.id.title);
        all = findViewById(R.id.all);
        detail = findViewById(R.id.detail);
        settlementButton = findViewById(R.id.settlement);
        nosettlementButton = findViewById(R.id.nosettlement);

        all.setOnCheckedChangeListener(this);
        settlementButton.setOnCheckedChangeListener(this);
        nosettlementButton.setOnCheckedChangeListener(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter = new ListAdapter());
        recyclerView.setOnItemClickListener(this);

        startText.setOnClickListener(this);
        endText.setOnClickListener(this);
        detail.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();

        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        setStartText(calendar.getTimeInMillis());
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) + 1, 0, 0, 0);
        setEndText(calendar.getTimeInMillis());

        all.setChecked(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE_DETAIL && resultCode == RESULT_OK) {
            all.setChecked(true);
        }
    }

    private void setStartText(long date) {
        startDate = date;
        startText.setText(String.format(getString(R.string.settlement_start), simpleDateFormat.format(new Date(date))));
    }

    private void setEndText(long date) {
        endDate = date;
        endText.setText(String.format(getString(R.string.settlement_end), simpleDateFormat.format(new Date(date))));
    }

    private void showContent() {
        List<Record> records = adapter.getData();
        total = 0;
        if (records.size() > 0) {
            MemberUtil.getInstance().clearMemberSpend();
            for (Record record : records) {
                if (record != null && record.getMembers() != null) {
                    MemberUtil.getInstance().getMemberById(record.getAgent()).addAdvancePayment(record.getSpend());
                    total = CalculationUtil.getInstance().add(total, record.getSpend());
                    for (int memberId : record.getMembers()) {
                        MemberUtil.getInstance().getMemberById(memberId).addSpend(record.getAverage());

                    }
                }
            }
        }
        title.setText(String.format(getString(R.string.total), total));
    }

    private void setSettlement(final boolean settlement) {
        boolean changed = false;
        for (Record record : adapter.getData()) {
            if (record.getIsSettled() != settlement) {
                record.setIsSettled(settlement);
                changed = true;
            }
        }

        if (changed) {
            DBUtil.getInstance().updateRecords(adapter.getData(), new DBUtil.DBWriteListener() {
                @Override
                public void onWriteBack(boolean success) {
                    if (success)
                        if (settlement) settlementButton.setChecked(true);
                        else nosettlementButton.setChecked(true);
                }
            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settlement_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.settlement:
                DialogUtil.getInstance().showPasswordDialog(this, new DialogUtil.OnPasswordConfirmClickListener() {
                    @Override
                    public void onConfirmClick(String password) {
                        if (MyApplication.PASSWORD.toLowerCase().equals(password.trim().toLowerCase())) {
                            setSettlement(true);
                        } else {
                            Snackbar.make(startText, getString(R.string.passwd_error), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.reverse:
                DialogUtil.getInstance().showPasswordDialog(this, new DialogUtil.OnPasswordConfirmClickListener() {
                    @Override
                    public void onConfirmClick(String password) {
                        if (MyApplication.PASSWORD.toLowerCase().equals(password.trim().toLowerCase())) {
                            setSettlement(false);
                        } else {
                            Snackbar.make(startText, getString(R.string.passwd_error), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                DialogUtil.getInstance().showDatePickerDialog(this, startDate, new DialogUtil.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(long date) {
                        setStartText(date);
                        adapter.notifyDataSetChangedWithSection(startDate, endDate, new ListAdapter.NotifyListener() {
                            @Override
                            public void onNotifySuccess() {
                                showContent();
                            }
                        });
                    }
                });
                break;
            case R.id.end:
                DialogUtil.getInstance().showDatePickerDialog(this, endDate, new DialogUtil.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(long date) {
                        setEndText(date);
                        adapter.notifyDataSetChangedWithSection(startDate, endDate, new ListAdapter.NotifyListener() {
                            @Override
                            public void onNotifySuccess() {
                                showContent();
                            }
                        });
                    }
                });
                break;
            case R.id.detail:
                Intent intent = new Intent(this, SettlementDetailActivityActivity.class);
                intent.putExtra("total", total);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.all:
                if (isChecked) {
                    detail.setEnabled(false);
                    adapter.notifyDataSetChangedWithSection(startDate, endDate, new ListAdapter.NotifyListener() {
                        @Override
                        public void onNotifySuccess() {
                            showContent();
                        }
                    });
                }

                break;
            case R.id.settlement:
                if (isChecked) {
                    detail.setEnabled(false);
                    adapter.notifyDataSetChangedWithSection(startDate, endDate, true, new ListAdapter.NotifyListener() {
                        @Override
                        public void onNotifySuccess() {
                            showContent();
                        }
                    });
                }
                break;
            case R.id.nosettlement:
                if (isChecked) {
                    detail.setEnabled(true);
                    adapter.notifyDataSetChangedWithSection(startDate, endDate, false, new ListAdapter.NotifyListener() {
                        @Override
                        public void onNotifySuccess() {
                            showContent();
                        }
                    });
                }
                break;

        }

    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View item, int position) {
        Record record = adapter.getItem(position);
        if (record != null) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.TYPE_KEY, DetailActivity.TYPE_DETAIL);
            intent.putExtra(DetailActivity.ID_KEY, record.getId());
            startActivityForResult(intent, REQUESTCODE_DETAIL);
        }
    }

    @Override
    public void onItemLongClick(RecyclerView recyclerView, View item, int position) {

    }
}
