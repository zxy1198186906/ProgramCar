package com.volvocars.fragment;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.volvocars.condition.IConditions;
import com.volvocars.EditCaseActivity;
import com.volvocars.model.CarNotice;
import com.volvocars.model.CarNoticeAdapter;
import com.volvocars.model.onButtonClickedListener;
import com.volvocars.model.onButtonStateListener;
import com.volvocars.programcar.R;
import com.volvocars.result.IResults;
import com.volvocars.service.*;

import java.util.ArrayList;
import java.util.List;

public class MyFragment extends Fragment implements onButtonClickedListener, onButtonStateListener {

    private static final String TAG = "My_Fragment";
    private static List<Case> pCaseList = null;
    private List<CarNotice> fragmentList = new ArrayList<>();

    private ListView list_mycase;

    private static Intent serIntent;
    private static AdminService.StateBinder pBinder = null;
    private static boolean isBinderInit = false;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            pBinder = (AdminService.StateBinder) service;
            initCase(pBinder.getCaseList());
            showCase(fragmentList);
            isBinderInit = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            pBinder = null;
            isBinderInit = false;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View mView = inflater.inflate(R.layout.fragment_my, container, false);
        list_mycase = (ListView) mView.findViewById(R.id.list_my);
        fragmentList.clear();

        serIntent = new Intent(getActivity(), AdminService.class);
        getActivity().bindService(serIntent, connection, Context.BIND_AUTO_CREATE);
        ArrayList<Case> testList = getActivity().getIntent().getParcelableArrayListExtra("caselist");
        if (null != testList){
            initCase(testList);
            showCase(fragmentList);
        }

        Log.d("zxy", ""+ (null == pBinder));

        return mView;
    }

    @Override
    public void onDestroy(){
        getActivity().unbindService(connection);
        super.onDestroy();
    }

    private void showCase(List objects){
        CarNoticeAdapter noticeAdapter = new CarNoticeAdapter(getActivity().getApplicationContext(), R.layout.item_fragment, objects,
                this, this);
        list_mycase.setAdapter(noticeAdapter);
    }


    private void initCase(List<Case> caseList) {
        if (!caseList.isEmpty()) {
            for (Case c : caseList) {
                int[] caseType = new int[6];
                int caseIndex = 0;
                List<IConditions> conditionList = c.getCondtionList();
                List<IResults> resultList = c.getResultList();
                for (IConditions condition : conditionList) {
                    caseType[caseIndex] = condition.getMsgType();
                    caseIndex++;
                }
                for (IResults result : resultList) {
                    if (caseIndex < 5) {
                        caseType[caseIndex] = result.getMsgType();
                        caseIndex++;
                    }
                }
                fragmentList.add(new CarNotice(getActivity(), c.getmCaseName(), caseType));
            }
            pCaseList = caseList;
        }
    }


    @Override
    public void onRunClicked(int itemId, String itemName) {
        Log.d("zxy", "clicked run");
        if (isBinderInit)
            pBinder.setState(itemId, true);
        else
            Log.d(TAG, "Binder is not valid");
    }

    @Override
    public void onStopClicked(int itemId, String itemName) {
        Log.d("zxy", "click stop");
        if (isBinderInit)
            pBinder.setState(itemId, false);
        else
            Log.d(TAG, "Binder is not valid");
    }

    @Override
    public void onEditClicked(int itemId, String itemName) {
        Log.d("zxy", "click edit");
        Intent editIntent = new Intent(getActivity(), EditCaseActivity.class);
        editIntent.putExtra("caseName", itemName);
        startActivity(editIntent);
    }

    @Override
    public void onDeleteClicked(int itemId, String itemName) {
        Log.d("zxy", "click delete");
        fragmentList.remove(itemId);
        pBinder.setDelete(itemId, itemName);
        showCase(fragmentList);
    }

    @Override
    public boolean onStateChecked(int itemId, String itemName) {
        return pCaseList.get(itemId).getCaseRun();
    }

    @Override
    public void onStateChanged(int itemId, String itemName, boolean itemChanged) {

    }
}

