package com.qiyei.criminalintent.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by qiyei on 2016/6/13.
 */
public class Utils {

    /**
     * 格式化日期成字符串形式
     * @param date
     * @return
     */
    public static String dateFormatToString(Date date){
        //24小时制
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdformat.format(date);
    }


    /**
     * 获取缩放的bitmap
     * @param file
     * @param descWidth
     * @param descHeight
     * @return
     */
    public static Bitmap getScaledBitmap(String file , int descWidth , int descHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//不加载图片到内存，只是获取图片基本信息
        BitmapFactory.decodeFile(file,options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int sampleSize = 1;

        if (srcWidth > descWidth || srcHeight > descHeight){

            if (srcWidth > descWidth){
                sampleSize = Math.round(srcWidth / descWidth);
            } else {
                sampleSize = Math.round(srcHeight / descHeight);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;

        return BitmapFactory.decodeFile(file,options);
    }

    /**
     * 根据屏幕尺寸获取缩放的图片，这里是加载的图片的保守估计算法
     * @param file
     * @param activity
     * @return
     */
    public static Bitmap getScaledBitmap(String file , Activity activity){
        Point point = new Point();
        //获取屏幕大小，单位为像素
        activity.getWindowManager().getDefaultDisplay().getSize(point);
        Log.d("屏幕大小", "x = " + point.x + ", y = " + point.y);

        return getScaledBitmap(file , point.x , point.y);
    }

}
