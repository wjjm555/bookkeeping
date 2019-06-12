package com.qinggan.mybookkeepingapplication.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class MyRecyclerView extends RecyclerView implements View.OnClickListener, View.OnLongClickListener {

    private OnItemClickListener mOnItemClickListener;

    private boolean longClickAble = false;

    public MyRecyclerView(Context context) {
        this(context, null);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onChildAttachedToWindow(View child) {
        super.onChildAttachedToWindow(child);
        if (!child.hasOnClickListeners()) {
            child.setOnClickListener(this);
            child.setOnLongClickListener(this);
        }
    }

    @Override
    public void onClick(View itemView) {
        if (mOnItemClickListener != null)
            mOnItemClickListener.onItemClick(MyRecyclerView.this, itemView, getChildLayoutPosition(itemView));
    }

    @Override
    public boolean onLongClick(View itemView) {
        if (longClickAble && mOnItemClickListener != null)
            mOnItemClickListener.onItemLongClick(MyRecyclerView.this, itemView, getChildLayoutPosition(itemView));
        return longClickAble;
    }

    public void setLongClickAble(boolean longClickAble) {
        this.longClickAble = longClickAble;
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public interface OnItemClickListener {
        void onItemClick(RecyclerView recyclerView, View item, int position);

        void onItemLongClick(RecyclerView recyclerView, View item, int position);
    }

}
