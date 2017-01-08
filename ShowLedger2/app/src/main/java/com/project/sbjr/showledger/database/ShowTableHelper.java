package com.project.sbjr.showledger.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by sbjr on 8/1/17.
 */

public class ShowTableHelper extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION = 1;
    private final static String DATABASE_NAME = "ShowLedger.db";

    public ShowTableHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTable(DatabaseContract.MOVIE_WATCHED_LIST_TABLE));
        db.execSQL(createTable(DatabaseContract.MOVIE_WISH_LIST_TABLE));
        db.execSQL(createTable(DatabaseContract.TV_SHOW_WATCHED_LIST_TABLE));
        db.execSQL(createTable(DatabaseContract.TV_SHOW_WISH_LIST_TABLE));
        db.execSQL(createTable(DatabaseContract.TV_SHOW_INCOMPLETE_LIST_TABLE));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(delteTable(DatabaseContract.MOVIE_WATCHED_LIST_TABLE));
        db.execSQL(delteTable(DatabaseContract.MOVIE_WISH_LIST_TABLE));
        db.execSQL(delteTable(DatabaseContract.TV_SHOW_WATCHED_LIST_TABLE));
        db.execSQL(delteTable(DatabaseContract.TV_SHOW_WISH_LIST_TABLE));
        db.execSQL(delteTable(DatabaseContract.TV_SHOW_INCOMPLETE_LIST_TABLE));
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        onUpgrade(db, oldVersion, newVersion);
    }

    private String createTable(String TableName) {
        return "CREATE TABLE " + TableName + " ("
                + DatabaseContract.TABLE_SHOW_ID + " INTEGER PRIMARY KEY,"
                + DatabaseContract.TABLE_SHOW_NAME + " TEXT)";
    }

    private String delteTable(String TableName) {
        return "DROP TABLE IF EXISTS " + TableName;
    }

    public long insertEntry(String TableName, int showID, String showName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.TABLE_SHOW_ID, showID);
        contentValues.put(DatabaseContract.TABLE_SHOW_NAME, showName);
        long rows = db.insert(TableName, null, contentValues);
        db.close();
        return rows;
    }

    public void insertEntryusingContentValues(String TableName, ContentValues contentValues) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TableName, null, contentValues);
        db.close();
    }

    public Cursor getShowIds(String TableName) {
        SQLiteDatabase db = getReadableDatabase();

        return db.query(
                true,
                TableName,
                new String[]{DatabaseContract.TABLE_SHOW_ID},
                null,
                null,
                null,
                null,
                null,
                null);
    }

    public ArrayList<String> getShowNames(String TableName) {
        ArrayList<String> showids = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                true,
                TableName,
                new String[]{DatabaseContract.TABLE_SHOW_NAME},
                null,
                null,
                null,
                null,
                null,
                null);

        int pos = cursor.getColumnIndex(DatabaseContract.TABLE_SHOW_NAME);
        while (cursor.moveToNext()) {
            String v = cursor.getString(pos);
            showids.add(v);
        }
        cursor.close();
        db.close();
        return showids;
    }

    public int deleteEntry(String TableName, int showId) {
        SQLiteDatabase db = getWritableDatabase();
        String id = Integer.toString(showId);
        int r = db.delete(TableName, DatabaseContract.TABLE_SHOW_ID + "?", new String[]{id});
        db.close();
        return r;
    }
}
