package com.qiyei.criminalintent.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.qiyei.criminalintent.database.CrimeCursorWrapper;
import com.qiyei.criminalintent.database.CrimeDbSchema.CrimeTable;
import com.qiyei.criminalintent.database.CrimeOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by daner on 2016/6/11.
 * 1273482124@qq.com
 */
public class CrimeLab {

    private static CrimeLab sCrimeLab;
    private Context mContext;

    private SQLiteDatabase mSQLiteDatabase;

    private CrimeLab(Context context){

        mContext = context.getApplicationContext();
        mSQLiteDatabase = new CrimeOpenHelper(mContext).getWritableDatabase();
    }


    /**
     * DCL 方式获取单例
     * @param context
     * @return
     */
    public static CrimeLab getInstance(Context context){

        if (sCrimeLab == null){
            synchronized (CrimeLab.class){
                if (sCrimeLab == null){
                    sCrimeLab = new CrimeLab(context);
                }
            }
        }
        return sCrimeLab;
    }

    /**
     * 根据crime返回图片文件
     * @param crime
     * @return
     */
    public File getPhotoFile(Crime crime){
        File externalFileDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFileDir == null){
            return null;
        }

        return new File(externalFileDir,crime.getPhotoFilename());

    }

    /**
     * 返回CrimeList
     * @return
     */
    public List<Crime> getCrimeList() {
        List<Crime> crimeList = new ArrayList<>();

        //查询所有的crime对象
        CrimeCursorWrapper wrapper = queryCrime(null,null);

        try{
            wrapper.moveToFirst();
            while (!wrapper.isAfterLast()){
                crimeList.add(wrapper.getCrime());
                wrapper.moveToNext();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            wrapper.close();
        }

        return crimeList;
    }

    /**
     * 根据id返回mCrimeList对应的Crime对象
     * @param id
     * @return
     */
    public Crime getCrime(UUID id){

        String uuidString = id.toString();

        CrimeCursorWrapper wrapper = queryCrime(CrimeTable.Cols.UUID + " = ?",new String[]{uuidString});
        Crime crime = null;
        try {
            if (wrapper.getCount() == 0){
                return null;
            }

            wrapper.moveToFirst();
            crime = wrapper.getCrime();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            wrapper.close();
        }

        return crime;
    }

    /**
     * 更新Crime
     * @param crime
     */
    public void updateCrime(Crime crime){
        String id = crime.getId().toString();
        ContentValues values = getContentValues(crime);
        mSQLiteDatabase.update(CrimeTable.NAME,values,CrimeTable.Cols.UUID + "= ?",new String[]{id});
    }

    /**
     * 增加Crime
     * @param crime
     */
    public void add(Crime crime){
        mSQLiteDatabase.insert(CrimeTable.NAME,null,getContentValues(crime));
    }


    /**
     * 删除Crime
     * @param crime
     */
    public void remove(Crime crime){
        String id = crime.getId().toString();
        mSQLiteDatabase.delete(CrimeTable.NAME,CrimeTable.Cols.UUID + " = ?",new String[]{id});

    }

    /**
     * 根据Crime返回可供数据库操作的ContentValues对象
     * @param crime
     * @return
     */
    private ContentValues getContentValues(Crime crime){

        ContentValues values = new ContentValues();

        values.put(CrimeTable.Cols.UUID,crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE,crime.getTitle());
        values.put(CrimeTable.Cols.DATE,crime.getDate().toString());
        values.put(CrimeTable.Cols.SOLVED,crime.isSolved() ? 1:0);
        values.put(CrimeTable.Cols.SUSPECT,crime.getSuspect());
        values.put(CrimeTable.Cols.DETAIL,crime.getDetail());

        return values;
    }

    /**
     * 获取从数据库查询的游标
     * @param selection
     * @param selectionArgs
     * @return
     */
    private CrimeCursorWrapper queryCrime(String selection, String[] selectionArgs){
        Cursor cursor = mSQLiteDatabase.query(CrimeTable.NAME,null,selection,selectionArgs,null
                ,null,null);
        return new CrimeCursorWrapper(cursor);
    }

}
