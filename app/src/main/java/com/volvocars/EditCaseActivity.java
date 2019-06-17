package com.volvocars;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.volvocars.condition.IConditions;
import com.volvocars.dialog.DialogUtil;
import com.volvocars.dialog.onValueChangedListener;
import com.volvocars.exception.IllegalConditionException;
import com.volvocars.exception.IllegalRelationException;
import com.volvocars.model.CarCondition;
import com.volvocars.model.CarConditionAdapter;
import com.volvocars.programcar.R;
import com.volvocars.result.IResults;
import com.volvocars.service.AdminService;
import com.volvocars.service.Case;

import java.util.ArrayList;
import java.util.List;

public class EditCaseActivity extends AppCompatActivity implements View.OnClickListener, onValueChangedListener, TextWatcher {

    private static final String TAG = "EditCaseActivity";
    private static final int ADD_IF = 0;
    private static final int ADD_THEN = 1;

    private EditText edit_caseName;
    private Button btn_back, btn_addCondition, btn_removeCondition, btn_addConsequence, btn_removeConsequence, btn_commit;
    private ListView list_if, list_then;

    private DialogUtil mDialog;
    private static String pOldName;
    private static int pDialogChoice;

    private static AdminService.StateBinder pBinder;
    private static boolean isBinderInit = false;
    private static Intent serIntent;

    private ArrayList<CarCondition> ifList = new ArrayList<>();
    private ArrayList<CarCondition> thenList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case);

        pOldName = getIntent().getStringExtra("caseName");
        edit_caseName = (EditText) findViewById(R.id.edit_casename);
        edit_caseName.setText(pOldName);
        Log.d(TAG, pOldName);

        btn_back = (Button) findViewById(R.id.btn_addcase_back);
        btn_addCondition = (Button) findViewById(R.id.btn_add_condition);
        btn_removeCondition = (Button) findViewById(R.id.btn_minus_condition);
        btn_addConsequence = (Button) findViewById(R.id.btn_add_consequence);
        btn_removeConsequence = (Button) findViewById(R.id.btn_minus_consequence);
        btn_commit = (Button) findViewById(R.id.btn_commit);

        list_if = (ListView) findViewById(R.id.list_condition);
        list_then = (ListView) findViewById(R.id.list_consequence);

        edit_caseName.addTextChangedListener(this);
        btn_back.setOnClickListener(this);
        btn_addCondition.setOnClickListener(this);
        btn_removeCondition.setOnClickListener(this);
        btn_addConsequence.setOnClickListener(this);
        btn_removeConsequence.setOnClickListener(this);
        btn_commit.setOnClickListener(this);
        btn_commit.setEnabled(true);

        mDialog = new DialogUtil(EditCaseActivity.this, this);
        serIntent = new Intent(this, AdminService.class);
        bindService(serIntent, connection, BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            pBinder = (AdminService.StateBinder) service;
            initOldList();
            showElement(ifList, ADD_IF);
            showElement(thenList, ADD_THEN);
            isBinderInit = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            pBinder = null;
            isBinderInit = false;
        }
    };

    @Override
    public void onDestroy(){
        unbindService(connection);
        super.onDestroy();
    }

    private void initOldList() {
        List<Case> caseList = pBinder.getCaseList();
        for (Case c : caseList) {
            List<IConditions> conditionsList = c.getCondtionList();
            List<IResults> resultsList = c.getResultList();
            try {
                for (IConditions condition : conditionsList) {
                    ifList.add(new CarCondition(this, condition.getMsgType(), condition.getDescription(), condition.getRelation()));
                }
                for (IResults result : resultsList) {
                    thenList.add(new CarCondition(this, result.getMsgType(), result.getDescription(), result.getRelation()));
                }
            } catch (IllegalRelationException e) {
                Log.d(TAG, "illegal relation setting");
            } catch (IllegalConditionException e) {
                Log.d(TAG, "illegal condtion setting");
            }
        }
    }

    private void showElement(List Objects, int ListType){
        CarConditionAdapter conditionAdapter = new CarConditionAdapter(getApplicationContext(), R.layout.item_condition, Objects);
        switch (ListType){
            case ADD_IF:
                list_if.setAdapter(conditionAdapter);
                break;
            case ADD_THEN:
                list_then.setAdapter(conditionAdapter);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_addcase_back:
                this.finish();
                break;
            case R.id.btn_add_condition:
                if (ifList.isEmpty())
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.showBottomDialog(DialogUtil.DIALOG_IF_CATEGORY); }
                    }, 500);
                else if (6 == ifList.size()){
                    EditCaseActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Too much conditions!", Toast.LENGTH_SHORT);
                        }
                    });
                }else {
                    showAlterDialog();
                }
                break;
            case R.id.btn_minus_condition:
                if (!ifList.isEmpty()) {
                    ifList.remove(ifList.size() - 1);
                    showElement(ifList, ADD_IF);
                }
                break;
            case R.id.btn_add_consequence:
                if (thenList.isEmpty())
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.showBottomDialog(DialogUtil.DIALOG_THEN_CATEGORY);
                        }
                    }, 500);
                else if (6 == thenList.size()){
                    EditCaseActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Too much actions!", Toast.LENGTH_SHORT);
                        }
                    });
                } else
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.showBottomDialog(DialogUtil.DIALOG_THEN_CATEGORY);
                        }
                    }, 500);
                break;
            case R.id.btn_minus_consequence:
                if (!thenList.isEmpty()) {
                    thenList.remove(thenList.size() - 1);
                    showElement(thenList, ADD_THEN);
                }
                break;
            case R.id.btn_commit:
                if (isBinderInit) {
                    Log.d(TAG, ""+ifList.get(0).getDescript());
                    pBinder.setInsert(pOldName, edit_caseName.getText().toString(), ifList, thenList);
                    Intent mainIntent = new Intent(this, MainActivity.class);
                    passByParcelable("caselist", mainIntent, pBinder.getCaseList());
                    startActivity(mainIntent);
                }
                break;
        }
    }

    private void passByParcelable(String key, Intent intent, ArrayList<Case> objects){
        Bundle mBundle = new Bundle();
        mBundle.putParcelableArrayList(key, objects);
        intent.putExtras(mBundle);
    }

    @Override
    public void onValueChanged(String value, int type) {
        CarCondition condition = null;
        try {
            if (type < 60) {
                if (ifList.isEmpty()) {
                    condition = new CarCondition(this, type, value, CarCondition.AND);
                } else {
                    condition = new CarCondition(this, type, value, 1 - pDialogChoice);
                }
                ifList.add(condition);
                showElement(ifList, ADD_IF);
            } else if (type <= 30) {
                condition = new CarCondition(this, type, value, CarCondition.NO);
                thenList.add(condition);
                showElement(thenList, ADD_THEN);
            }
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }

    private void showAlterDialog(){
        final String[] items = {"And", "Or"};
        final AlertDialog.Builder alterDialog = new AlertDialog.Builder(EditCaseActivity.this);
        alterDialog.setTitle("Select Relation");
        alterDialog.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pDialogChoice = which;
                dialog.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.showBottomDialog(DialogUtil.DIALOG_IF_CATEGORY); }
                }, 300);
            }
        });
        alterDialog.show();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}