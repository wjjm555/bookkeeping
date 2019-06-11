package com.qinggan.mybookkeepingapplication.model;


import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Record {
    @Id(autoincrement = true)
    private Long id;

    @Index(name = "date", unique = true)
    private long date;

    private float spend;

    private String name;

    private boolean isSettled = false;

    @Convert(converter = MemberConvert.class, columnType = String.class)
    private List<Integer> members = new ArrayList<>();

    @Transient
    private float percapita;

    @Generated(hash = 639859102)
    public Record(Long id, long date, float spend, String name, boolean isSettled,
            List<Integer> members) {
        this.id = id;
        this.date = date;
        this.spend = spend;
        this.name = name;
        this.isSettled = isSettled;
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
                ", percapita='" + percapita + '\'' +
                ", members='" + members.toString() + '\'' +
                '}';
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

    public List<Integer> getMembers() {
        return this.members;
    }

    public void setMembers(List<Integer> members) {
        this.members = members;
    }
}
