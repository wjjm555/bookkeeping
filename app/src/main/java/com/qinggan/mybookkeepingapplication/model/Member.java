package com.qinggan.mybookkeepingapplication.model;

import com.qinggan.mybookkeepingapplication.utils.CalculationUtil;

public class Member {

    private int id;

    private String name;

    private float spend;


    private float advancePayment;

    public Member(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getSpend() {
        return spend;
    }

    public void setSpend(float spend) {
        this.spend = spend;
    }

    public float getAdvancePayment() {
        return advancePayment;
    }

    public void setAdvancePayment(float advancePayment) {
        this.advancePayment = advancePayment;
    }

    public void addAdvancePayment(float add) {
        this.advancePayment = CalculationUtil.getInstance().add(advancePayment, add);
    }

    public void addSpend(float add) {
        this.spend = CalculationUtil.getInstance().add(spend, add);
    }
}
