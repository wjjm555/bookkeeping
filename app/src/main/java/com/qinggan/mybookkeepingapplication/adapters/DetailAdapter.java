package com.qinggan.mybookkeepingapplication.adapters;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qinggan.mybookkeepingapplication.R;
import com.qinggan.mybookkeepingapplication.model.Member;
import com.qinggan.mybookkeepingapplication.utils.CalculationUtil;
import com.qinggan.mybookkeepingapplication.utils.MemberUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.Holder> {

    private int type;

    private List<Integer> memberIds;

    @IntDef({TYPE.AVERAGE, TYPE.ADVANCE, TYPE.COLLECTIONS, TYPE.PAYMENT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    public static class TYPE {
        public static final int AVERAGE = 0, ADVANCE = 1, COLLECTIONS = 2, PAYMENT = 3;
    }

    public DetailAdapter(@Type int type) {
        this.type = type;
        memberIds = new ArrayList<>();
        for (Member member : MemberUtil.getInstance().getMemberList()) {
            if (member != null)
                switch (type) {
                    case TYPE.AVERAGE:
                        if (member.getSpend() > 0)
                            memberIds.add(member.getId());
                        break;
                    case TYPE.ADVANCE:
                        if (member.getAdvancePayment() > 0)
                            memberIds.add(member.getId());
                        break;
                    case TYPE.COLLECTIONS:
                        if (member.getSpend() > member.getAdvancePayment())
                            memberIds.add(member.getId());
                        break;
                    case TYPE.PAYMENT:
                        if (member.getSpend() < member.getAdvancePayment())
                            memberIds.add(member.getId());
                        break;
                }
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Member member = MemberUtil.getInstance().getMemberById(memberIds.get(position));
        if (member != null)
            switch (type) {
                case TYPE.AVERAGE:
                    holder.content.setTextColor(holder.content.getContext().getResources().getColor(android.R.color.holo_purple));
                    holder.content.setText(String.format(holder.content.getContext().getResources().getString(R.string.everyone_spend), member.getName(), member.getSpend()));
                    break;
                case TYPE.ADVANCE:
                    holder.content.setTextColor(holder.content.getContext().getResources().getColor(android.R.color.holo_blue_dark));
                    holder.content.setText(String.format(holder.content.getContext().getResources().getString(R.string.everyone_spend), member.getName(), member.getAdvancePayment()));
                    break;
                case TYPE.COLLECTIONS:
                    holder.content.setTextColor(holder.content.getContext().getResources().getColor(R.color.colorAccent));
                    holder.content.setText(String.format(holder.content.getContext().getResources().getString(R.string.everyone_spend), member.getName(), CalculationUtil.getInstance().subtractAbs(member.getAdvancePayment(), member.getSpend())));
                    break;
                case TYPE.PAYMENT:
                    holder.content.setTextColor(holder.content.getContext().getResources().getColor(R.color.colorPrimary));
                    holder.content.setText(String.format(holder.content.getContext().getResources().getString(R.string.everyone_spend), member.getName(), CalculationUtil.getInstance().subtractAbs(member.getAdvancePayment(), member.getSpend())));
                    break;
            }
    }

    @Override
    public int getItemCount() {
        return memberIds.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView content;

        public Holder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
        }
    }
}
