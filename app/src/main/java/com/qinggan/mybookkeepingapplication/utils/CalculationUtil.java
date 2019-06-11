package com.qinggan.mybookkeepingapplication.utils;

import android.util.Log;

public class CalculationUtil {

    private static class InnerHolder {
        private static final CalculationUtil INSTANCE = new CalculationUtil();
    }

    public static CalculationUtil getInstance() {
        return CalculationUtil.InnerHolder.INSTANCE;
    }

    public float getAverageSpend(float total, int num) {
        float price = total / num;
        Log.w("CJM", "getAverageSpend:" + price);
        return (float) (Math.round(price * 100) / 100.00);
    }
}
