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

    private List<Record> data = new ArrayList<>();

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
        return data.size();
    }

    public Record getItem(int position) {
        return data.get(position);
    }

    public void notifyDataSetChangedByDB() {
        data.clear();
        data.addAll(DBUtil.getInstance().loadAllRecord());
        super.notifyDataSetChanged();
    }

    public void notifyDataSetChangedWithSection(long start, long end) {
        data.clear();
        data.addAll(DBUtil.getInstance().loadRecordWithSection(start, end));
        super.notifyDataSetChanged();
    }

    public List<Record> getData() {
        return data;
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
}
