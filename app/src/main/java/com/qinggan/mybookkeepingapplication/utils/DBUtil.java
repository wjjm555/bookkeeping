package com.qinggan.mybookkeepingapplication.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.qinggan.mybookkeepingapplication.greendao.DaoMaster;
import com.qinggan.mybookkeepingapplication.greendao.DaoSession;
import com.qinggan.mybookkeepingapplication.greendao.RecordDao;
import com.qinggan.mybookkeepingapplication.model.Record;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class DBUtil {

    private final String DB_NAME = "Book_Keeping";

    private static class InnerHolder {
        private static final DBUtil INSTANCE = new DBUtil();
    }

    public static DBUtil getInstance() {
        return DBUtil.InnerHolder.INSTANCE;
    }

    private DaoSession daoSession;

    public void initDB(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public void insertRecord(Record record) {
        daoSession.insert(record);
    }

    public void deleteRecord(Record record) {
        daoSession.delete(record);
    }

    public void updateRecord(Record record) {
        daoSession.update(record);
    }

    public List<Record> loadAllRecord() {
        return daoSession.loadAll(Record.class);
    }

    public Record loadRecordById(long id) {
        QueryBuilder<Record> qb = daoSession.queryBuilder(Record.class);
        QueryBuilder<Record> recordQueryBuilder = qb.where(RecordDao.Properties.Id.eq(id));
        List<Record> records = recordQueryBuilder.list();
        if (records.size() > 0) return records.get(0);
        return null;
    }

}
