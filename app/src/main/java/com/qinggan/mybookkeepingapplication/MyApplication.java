package com.qinggan.mybookkeepingapplication;

import android.app.Application;

import com.qinggan.mybookkeepingapplication.utils.DBUtil;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DBUtil.getInstance().initDB(this);
    }
}
