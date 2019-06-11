package com.qinggan.mybookkeepingapplication;

import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.qinggan.mybookkeepingapplication.model.Record;
import com.qinggan.mybookkeepingapplication.utils.CalculationUtil;
import com.qinggan.mybookkeepingapplication.utils.DBUtil;
import com.qinggan.mybookkeepingapplication.views.MyCheckBox;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, MyCheckBox.OnCheckedChangeListener {

    public static final String TYPE_KEY = "TYPE", ID_KEY = "ID";

    public static final int TYPE_ADD = 0, TYPE_FIX = 1, TYPE_DETAIL = 2;


    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private TextView data, percapita, commit, delete;

    private EditText nameEdit, spendEdit;

    private MyCheckBox checkbox;

    private View fixLayout;

    private CheckBox settled;

    private int type = -1;

    private long id = -1, dataLong;

    private float spend;

    private Record record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        fixLayout = findViewById(R.id.fixLayout);
        settled = findViewById(R.id.settled);
        data = findViewById(R.id.data);
        percapita = findViewById(R.id.percapita);
        commit = findViewById(R.id.commit);
        delete = findViewById(R.id.delete);
        nameEdit = findViewById(R.id.name);
        spendEdit = findViewById(R.id.spend);
        checkbox = findViewById(R.id.checkbox);

        commit.setOnClickListener(this);
        delete.setOnClickListener(this);
        spendEdit.addTextChangedListener(this);
        checkbox.setOnCheckedChangeListener(this);

        id = getIntent().getLongExtra(ID_KEY, -1);
        type = getIntent().getIntExtra(TYPE_KEY, -1);

        switch (type) {
            case TYPE_ADD:
                initAdd();
                break;
            case TYPE_FIX:
                initFix();
                break;
            case TYPE_DETAIL:
                initDetail();
                break;
            default:
                finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initAdd() {
        setTitle(R.string.title_add);
        commit.setText(R.string.btn_add);
        dataLong = System.currentTimeMillis();
        data.setText(simpleDateFormat.format(new Date(dataLong)));

    }

    private void initFix() {
        setTitle(R.string.title_fix);
        commit.setText(R.string.btn_fix);
        fixLayout.setVisibility(View.VISIBLE);

        record = DBUtil.getInstance().loadRecordById(id);
        if (record == null) {
            finish();
            return;
        }

        dataLong = record.getDate();

        data.setText(simpleDateFormat.format(new Date(dataLong)));
        nameEdit.setText(record.getName());
        spendEdit.setText(String.valueOf(record.getSpend()));
        checkbox.setSelectedMember(record.getMembers());
        settled.setChecked(record.getIsSettled());
    }

    private void initDetail() {
        setTitle(R.string.title_detail);
        commit.setVisibility(View.GONE);
        nameEdit.setEnabled(false);
        spendEdit.setEnabled(false);
        checkbox.setEnabled(false);

        record = DBUtil.getInstance().loadRecordById(id);
        if (record == null) {
            finish();
            return;
        }

        dataLong = record.getDate();

        data.setText(simpleDateFormat.format(new Date(dataLong)));
        nameEdit.setText(record.getName());
        spendEdit.setText(String.valueOf(record.getSpend()));
        checkbox.setSelectedMember(record.getMembers());
        settled.setChecked(record.getIsSettled());
    }

    private void setAverageText(String totalStr) {
        try {
            spend = Float.valueOf(totalStr);
            int checkedNum = checkbox.getCheckedNumber();
            if (checkedNum > 0) {
                percapita.setText(String.format(getString(R.string.percapita), CalculationUtil.getInstance().getAverageSpend(spend, checkedNum)));
            }
        } catch (Exception ignore) {
            percapita.setText("");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        setAverageText(s.toString());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit:
                if (TextUtils.isEmpty(nameEdit.getText().toString())) {
                    Snackbar.make(v, String.format(getString(R.string.not_empty), nameEdit.getHint().toString()), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(spendEdit.getText().toString())) {
                    Snackbar.make(v, String.format(getString(R.string.not_empty), spendEdit.getHint().toString()), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                switch (type) {
                    case TYPE_ADD:
                        record = new Record();
                        record.setDate(dataLong);
                        record.setName(nameEdit.getText().toString());
                        record.setSpend(spend);
                        record.setMembers(checkbox.getSelectedMember());
                        record.setIsSettled(settled.isChecked());
                        DBUtil.getInstance().insertRecord(record);
                        finish();
                        break;
                    case TYPE_FIX:
                        if (record != null) {
                            record.setDate(dataLong);
                            record.setName(nameEdit.getText().toString());
                            record.setSpend(spend);
                            record.setMembers(checkbox.getSelectedMember());
                            record.setIsSettled(settled.isChecked());
                            DBUtil.getInstance().updateRecord(record);
                        }
                        finish();
                        break;
                }
                break;
            case R.id.delete:
                if (record != null)
                    DBUtil.getInstance().deleteRecord(record);
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(int position, boolean isChecked) {
        setAverageText(spendEdit.getText().toString());
    }
}
