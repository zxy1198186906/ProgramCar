package com.volvocars.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.volvocars.exception.IllegalConditionException;
import com.volvocars.exception.IllegalRelationException;

import static com.volvocars.model.CaseFactory.*;

public class CarCondition implements Parcelable{

    private Context mContext;
    private int mImageRes;
    private String mDec;
    private int mImageRelation;

    public static final int AND = 1;
    public static final int OR = 2;
    public static final int NO = 3;

    public CarCondition(Context context, int imageRes, String dec, int imageRelation) throws IllegalRelationException, IllegalConditionException{
        mContext = context;
        if (imageRes < HANDLER_TIME || imageRes > HANDLER_GPS)
            throw new IllegalConditionException("wrong condition type referrenced");
        mImageRes = imageRes;
        mDec = dec;
        if (imageRelation < AND || imageRelation > NO)
            throw new IllegalRelationException("wrong relation referrenced");
        mImageRelation = imageRelation;
    }

    protected CarCondition(Parcel in) {
        mImageRes = in.readInt();
        mDec = in.readString();
        mImageRelation = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mImageRes);
        dest.writeString(mDec);
        dest.writeInt(mImageRelation);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CarCondition> CREATOR = new Creator<CarCondition>() {
        @Override
        public CarCondition createFromParcel(Parcel in) {
            return new CarCondition(in);
        }

        @Override
        public CarCondition[] newArray(int size) {
            return new CarCondition[size];
        }
    };

    public int getmImageId(){
        return this.mImageRes;
    }

    public String getDescript(){
        return this.mDec;
    }

    public int getmImageRelation(){
        return this.mImageRelation;
    }
}
