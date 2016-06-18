package com.qiyei.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by qiyei on 2016/6/14.
 */
public class CrimeOpenHelper extends SQLiteOpenHelper {

    private static final String NAME = "crime.db";
    private static final int VERSION = 1;

    private static final String CREATE_TABLE = "create table " + CrimeDbSchema.CrimeTable.NAME + " ( "
            + "id integer primary key autoincrement, "
            + CrimeDbSchema.CrimeTable.Cols.UUID + " text, "
            + CrimeDbSchema.CrimeTable.Cols.TITLE + " text, "
            + CrimeDbSchema.CrimeTable.Cols.DETAIL + " text,"
            + CrimeDbSchema.CrimeTable.Cols.DATE + " text, "
            + CrimeDbSchema.CrimeTable.Cols.SOLVED + " text, "
            + CrimeDbSchema.CrimeTable.Cols.SUSPECT + " text "
            +")";


    public CrimeOpenHelper(Context context){
        super(context,NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
