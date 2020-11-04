package com.example.android_global_hw;

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


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_MARKER + "(" +
                KEY_ID + " integer primary key," +
                KEY_LINK + " text," +
                KEY_HEADER + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_MARKER );

        onCreate(db);
    }
}
