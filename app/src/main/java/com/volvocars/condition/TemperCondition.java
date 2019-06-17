package com.volvocars.condition;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import static com.volvocars.service.RegexHelper.NUM_LESS;
import static com.volvocars.service.RegexHelper.NUM_MORE;

public class TemperCondition implements IConditions {

    private Context mContext;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private static float temperature;
    private String mDescription;
    private int mMsgType;
    private int mRelation;

    public TemperCondition(Context context){
        mContext = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
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
    public boolean find(int equation, int degree) {
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
        mSensorManager.registerListener(mListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        switch (equation){
            case NUM_LESS:
                if (temperature < degree)
                    return true;
            case NUM_MORE:
                if (temperature > degree)
                    return true;
        }
        return false;
    }

    private final SensorEventListener mListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (Sensor.TYPE_TEMPERATURE == event.sensor.getType())
                temperature = event.values[0];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
