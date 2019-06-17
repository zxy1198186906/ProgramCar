package com.volvocars.condition;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static com.volvocars.service.RegexHelper.NUM_LESS;
import static com.volvocars.service.RegexHelper.NUM_MORE;

public class NetCondition implements IConditions{
    private Context mContext;
    private ConnectivityManager mConnectivityManager;
    private String mDescription;
    private int mMsgType;
    private int mRelation;

    public NetCondition(Context context){
        mContext = context;
        mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
    public boolean find(int equation, int state) {
        NetworkInfo localInfo = mConnectivityManager.getActiveNetworkInfo();
        switch (state){
            case NUM_MORE:
                if ((null != localInfo) && (localInfo.isAvailable()))
                    return true;
                break;
            case NUM_LESS:
                if (null == localInfo || !localInfo.isAvailable())
                    return true;
                break;
        }
        return false;
    }
}
