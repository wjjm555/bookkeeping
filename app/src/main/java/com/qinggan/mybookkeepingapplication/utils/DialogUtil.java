package com.qinggan.mybookkeepingapplication.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.qinggan.mybookkeepingapplication.R;
import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.Dialog;

import java.util.Calendar;

public class DialogUtil {

    private DatePickerDialog datePickerDialog;

    private final int[] AGENT_IDS = {R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven};

    private static class InnerHolder {
        private static final DialogUtil INSTANCE = new DialogUtil();
    }

    public static DialogUtil getInstance() {
        return DialogUtil.InnerHolder.INSTANCE;
    }

    private DialogUtil() {

    }

    public void showAgentDialog(Context context, int defaultValue, final OnAgentConfirmClickListener listener) {
        final View dialogAgentView = LayoutInflater.from(context).inflate(R.layout.dialog_agent, null);
        for (int i = 0; i < AGENT_IDS.length; ++i) {
            int id = AGENT_IDS[i];
            String name = MemberUtil.MembersName[i];
            RadioButton button = dialogAgentView.findViewById(id);
            button.setText(name);
            if (defaultValue == i)
                button.setChecked(true);
        }

        final Dialog agentDialog = new Dialog(context);
        agentDialog.title(R.string.dialog_agent_title)
                .positiveAction(R.string.confirm)
                .negativeAction(R.string.cancel)
                .cancelable(true)
                .canceledOnTouchOutside(true)
                .contentView(dialogAgentView)
                .positiveActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        agentDialog.dismiss();
                        int memberId = -1;
                        for (int i = 0; i < AGENT_IDS.length; ++i) {
                            int id = AGENT_IDS[i];
                            RadioButton button = dialogAgentView.findViewById(id);
                            if (button.isChecked()) {
                                memberId = i;
                                break;
                            }
                        }
                        if (memberId >= 0 && listener != null)
                            listener.onConfirmClick(memberId);
                    }
                })
                .negativeActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        agentDialog.dismiss();
                    }
                }).show();
    }

    public void showPasswordDialog(Context context, final OnPasswordConfirmClickListener listener) {
        View dialogInputView = LayoutInflater.from(context).inflate(R.layout.dialog_password, null);
        final EditText passwordEdit = dialogInputView.findViewById(R.id.edit);
        final Dialog passwordDialog = new Dialog(context);
        passwordDialog.title(R.string.dialog_title)
                .positiveAction(R.string.confirm)
                .negativeAction(R.string.cancel)
                .cancelable(true)
                .canceledOnTouchOutside(true)
                .contentView(dialogInputView)
                .positiveActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        passwordDialog.dismiss();
                        if (listener != null)
                            listener.onConfirmClick(passwordEdit.getText().toString());
                    }
                })
                .negativeActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        passwordDialog.dismiss();
                    }
                }).show();
    }

    public void showDatePickerDialog(Context context, long date, final OnDateSelectedListener listener) {
        initDatePickerDialog(context);
        datePickerDialog.date(date).positiveActionClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.dismiss();
                if (listener != null) listener.onDateSelected(datePickerDialog.getDate());
            }
        }).show();
    }

    public void showLoadingDialog() {

    }

    public void hideLoadingDialog() {

    }

    private void initDatePickerDialog(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR) - 10, 1, 1);
        long start = calendar.getTimeInMillis();
        calendar.set(calendar.get(Calendar.YEAR) + 20, 12, 31);
        long end = calendar.getTimeInMillis();

        datePickerDialog = new DatePickerDialog(context);
        datePickerDialog.dateRange(start, end)
                .positiveAction(R.string.confirm)
                .negativeAction(R.string.cancel)
                .cancelable(true)
                .canceledOnTouchOutside(true)
                .negativeActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePickerDialog.dismiss();
                    }
                });
    }


    public interface OnDateSelectedListener {
        void onDateSelected(long date);
    }

    public interface OnPasswordConfirmClickListener {
        void onConfirmClick(String password);
    }

    public interface OnAgentConfirmClickListener {
        void onConfirmClick(int id);
    }
}
