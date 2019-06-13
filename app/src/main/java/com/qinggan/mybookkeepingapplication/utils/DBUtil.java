package com.qinggan.mybookkeepingapplication.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;

import com.qinggan.mybookkeepingapplication.greendao.DaoMaster;
import com.qinggan.mybookkeepingapplication.greendao.DaoSession;
import com.qinggan.mybookkeepingapplication.greendao.RecordDao;
import com.qinggan.mybookkeepingapplication.model.Record;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class DBUtil {

    private final String DB_NAME = "Book_Keeping";

    private Handler mHandler = new Handler(Looper.getMainLooper());

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

    public void insertRecord(final Record record, final DBWriteListener listener) {
        new Thread() {
            @Override
            public void run() {
                daoSession.insert(record);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null)
                            listener.onWriteBack(true);
                    }
                });
            }
        }.start();
    }

    public void deleteRecord(final Record record, final DBWriteListener listener) {
        new Thread() {
            @Override
            public void run() {
                daoSession.delete(record);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null)
                            listener.onWriteBack(true);
                    }
                });
            }
        }.start();
    }

    public void updateRecord(final Record record, final DBWriteListener listener) {
        new Thread() {
            @Override
            public void run() {
                daoSession.update(record);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null)
                            listener.onWriteBack(true);
                    }
                });
            }
        }.start();
    }

    public void updateRecords(final List<Record> records, final DBWriteListener listener) {
        new Thread() {
            @Override
            public void run() {
                for (Record record : records) {
                    daoSession.update(record);
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null)
                            listener.onWriteBack(true);
                    }
                });
            }
        }.start();
    }

    public void loadAllRecord(final DBReadListener listener) {
        new Thread() {
            @Override
            public void run() {
                final List<Record> records = daoSession.loadAll(Record.class);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null)
                            listener.onReadBack(records);
                    }
                });
            }
        }.start();
    }

    public void loadRecordWithSection(final long start, final long end, final DBReadListener listener) {
        new Thread() {
            @Override
            public void run() {
                QueryBuilder<Record> qb = daoSession.queryBuilder(Record.class);
                QueryBuilder<Record> recordQueryBuilder = qb.where(RecordDao.Properties.Date.between(start, end)).orderAsc(RecordDao.Properties.Date);
                final List<Record> records = recordQueryBuilder.list();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null)
                            listener.onReadBack(records);
                    }
                });
            }
        }.start();

    }

    public void loadRecordWithSection(final long start, final long end, final boolean settlement, final DBReadListener listener) {
        new Thread() {
            @Override
            public void run() {
                QueryBuilder<Record> qb = daoSession.queryBuilder(Record.class);
                QueryBuilder<Record> recordQueryBuilder = qb.where(RecordDao.Properties.Date.between(start, end), RecordDao.Properties.IsSettled.eq(settlement)).orderAsc(RecordDao.Properties.Date);
                final List<Record> records = recordQueryBuilder.list();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null)
                            listener.onReadBack(records);
                    }
                });
            }
        }.start();
    }

    public void loadRecordById(final long id, final DBReadListener listener) {
        new Thread() {
            @Override
            public void run() {
                QueryBuilder<Record> qb = daoSession.queryBuilder(Record.class);
                QueryBuilder<Record> recordQueryBuilder = qb.where(RecordDao.Properties.Id.eq(id));
                final List<Record> records = recordQueryBuilder.list();

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null && records != null && records.size() > 0)
                            listener.onReadBack(records.get(0));
                    }
                });
            }
        }.start();
    }

    public interface DBReadListener<T> {
        void onReadBack(T data);
    }

    public interface DBWriteListener {
        void onWriteBack(boolean success);
    }

}
