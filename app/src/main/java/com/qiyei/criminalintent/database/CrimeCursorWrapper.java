package com.qiyei.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.qiyei.criminalintent.database.CrimeDbSchema.CrimeTable;
import com.qiyei.criminalintent.model.Crime;

import java.util.Date;
import java.util.UUID;

/**
 * Created by qiyei on 2016/6/14.
 */
public class CrimeCursorWrapper extends CursorWrapper {

    public CrimeCursorWrapper(Cursor cursor){
        super(cursor);
    }

    /**
     * 返回查询得到的Crime对象
     * @return
     */
    public Crime getCrime(){
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        String date = getString(getColumnIndex(CrimeTable.Cols.DATE));
        String solved = getString(getColumnIndex(CrimeTable.Cols.SOLVED));
        String suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECT));
        String detail = getString(getColumnIndex(CrimeTable.Cols.DETAIL));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setDate(new Date(date));
        crime.setTitle(title);
        crime.setSolved(solved.equals("1"));
        crime.setSuspect(suspect);
        crime.setDetail(detail);

        return crime;
    }

}
