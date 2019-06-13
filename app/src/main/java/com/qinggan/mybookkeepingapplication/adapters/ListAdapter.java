package com.qinggan.mybookkeepingapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qinggan.mybookkeepingapplication.R;
import com.qinggan.mybookkeepingapplication.model.Record;
import com.qinggan.mybookkeepingapplication.utils.DBUtil;
import com.qinggan.mybookkeepingapplication.utils.MemberUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.Holder> {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private List<Record> records = new ArrayList<>();

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Record record = getItem(position);
        Log.w("CJM", "onBindViewHolder:" + record);
        if (record != null) {
            holder.title.setText(String.format(holder.title.getContext().getString(R.string.list_item_title), simpleDateFormat.format(new Date(record.getDate())), record.getName()));
            holder.total.setText(String.format(holder.total.getContext().getString(R.string.total), record.getSpend()));
            holder.settled.setText(holder.total.getContext().getString(record.getIsSettled() ? R.string.check_settled : R.string.check_not_settle));
            holder.average.setText(String.format(holder.total.getContext().getString(R.string.percapita), record.getAverage()));
            holder.members.setText(getMemberString(record.getMembers()));
            holder.settled.setTextColor(holder.total.getContext().getResources().getColor(record.getIsSettled() ? R.color.colorPrimary : R.color.colorAccent));
        }
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public Record getItem(int position) {
        return records.get(position);
    }

    public void notifyDataSetChangedByDB(final NotifyListener listener) {
        DBUtil.getInstance().loadAllRecord(new DBUtil.DBReadListener<List<Record>>() {
            @Override
            public void onReadBack(List<Record> data) {
                records.clear();
                records.addAll(data);
                ListAdapter.this.notifyDataSetChanged();
                if (listener != null) listener.onNotifySuccess();
            }
        });
    }

    public void notifyDataSetChangedWithSection(long start, long end, final NotifyListener listener) {
        DBUtil.getInstance().loadRecordWithSection(start, end, new DBUtil.DBReadListener<List<Record>>() {

            @Override
            public void onReadBack(List<Record> data) {
                records.clear();
                records.addAll(data);
                ListAdapter.this.notifyDataSetChanged();
                if (listener != null) listener.onNotifySuccess();
            }
        });

    }

    public void notifyDataSetChangedWithSection(long start, long end, boolean settlement, final NotifyListener listener) {
        DBUtil.getInstance().loadRecordWithSection(start, end, settlement, new DBUtil.DBReadListener<List<Record>>() {

            @Override
            public void onReadBack(List<Record> data) {
                records.clear();
                records.addAll(data);
                ListAdapter.this.notifyDataSetChanged();
                if (listener != null) listener.onNotifySuccess();
            }
        });
    }

    public List<Record> getData() {
        return records;
    }

    private String getMemberString(List<Integer> membersId) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer memberId : membersId) {
            stringBuilder.append(MemberUtil.getInstance().getMemberById(memberId).getName());
            stringBuilder.append("  ");
        }
        return stringBuilder.toString();
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView title, total, members, settled, average;

        public Holder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            total = itemView.findViewById(R.id.total);
            members = itemView.findViewById(R.id.members);
            settled = itemView.findViewById(R.id.settled);
            average = itemView.findViewById(R.id.average);
        }
    }


    public interface NotifyListener {

        void onNotifySuccess();

    }
}
