package com.qinggan.mybookkeepingapplication.utils;

import java.math.BigDecimal;

public class CalculationUtil {

    private static class InnerHolder {
        private static final CalculationUtil INSTANCE = new CalculationUtil();
    }

    public static CalculationUtil getInstance() {
        return CalculationUtil.InnerHolder.INSTANCE;
    }

    public float getAverageSpend(float total, int num) {
        if (num > 0) {
            float price = total / num;
            return (float) (Math.round(price * 100) / 100.00);
        }
        return total;
    }

    public float add(float a, float b) {
        BigDecimal ab = new BigDecimal(a);
        BigDecimal bb = new BigDecimal(b);
        return ab.add(bb).floatValue();
    }
}
