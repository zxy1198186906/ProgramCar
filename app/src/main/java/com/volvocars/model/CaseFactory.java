package com.volvocars.model;

import android.content.Context;

import com.volvocars.condition.IConditions;
import com.volvocars.condition.NetCondition;
import com.volvocars.condition.TemperCondition;
import com.volvocars.condition.TimeCondtion;
import com.volvocars.result.ClockResult;
import com.volvocars.result.GpsResult;
import com.volvocars.result.IResults;
import com.volvocars.result.MediaResult;

public final class CaseFactory {

    public static final int HANDLER_TIME = 50;
    public static final int HANDLER_TEMPERATURE = 51;
    public static final int HANDLER_NET = 52;
    public static final int HANDLER_CLOCK = 60;
    public static final int HANDLER_MUSIC = 61;
    public static final int HANDLER_GPS = 62;

    public static IConditions getCondition(Context context, int type){
        switch (type){
            case HANDLER_TIME:
                return new TimeCondtion(context);
            case HANDLER_TEMPERATURE:
                return new TemperCondition(context);
            case HANDLER_NET:
                return new NetCondition(context);
            default:
                return null;
        }
    }

    public static IResults getResult(Context context, int type){
        switch (type) {
            case HANDLER_CLOCK:
                return new ClockResult(context);
            case HANDLER_MUSIC:
                return new MediaResult(context);
            case HANDLER_GPS:
                return new GpsResult(context);
            default:
                return null;
        }
    }
}
