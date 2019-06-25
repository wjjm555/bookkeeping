package com.qinggan.mybookkeepingapplication;

import android.app.Application;

import com.qinggan.mybookkeepingapplication.utils.DBUtil;

public class MyApplication extends Application {

    public static final String PASSWORD = "147258369";

    @Override
    public void onCreate() {
        super.onCreate();
        DBUtil.getInstance().initDB(this);
    }
}
