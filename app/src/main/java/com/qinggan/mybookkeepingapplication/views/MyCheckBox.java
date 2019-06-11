package com.qinggan.mybookkeepingapplication.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

import com.qinggan.mybookkeepingapplication.R;
import com.qinggan.mybookkeepingapplication.model.Member;
import com.qinggan.mybookkeepingapplication.utils.MemberUtil;

import java.util.ArrayList;
import java.util.List;

public class MyCheckBox extends FrameLayout implements CompoundButton.OnCheckedChangeListener {

    private List<CheckBox> boxes = new ArrayList<>();

    private final int[] ids = {R.id.boxOne, R.id.boxTwo, R.id.boxThree, R.id.boxFour, R.id.boxFive, R.id.boxSix, R.id.boxSeven};


    private OnCheckedChangeListener mOnCheckedChangeListener;

    public MyCheckBox(@NonNull Context context) {
        this(context, null);
    }

    public MyCheckBox(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCheckBox(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_check_box, this);

        for (int id : ids) {
            boxes.add((CheckBox) findViewById(id));
        }
        for (int i = 0; i < boxes.size(); ++i) {
            Member member = MemberUtil.getInstance().getMemberList().get(i);
            CheckBox checkBox = boxes.get(i);
            checkBox.setText(member.getName());
            checkBox.setOnCheckedChangeListener(this);
        }
    }

    private boolean isAllUnchecked() {
        for (CheckBox box : boxes) {
            if (box.isChecked()) return false;
        }
        return true;
    }

    public List<CheckBox> getBoxes() {
        return boxes;
    }

    public int getCheckedNumber() {
        int number = 0;
        for (CheckBox box : boxes) {
            if (box.isChecked()) ++number;
        }
        return number;
    }

    public void setSelectedMember(List<Integer> members) {
        if (members != null && members.size() > 0)
            for (int i = 0; i < boxes.size(); ++i) {
                CheckBox box = boxes.get(i);
                box.setChecked(members.contains(i));
            }
    }

    public List<Integer> getSelectedMember() {
        List<Integer> members = new ArrayList<>();
        for (int i = 0; i < boxes.size(); ++i) {
            CheckBox box = boxes.get(i);
            if (box.isChecked()) members.add(i);
        }
        return members;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (CheckBox box : boxes) {
            box.setEnabled(enabled);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked && isAllUnchecked()) {
            buttonView.setChecked(true);
            Snackbar.make(this, R.string.tips_less_than_one, Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (mOnCheckedChangeListener != null)
            mOnCheckedChangeListener.onCheckedChanged(boxes.indexOf(buttonView), isChecked);
    }

    public OnCheckedChangeListener getOnCheckedChangeListener() {
        return mOnCheckedChangeListener;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener mOnCheckedChangeListener) {
        this.mOnCheckedChangeListener = mOnCheckedChangeListener;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(int position, boolean isChecked);
    }
}
