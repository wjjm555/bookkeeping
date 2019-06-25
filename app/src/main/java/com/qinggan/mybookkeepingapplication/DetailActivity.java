package com.qinggan.mybookkeepingapplication;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.qinggan.mybookkeepingapplication.model.Record;
import com.qinggan.mybookkeepingapplication.utils.CalculationUtil;
import com.qinggan.mybookkeepingapplication.utils.DBUtil;
import com.qinggan.mybookkeepingapplication.utils.DialogUtil;
import com.qinggan.mybookkeepingapplication.utils.MemberUtil;
import com.qinggan.mybookkeepingapplication.views.MyCheckBox;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, MyCheckBox.OnCheckedChangeListener {

    public static final String TYPE_KEY = "TYPE", ID_KEY = "ID";

    public static final int TYPE_ADD = 0, TYPE_FIX = 1, TYPE_DETAIL = 2;

    protected final long DATERANGE = 1000 * 60 * 60 * 24 * 365;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private TextView date, percapita, commit, delete, settledText, agentText;

    private EditText nameEdit, spendEdit;

    private MyCheckBox checkbox;

    private View fixLayout;

    private CheckBox settled;

    private int type = -1, agent = -1;

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
        date = findViewById(R.id.date);
        percapita = findViewById(R.id.percapita);
        commit = findViewById(R.id.commit);
        delete = findViewById(R.id.delete);
        nameEdit = findViewById(R.id.name);
        spendEdit = findViewById(R.id.spend);
        checkbox = findViewById(R.id.checkbox);
        settledText = findViewById(R.id.settledText);
        agentText = findViewById(R.id.agent);

        commit.setOnClickListener(this);
        delete.setOnClickListener(this);
        date.setOnClickListener(this);
        agentText.setOnClickListener(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return type != TYPE_ADD;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.fix:
                if (type != TYPE_FIX)
                    DialogUtil.getInstance().showPasswordDialog(this, new DialogUtil.OnPasswordConfirmClickListener() {
                        @Override
                        public void onConfirmClick(String password) {
                            if (MyApplication.PASSWORD.toLowerCase().equals(password.trim().toLowerCase())) {
                                if (type != TYPE_FIX) {
                                    type = TYPE_FIX;
                                    initFix();
                                }
                            } else {
                                Snackbar.make(fixLayout, getString(R.string.passwd_error), Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                break;

        }
        return true;
    }

    private void initAdd() {
        setTitle(R.string.title_add);
        commit.setText(R.string.btn_add);
        setDate(System.currentTimeMillis());
        nameEdit.requestFocus();
    }

    private void initFix() {
        setTitle(R.string.title_fix);
        commit.setVisibility(View.VISIBLE);
        commit.setText(R.string.btn_fix);
        fixLayout.setVisibility(View.VISIBLE);
        settledText.setVisibility(View.GONE);
        percapita.setVisibility(View.GONE);
        nameEdit.setEnabled(true);
        spendEdit.setEnabled(true);
        checkbox.setEnabled(true);
        agentText.setEnabled(true);

        DBUtil.getInstance().loadRecordById(id, new DBUtil.DBReadListener<Record>() {

            @Override
            public void onReadBack(Record data) {
                record = data;
                if (record == null) {
                    finish();
                    return;
                }

                setDate(record.getDate());
                setAgent(record.getAgent());
                nameEdit.setText(record.getName());
                spendEdit.setText(String.valueOf(record.getSpend()));
                checkbox.setSelectedMember(record.getMembers());
                settled.setChecked(record.getIsSettled());
                nameEdit.requestFocus();
            }
        });
    }

    private void initDetail() {
        setTitle(R.string.title_detail);
        commit.setVisibility(View.GONE);
        settledText.setVisibility(View.VISIBLE);
        percapita.setVisibility(View.VISIBLE);
        nameEdit.setEnabled(false);
        spendEdit.setEnabled(false);
        checkbox.setEnabled(false);
        agentText.setEnabled(false);

        DBUtil.getInstance().loadRecordById(id, new DBUtil.DBReadListener<Record>() {

            @Override
            public void onReadBack(Record data) {
                record = data;
                if (record == null) {
                    finish();
                    return;
                }

                setDate(record.getDate());
                setAgent(record.getAgent());
                nameEdit.setText(record.getName());
                spendEdit.setText(String.valueOf(record.getSpend()));
                checkbox.setSelectedMember(record.getMembers());
                settled.setChecked(record.getIsSettled());
                settledText.setText(getString(record.getIsSettled() ? R.string.check_settled : R.string.check_not_settle));
            }
        });

    }

    private void setAgent(int agent) {
        this.agent = agent < 0 ? 0 : agent;
        if (agent >= 0 && agent < MemberUtil.getInstance().getMemberList().size())
            agentText.setText(String.format(getString(R.string.agent_detail), MemberUtil.getInstance().getMemberById(agent).getName()));
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

    private void setDate(long dateTime) {
        dataLong = dateTime;
        date.setText(simpleDateFormat.format(new Date(dateTime)));
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
                if (agent < 0) {
                    Snackbar.make(v, String.format(getString(R.string.not_empty), getString(R.string.agent)), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                switch (type) {
                    case TYPE_ADD:
                        record = new Record();
                        record.setDate(dataLong);
                        record.setName(nameEdit.getText().toString());
                        record.setSpend(spend);
                        record.setMembers(checkbox.getSelectedMember());
                        record.setIsSettled(false);
                        record.setAgent(agent);
                        DBUtil.getInstance().insertRecord(record, new DBUtil.DBWriteListener() {
                            @Override
                            public void onWriteBack(boolean success) {
                                setResult(RESULT_OK);
                                finish();
                            }
                        });
                        break;
                    case TYPE_FIX:
                        if (record != null) {
                            record.setDate(dataLong);
                            record.setName(nameEdit.getText().toString());
                            record.setSpend(spend);
                            record.setMembers(checkbox.getSelectedMember());
                            record.setIsSettled(settled.isChecked());
                            record.setAgent(agent);
                            DBUtil.getInstance().updateRecord(record, new DBUtil.DBWriteListener() {
                                @Override
                                public void onWriteBack(boolean success) {
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            });
                        }
                        break;
                }
                break;
            case R.id.delete:
                if (record != null)
                    DBUtil.getInstance().deleteRecord(record, new DBUtil.DBWriteListener() {
                        @Override
                        public void onWriteBack(boolean success) {
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
                break;
            case R.id.date:
                if (type != TYPE_DETAIL) {
                    DialogUtil.getInstance().showDatePickerDialog(this, dataLong, new DialogUtil.OnDateSelectedListener() {
                        @Override
                        public void onDateSelected(long date) {
                            setDate(date);
                        }
                    });
                }
                break;
            case R.id.agent:
                DialogUtil.getInstance().showAgentDialog(this, agent, new DialogUtil.OnAgentConfirmClickListener() {
                    @Override
                    public void onConfirmClick(int id) {
                        setAgent(id);
                    }
                });
                break;
        }
    }

    @Override
    public void onCheckedChanged(int position, boolean isChecked) {
        setAverageText(spendEdit.getText().toString());
    }
}
