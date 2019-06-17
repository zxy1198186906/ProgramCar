package com.volvocars.model;

import android.content.Context;

public class CarNotice {

    private Context mContext;

    private String noticeName;
    private int[] noticeImageRes;

    public CarNotice(Context context, String name, int[] image){
        this.mContext = context;
        this.noticeName = name;
        this.noticeImageRes = image;
    }

    public String getNoticeName(){
        return this.noticeName;
    }


    public int[] getNoticeImage(){
        return this.noticeImageRes;
    }

}
