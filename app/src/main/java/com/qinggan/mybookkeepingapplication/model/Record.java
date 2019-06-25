package com.qinggan.mybookkeepingapplication.model;


import com.qinggan.mybookkeepingapplication.utils.CalculationUtil;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Record {
    @Id(autoincrement = true)
    private Long id;

    @Index(name = "date")
    private long date;

    @Index(name = "type")
    private int type;

    private float spend;

    private String name;

    private boolean isSettled = false;

    private int agent;

    @Convert(converter = MemberConvert.class, columnType = String.class)
    private List<Integer> members = new ArrayList<>();

    @Transient
    private float average;

    @Generated(hash = 1719369227)
    public Record(Long id, long date, int type, float spend, String name, boolean isSettled, int agent,
            List<Integer> members) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.spend = spend;
        this.name = name;
        this.isSettled = isSettled;
        this.agent = agent;
        this.members = members;
    }

    @Generated(hash = 477726293)
    public Record() {
    }

    @Override
    public String toString() {
        return "Record{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", name='" + name + '\'' +
                ", spend='" + spend + '\'' +
                ", isSettled='" + isSettled + '\'' +
                ", agent='" + agent + '\'' +
                ", members='" + members.toString() + '\'' +
                '}';
    }

    public float getAverage() {
        if (average <= 0)
            average = CalculationUtil.getInstance().getAverageSpend(getSpend(), getMembers().size());
        return average;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDate() {
        return this.date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getSpend() {
        return this.spend;
    }

    public void setSpend(float spend) {
        this.spend = spend;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsSettled() {
        return this.isSettled;
    }

    public void setIsSettled(boolean isSettled) {
        this.isSettled = isSettled;
    }

    public int getAgent() {
        return this.agent;
    }

    public void setAgent(int agent) {
        this.agent = agent;
    }

    public List<Integer> getMembers() {
        return this.members;
    }

    public void setMembers(List<Integer> members) {
        this.members = members;
    }

   


}
