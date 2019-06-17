package com.volvocars.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.volvocars.db.CommitOperator;
import com.volvocars.db.OperatorFactory;
import com.volvocars.db.SQLite;
import com.volvocars.model.CarCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;

public class AdminService extends Service {
    @Nullable
    private static final String TAG = "AdminService";

    private static StateBinder mBinder = null;
    private List<Case> mCaseList = new ArrayList<>();
    private CommitOperator mOperator;


    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "admin service start");
        mOperator = OperatorFactory.getOperator(this, "SQL");
        initCofig(mOperator);
        startAsConfig(this);
    }


    public class StateBinder extends Binder {
        public void setState(int caseId, boolean caseState) {
            if (!mCaseList.isEmpty()) {
                mCaseList.get(caseId).setCaseRun(caseState);
                if (caseState == true)
                    mOperator.updateDBState(mCaseList.get(caseId).getmCaseName(), 1);
                else if (caseState == false)
                    mOperator.updateDBState(mCaseList.get(caseId).getmCaseName(), 0);
                startAsConfig(getApplicationContext());
            }
        }

        public void setDelete(int itemId, String tableName) {
            mOperator.deleteDB(tableName);
            mCaseList.remove(itemId);
            startAsConfig(getApplicationContext());
        }

        public void setInsert(String oldName, @NonNull String newName, List<CarCondition> objectIf, List<CarCondition> objectThen) {
            ContentValues valueIf = new ContentValues();
            ContentValues valueThen = new ContentValues();
            if (null == oldName)
                mOperator.createDB(newName);
            else{
                mOperator.deleteDB(oldName);
                mOperator.createDB(newName);
            }
            for (CarCondition con : objectIf) {
                valueIf.put(SQLite.ITEM_TYPE, con.getmImageId());
                Log.d("zxy", "" + con.getmImageId());
                valueIf.put(SQLite.ITEM_TEXT, con.getDescript());
                Log.d("zxy", "" + con.getDescript());
                valueIf.put(SQLite.ITEM_RELATION, con.getmImageRelation());
                Log.d("zxy", "" + con.getmImageRelation());
                valueIf.put(SQLite.ITEM_RUN, "" + CommitOperator.STATE_STOP);
                mOperator.insertDB(newName, valueIf);
            }
            for (CarCondition con : objectThen) {
                valueThen.put(SQLite.ITEM_TYPE, con.getmImageId());
                Log.d("zxy", "" + con.getmImageId());
                valueThen.put(SQLite.ITEM_TEXT, con.getDescript());
                Log.d("zxy", "" + con.getDescript());
                valueThen.put(SQLite.ITEM_RELATION, con.getmImageRelation());
                Log.d("zxy", "" + con.getmImageRelation());
                valueThen.put(SQLite.ITEM_RUN, "" + CommitOperator.STATE_STOP);
                mOperator.insertDB(newName, valueThen);
            }
            Log.d(TAG, "update sqlite succeed");
            initCofig(mOperator);
        }


        public ArrayList<Case> getCaseList() {
            return (ArrayList<Case>) mCaseList;
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Admin Service is destroyed");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Admin service onBind");
        if (null == mBinder)
            mBinder = new StateBinder();
        return mBinder;
    }


    private void initCofig(CommitOperator operator) {
        mCaseList.clear();
        List<String> caseNames = operator.queryAllDB();
        caseNames.remove("android_metadata");
        caseNames.remove("sqlite_sequence");
        if (caseNames.isEmpty()) {
            Log.d(TAG, "The CaseName List is Empty!");
        }
        for (int i = 0; i < caseNames.size(); i++) {
            String tempStr = caseNames.get(i);
            Case programCase = new Case(tempStr, this);
            Log.d("TAG", "Case "+tempStr+" is added into caseList");
            programCase.initList(operator);
            mCaseList.add(programCase);
        }
    }

    private void startAsConfig(Context context) {
        Log.d(TAG, "start service config");
        for (Case c : mCaseList) {
            if (c.getCaseRun())
                c.promoteResult();
        }
    }
}
