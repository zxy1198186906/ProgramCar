package com.volvocars.result;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.volvocars.model.CarCondition;
import com.volvocars.programcar.R;

public class MediaResult implements IResults {
    private static final String TAG = "Media Result";
    private static final int RELATION = CarCondition.NO;

    private Context mContext;
    private MediaPlayer mPlayer;
    private String mDescription;
    private int mMsgType;

    public MediaResult(Context context){
        mContext = context;
        mPlayer = MediaPlayer.create(context, R.raw.aircity);
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
        Log.e("MediaService", " aircity is playing");
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mPlayer.start();
            }
        });
        Log.d(TAG, "music is playing");
        if (null == mPlayer)
            return -1;
        else if (!mPlayer.isPlaying())
            return 0;
        else
            return 1;
    }

    @Override
    public int stopAction() {
        Log.d(TAG, "Media stopped");
        if (null != mPlayer){
            mPlayer.stop();
            mPlayer.release();
        }
        return 0;
    }
}
