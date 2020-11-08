package com.example.android_global_hw;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "markerDB";
    public static final String TABLE_MARKER = "markers";

    public static final String KEY_ID = "_id";
    public static final String KEY_LINK = "link";
    public static final String KEY_HEADER = "header";
    public static final String KEY_DESCRIPTION = "description";
    //TODO add bitmap


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_MARKER + "(" +
                KEY_ID + " integer primary key," +
                KEY_LINK + " text," +
                KEY_HEADER + " text," +
                KEY_DESCRIPTION + " text" + ")");
        initializeDataBase(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_MARKER);
        onCreate(db);

    }

    public void updateTable(int markerId, String typeOfEditText, String newText) {
        System.out.println(markerId + " ID IS");
        ContentValues contentValues = new ContentValues();
        contentValues.put(typeOfEditText, newText);
        int update = getWritableDatabase().update(DBHelper.TABLE_MARKER, contentValues, KEY_ID + " = ?", new String[]{String.valueOf(markerId)});
        System.out.println("UPDATED " + update);
    }


    private void initializeDataBase(SQLiteDatabase db) {
        //TODO add description
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_LINK, "www.google.com");
        contentValues.put(DBHelper.KEY_HEADER, "it's google!");
        contentValues.put(DBHelper.KEY_DESCRIPTION, "1");
        db.insert(DBHelper.TABLE_MARKER, null, contentValues);

        contentValues.clear();
        contentValues.put(DBHelper.KEY_LINK, "www.yandex.ru");
        contentValues.put(DBHelper.KEY_HEADER, "it's yandex!");
        contentValues.put(DBHelper.KEY_DESCRIPTION, "2");
        db.insert(DBHelper.TABLE_MARKER, null, contentValues);

        contentValues.clear();
        contentValues.put(DBHelper.KEY_LINK, "www.google.com");
        contentValues.put(DBHelper.KEY_HEADER, "it's second google!");
        contentValues.put(DBHelper.KEY_DESCRIPTION, "3");
        db.insert(DBHelper.TABLE_MARKER, null, contentValues);

        contentValues.clear();
        contentValues.put(DBHelper.KEY_LINK, "www.youtube.com");
        contentValues.put(DBHelper.KEY_HEADER, "it's youtube!!");
        contentValues.put(DBHelper.KEY_DESCRIPTION, "4");
        db.insert(DBHelper.TABLE_MARKER, null, contentValues);
    }
}
