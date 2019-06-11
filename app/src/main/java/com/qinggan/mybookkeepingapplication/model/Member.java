package com.qinggan.mybookkeepingapplication.model;

public class Member {

    private int id;

    private String name;

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

}
