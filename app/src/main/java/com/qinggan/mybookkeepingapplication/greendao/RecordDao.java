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
        public final static Property Type = new Property(2, int.class, "type", false, "TYPE");
        public final static Property Spend = new Property(3, float.class, "spend", false, "SPEND");
        public final static Property Name = new Property(4, String.class, "name", false, "NAME");
        public final static Property IsSettled = new Property(5, boolean.class, "isSettled", false, "IS_SETTLED");
        public final static Property Members = new Property(6, String.class, "members", false, "MEMBERS");
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
                "\"TYPE\" INTEGER NOT NULL ," + // 2: type
                "\"SPEND\" REAL NOT NULL ," + // 3: spend
                "\"NAME\" TEXT," + // 4: name
                "\"IS_SETTLED\" INTEGER NOT NULL ," + // 5: isSettled
                "\"MEMBERS\" TEXT);"); // 6: members
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "date ON \"RECORD\"" +
                " (\"DATE\" ASC);");
        db.execSQL("CREATE INDEX " + constraint + "type ON \"RECORD\"" +
                " (\"TYPE\" ASC);");
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
        stmt.bindLong(3, entity.getType());
        stmt.bindDouble(4, entity.getSpend());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(5, name);
        }
        stmt.bindLong(6, entity.getIsSettled() ? 1L: 0L);
 
        List members = entity.getMembers();
        if (members != null) {
            stmt.bindString(7, membersConverter.convertToDatabaseValue(members));
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
        stmt.bindLong(3, entity.getType());
        stmt.bindDouble(4, entity.getSpend());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(5, name);
        }
        stmt.bindLong(6, entity.getIsSettled() ? 1L: 0L);
 
        List members = entity.getMembers();
        if (members != null) {
            stmt.bindString(7, membersConverter.convertToDatabaseValue(members));
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
            cursor.getInt(offset + 2), // type
            cursor.getFloat(offset + 3), // spend
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // name
            cursor.getShort(offset + 5) != 0, // isSettled
            cursor.isNull(offset + 6) ? null : membersConverter.convertToEntityProperty(cursor.getString(offset + 6)) // members
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Record entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDate(cursor.getLong(offset + 1));
        entity.setType(cursor.getInt(offset + 2));
        entity.setSpend(cursor.getFloat(offset + 3));
        entity.setName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setIsSettled(cursor.getShort(offset + 5) != 0);
        entity.setMembers(cursor.isNull(offset + 6) ? null : membersConverter.convertToEntityProperty(cursor.getString(offset + 6)));
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
