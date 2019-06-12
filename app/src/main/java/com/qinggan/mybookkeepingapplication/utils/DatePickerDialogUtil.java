package com.qinggan.mybookkeepingapplication.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.qinggan.mybookkeepingapplication.R;
import com.rey.material.app.DatePickerDialog;

import java.util.Calendar;
import java.util.Date;

public class DatePickerDialogUtil {

    private DatePickerDialog datePickerDialog;

    private static class InnerHolder {
        private static final DatePickerDialogUtil INSTANCE = new DatePickerDialogUtil();
    }

    public static DatePickerDialogUtil getInstance() {
        return DatePickerDialogUtil.InnerHolder.INSTANCE;
    }

    private DatePickerDialogUtil() {

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

    public interface OnDateSelectedListener {
        void onDateSelected(long date);
    }
}
