package com.volvocars.service;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.volvocars.condition.IConditions;
import com.volvocars.condition.NetCondition;
import com.volvocars.condition.TemperCondition;
import com.volvocars.condition.TimeCondtion;
import com.volvocars.model.CarCondition;
import com.volvocars.db.CommitOperator;
import com.volvocars.model.CaseFactory;
import com.volvocars.result.ClockResult;
import com.volvocars.result.GpsResult;
import com.volvocars.result.IResults;
import com.volvocars.result.MediaResult;

import java.util.ArrayList;
import java.util.List;

import static com.volvocars.model.CaseFactory.*;
import static com.volvocars.service.RegexHelper.*;

public class Case implements Parcelable{

    private static final String TAG = "Case";
    private Context mContext;

    private String mCaseName;
    private List<IConditions> mConditionList = new ArrayList<>();
    private List<IResults> mResultList = new ArrayList<>();
    private boolean isCaseRun = false;

    Case(String caseName, Context context){
        mCaseName = caseName;
        mContext = context;
    }

    protected Case(Parcel in) {
        mCaseName = in.readString();
        isCaseRun = in.readByte() != 0;
    }

    public static final Creator<Case> CREATOR = new Creator<Case>() {
        @Override
        public Case createFromParcel(Parcel in) {
            return new Case(in);
        }

        @Override
        public Case[] newArray(int size) {
            return new Case[size];
        }
    };

    public List<IConditions> getCondtionList(){
        return mConditionList;
    }

    public List<IResults> getResultList(){
        return mResultList;
    }

    public String getmCaseName(){
        return mCaseName;
    }

    public void setCaseRun(boolean state){
        isCaseRun = state;
    }

    public boolean getCaseRun(){
        return isCaseRun;
    }

    public void initList(CommitOperator operator){
        Log.d("zxy", "enter init case list");
        Cursor cursor = operator.queryDB(mCaseName);
        IConditions condition = null;
        IResults result = null;
        while (cursor.moveToNext()){
            int tempState = cursor.getInt(cursor.getColumnIndex("state"));
            isCaseRun = CommitOperator.STATE_RUN == tempState? true : false;
            Log.d("zxy", "cursor move to loop");
            String tempStr = cursor.getString(cursor.getColumnIndex("destext"));
            int temprel = cursor.getInt(cursor.getColumnIndex("relation"));
            int tempMsgType = regexFindType(tempStr);
            if (CarCondition.NO == temprel) {
                result = CaseFactory.getResult(mContext, tempMsgType);
                result.setDescription(tempStr);
                result.setMsgType(tempMsgType);
                mResultList.add(result);
            }else{
                condition = CaseFactory.getCondition(mContext, tempMsgType);
                condition.setDescription(tempStr);
                condition.setMsgType(tempMsgType);
                condition.setRelation(temprel);
                mConditionList.add(condition);
            }
        }
    }

    private boolean judgeCondition(List<IConditions> conditionList){
        int sum = 0;
        if (conditionList.size()>0) {
            for (int i=0; i<conditionList.size(); i++) {
                IConditions conditions = conditionList.get(i);
                String tempStr = conditions.getDescription();
                int tempRelation = conditions.getRelation();
                int tempEquation = regexFindEqual(tempStr);
                int tempNum = regexFindNum(tempStr);
                if (!conditions.find(tempEquation, tempNum)) {
                    if (tempRelation > 0)
                        sum--;
                }
            }
            Log.i("sum result", "sum is "+sum);
            if (sum >= 0)
                return true;
            else
                return false;
        }
        return false;
    }

    public void promoteResult(){
        if (!mResultList.isEmpty()){
            if (judgeCondition(mConditionList)){
                for (IResults i : mResultList){
                    if (regexFindState(i.getDescription()))
                        i.startAction();
                    else
                        i.stopAction();
                }
            }
        } else{
            Log.d(TAG, "target result list is empty!");
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCaseName);
        dest.writeByte((byte) (isCaseRun ? 1 : 0));
    }
}
