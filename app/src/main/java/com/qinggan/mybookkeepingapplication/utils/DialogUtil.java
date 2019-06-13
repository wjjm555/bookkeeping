package com.qinggan.mybookkeepingapplication.utils;

import android.content.Context;
import android.view.View;

import com.qinggan.mybookkeepingapplication.R;
import com.rey.material.app.DatePickerDialog;

import java.util.Calendar;

public class DialogUtil {

    private DatePickerDialog datePickerDialog;

    private static class InnerHolder {
        private static final DialogUtil INSTANCE = new DialogUtil();
    }

    public static DialogUtil getInstance() {
        return DialogUtil.InnerHolder.INSTANCE;
    }

    private DialogUtil() {

    }

    public void showDatePickerDialog(Context context, long date, final OnDateSelectedListener listener) {
        initDialog(context);
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

    private void initDialog(Context context) {
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
}
