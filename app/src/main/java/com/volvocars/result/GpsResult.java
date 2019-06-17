package com.volvocars.result;

import android.content.Context;
import android.util.Log;

import com.volvocars.model.CarCondition;

public class GpsResult implements IResults {
    private static final String TAG = "GPS Result";
    private static final int RELATION = CarCondition.NO;

    private Context mContext;
    private String mDescription;
    private int mMsgType;

    public GpsResult(Context context){
        mContext = context;
    }


    @Override
    public int getRelation() {
        return RELATION;
    }

    @Override
    public void setDescription(String description) {
        mDescription = description;
    }

    @Override
    public String getDescription() {
        return mDescription;
    }

    @Override
    public void setMsgType(int msgType) {
        mMsgType = msgType;
    }

    @Override
    public int getMsgType() {
        return mMsgType;
    }

    @Override
    public int startAction() {
        Log.d(TAG, "gps is running");
        return 0;
    }

    @Override
    public int stopAction() {
        Log.d(TAG, "gps stopped");
        return 0;
    }
}
