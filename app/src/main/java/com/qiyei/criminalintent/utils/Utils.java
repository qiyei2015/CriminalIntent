package com.qiyei.criminalintent.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;
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

        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;

        int sampleSize = 1;

        if (srcWidth > descWidth || srcHeight > descHeight){
            int halfWidth = srcWidth / 2;
            int halfHeight = srcHeight / 2;
            while (halfHeight/sampleSize > descHeight || halfWidth/sampleSize > descWidth ){
                sampleSize *= 2;
            }
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;

        int degree = readPictureDegree(file);
        Bitmap bitmap = BitmapFactory.decodeFile(file,options);

        return rotaingImageView(degree,bitmap);
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

        return getScaledBitmap(file ,point.x,point.y);
    }

    /**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    private static int readPictureDegree(String path) {
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();;
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

}
