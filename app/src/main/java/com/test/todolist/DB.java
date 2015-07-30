package com.test.todolist;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by FATAL1TY on 25.07.2015.
 */
public class DB {
    private static final String DB_NAME = "mydb";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "mytab";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TXT = "txt";
    public static final String COLUMN_STATUS = "status";

    private static final String DB_CREATE = "create table " + DB_TABLE + "(" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_TXT + " text, " +
            COLUMN_STATUS + " status" + ");";

    private final Context mCtx;

    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DB(Context ctx) {
        mCtx = ctx;
    }

    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    public void close() {
        if (mDBHelper != null)
            mDBHelper.close();
    }

    public Cursor getAllData() {
        return mDB.query(DB_TABLE, null, null, null, null, null, null);
    }

    public void addRec(String txt) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TXT, txt);
        cv.put(COLUMN_STATUS, "new");
        mDB.insert(DB_TABLE, null, cv);
    }

    public void delRec(Long id) {
        mDB.delete(DB_TABLE, COLUMN_ID + " = " + id, null);
    }

    public void editStatus(Long id, String txt) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(COLUMN_STATUS, txt);
        String where = COLUMN_ID + "=" + id;
        mDB.update(DB_TABLE, updatedValues, where, null);
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
