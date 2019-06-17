package com.volvocars.result;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.volvocars.broadCast.AlarmReceiver;
import com.volvocars.model.CarCondition;

public class ClockResult implements IResults {
    private static final String TAG = "Clock Result";
    private static final int RELATION = CarCondition.NO;

    private Context mContext;
    private AlarmManager mAlarmManager;
    private String mDescription;
    private int mMsgType;

    public ClockResult(Context context){
        mContext = context;
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
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
        Intent clockIntent = new Intent(mContext, AlarmReceiver.class);
        Log.d(TAG, "clock rings");
        PendingIntent pIntent = PendingIntent.getBroadcast(mContext, 0, clockIntent, 0);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5*1000, pIntent);
        return 0;
    }

    @Override
    public int stopAction() {
        Intent clockIntent = new Intent(mContext, AlarmReceiver.class);
        Log.d(TAG, "clock rests");
        PendingIntent pIntent = PendingIntent.getBroadcast(mContext, 0, clockIntent, 0);
        mAlarmManager.cancel(pIntent);
        return 0;
    }
}
