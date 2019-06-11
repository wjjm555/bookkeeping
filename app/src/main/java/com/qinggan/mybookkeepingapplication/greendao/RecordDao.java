package com.qinggan.mybookkeepingapplication.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.qinggan.mybookkeepingapplication.model.MemberConvert;
import java.util.List;

import com.qinggan.mybookkeepingapplication.model.Record;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "RECORD".
*/
public class RecordDao extends AbstractDao<Record, Long> {

    public static final String TABLENAME = "RECORD";

    /**
     * Properties of entity Record.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Date = new Property(1, long.class, "date", false, "DATE");
        public final static Property Spend = new Property(2, float.class, "spend", false, "SPEND");
        public final static Property Name = new Property(3, String.class, "name", false, "NAME");
        public final static Property IsSettled = new Property(4, boolean.class, "isSettled", false, "IS_SETTLED");
        public final static Property Members = new Property(5, String.class, "members", false, "MEMBERS");
    }

    private final MemberConvert membersConverter = new MemberConvert();

    public RecordDao(DaoConfig config) {
        super(config);
    }
    
    public RecordDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"RECORD\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"DATE\" INTEGER NOT NULL ," + // 1: date
                "\"SPEND\" REAL NOT NULL ," + // 2: spend
                "\"NAME\" TEXT," + // 3: name
                "\"IS_SETTLED\" INTEGER NOT NULL ," + // 4: isSettled
                "\"MEMBERS\" TEXT);"); // 5: members
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "date ON \"RECORD\"" +
                " (\"DATE\" ASC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"RECORD\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Record entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getDate());
        stmt.bindDouble(3, entity.getSpend());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
        stmt.bindLong(5, entity.getIsSettled() ? 1L: 0L);
 
        List members = entity.getMembers();
        if (members != null) {
            stmt.bindString(6, membersConverter.convertToDatabaseValue(members));
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Record entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getDate());
        stmt.bindDouble(3, entity.getSpend());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
        stmt.bindLong(5, entity.getIsSettled() ? 1L: 0L);
 
        List members = entity.getMembers();
        if (members != null) {
            stmt.bindString(6, membersConverter.convertToDatabaseValue(members));
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Record readEntity(Cursor cursor, int offset) {
        Record entity = new Record( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // date
            cursor.getFloat(offset + 2), // spend
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // name
            cursor.getShort(offset + 4) != 0, // isSettled
            cursor.isNull(offset + 5) ? null : membersConverter.convertToEntityProperty(cursor.getString(offset + 5)) // members
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Record entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDate(cursor.getLong(offset + 1));
        entity.setSpend(cursor.getFloat(offset + 2));
        entity.setName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setIsSettled(cursor.getShort(offset + 4) != 0);
        entity.setMembers(cursor.isNull(offset + 5) ? null : membersConverter.convertToEntityProperty(cursor.getString(offset + 5)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Record entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Record entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Record entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}