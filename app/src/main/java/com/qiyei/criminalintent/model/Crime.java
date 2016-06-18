package com.qiyei.criminalintent.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by daner on 2016/6/10.
 * 1273482124@qq.com
 */
public class Crime {

    private String mTitle;
    private String mDetail;
    private UUID mId;//通用识别码
    private Date mDate;
    private boolean mSolved;
    private String mSuspect;//嫌疑犯

    public Crime(){
        //生成通用识别码
        mId = UUID.randomUUID();
        mDate = new Date();//当前日期

    }

    public Crime(UUID id){
        mId = id;
        mDate = new Date();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public UUID getId() {
        return mId;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public String getPhotoFilename(){
        return "IMG_" + getId().toString() + ".jpg";
    }

    public String getDetail() {
        return mDetail;
    }

    public void setDetail(String detail) {
        mDetail = detail;
    }
}
