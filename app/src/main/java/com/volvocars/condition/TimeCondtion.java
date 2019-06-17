package com.volvocars.condition;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;

import static com.volvocars.service.RegexHelper.NUM_LESS;
import static com.volvocars.service.RegexHelper.NUM_MORE;

public class TimeCondtion implements IConditions {
    private Context mContext;
    private String mDescription;
    private int mMsgType;
    private int mRelation;

    public TimeCondtion(Context context){
        mContext = context;
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
    public void setRelation(int relation) {
        mRelation = relation;
    }

    @Override
    public int getRelation() {
        return mRelation;
    }

    @Override
    public boolean find(int equation, int hourTime) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        Log.d("Calendar", "now the hour is "+hour);
        switch (equation){
            case NUM_LESS:
                if (hour < hourTime)
                    return true;
                break;
            case NUM_MORE:
                if (hour > hourTime)
                    return true;
                break;
        }
        return false;
    }
}
