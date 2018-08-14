package com.google.firebase.quickstart.fcm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ToDoDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "alert_history.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE = "alerts";

    // поля таблицы
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIME = "recieve_time";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_MESSAGE= "message";

    // запрос на создание базы данных
    private static final String DATABASE_CREATE = "create table "
            + DATABASE_TABLE + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TIME
            + " text not null, " + COLUMN_TITLE + " text not null,"
            + COLUMN_MESSAGE + " text not null" + ");";

    public ToDoDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        Log.w(ToDoDatabase.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS todos");
        onCreate(db);
    }

    public long createNewAlert(String time, String title,
                              String message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = createContentValues(time, title,
                message);

        long row = db.insert(DATABASE_TABLE, null, initialValues);
        db.close();

        return row;
    }


    public Cursor getAllAlerts() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(DATABASE_TABLE, new String[] { COLUMN_ID,
                        COLUMN_TIME, COLUMN_TITLE, COLUMN_MESSAGE }, null,
                null, null, null, null);
    }

    public Cursor getAlert(long rowId) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(true, DATABASE_TABLE,
                new String[] { COLUMN_ID, COLUMN_TIME, COLUMN_TITLE,
                        COLUMN_MESSAGE }, COLUMN_ID + "=" + rowId, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    private ContentValues createContentValues(String time, String title,
                                              String message) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_MESSAGE, message);
        return values;
    }
}