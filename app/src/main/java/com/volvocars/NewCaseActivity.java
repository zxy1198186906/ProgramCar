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

import com.volvocars.dialog.DialogUtil;
import com.volvocars.dialog.onValueChangedListener;
import com.volvocars.model.CarCondition;
import com.volvocars.model.CarConditionAdapter;
import com.volvocars.model.CaseFactory;
import com.volvocars.programcar.R;
import com.volvocars.service.AdminService;
import com.volvocars.service.Case;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.volvocars.model.CaseFactory.*;

public class NewCaseActivity extends AppCompatActivity implements View.OnClickListener, onValueChangedListener, TextWatcher {

    private static final String TAG = "NewCaseActivity";
    private static final String ILLEGAL_STRING = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

    private EditText edit_caseName;
    private Button btn_back, btn_addCondition, btn_removeCondition, btn_addConsequence, btn_removeConsequence, btn_commit;
    private ListView list_if, list_then;

    private DialogUtil mDialog;
    private static int pDialogChoice;

    private static Intent serIntent;
    private static AdminService.StateBinder pBinder = null;
    private static boolean isBinderInit = false;

    private static final int ADD_IF = 0;
    private static final int ADD_THEN = 1;

    private ArrayList<CarCondition> ifList = new ArrayList<>();
    private ArrayList<CarCondition> thenList = new ArrayList<>();

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            pBinder = (AdminService.StateBinder) service;
            isBinderInit = true;
            Log.d(TAG, "NewCaseActivity binds to AdminService");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            pBinder = null;
            isBinderInit = false;
            Log.d(TAG, "NewCaseActivity lost bind to AdminService");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case);

        edit_caseName = (EditText) findViewById(R.id.edit_casename);
        btn_back = (Button) findViewById(R.id.btn_addcase_back);
        btn_addCondition = (Button) findViewById(R.id.btn_add_condition);
        btn_removeCondition = (Button) findViewById(R.id.btn_minus_condition);
        btn_addConsequence = (Button) findViewById(R.id.btn_add_consequence);
        btn_removeConsequence = (Button) findViewById(R.id.btn_minus_consequence);
        btn_commit = (Button) findViewById(R.id.btn_commit);

        list_if = (ListView) findViewById(R.id.list_condition);
        list_then = (ListView) findViewById(R.id.list_consequence);
        serIntent = new Intent(this, AdminService.class);
        bindService(serIntent, connection, BIND_AUTO_CREATE);

        edit_caseName.addTextChangedListener(this);
        btn_back.setOnClickListener(this);
        btn_addCondition.setOnClickListener(this);
        btn_removeCondition.setOnClickListener(this);
        btn_addConsequence.setOnClickListener(this);
        btn_removeConsequence.setOnClickListener(this);
        btn_commit.setOnClickListener(this);
        mDialog = new DialogUtil(NewCaseActivity.this, this);
    }

    @Override
    public void onDestroy(){
        unbindService(connection);
        super.onDestroy();
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
                    NewCaseActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Too much conditions!", Toast.LENGTH_SHORT);
                        }
                    });
                }else
                    showAlterDialog();
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
                    NewCaseActivity.this.runOnUiThread(new Runnable() {
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
                if (RegexFind(edit_caseName.getText().toString()))
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Detect illegal char in case name!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }, 300);
                else {
                    if (isBinderInit) {
                        Log.d(TAG, ""+ifList.get(0).getDescript());
                        pBinder.setInsert(null, edit_caseName.getText().toString(), ifList, thenList);
                        Intent mainIntent = new Intent(this, MainActivity.class);
                        passByParcelable("caselist", mainIntent, pBinder.getCaseList());
                        startActivity(mainIntent);
                    }
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
            if (type <= HANDLER_NET) {
                if (ifList.isEmpty()) {
                    condition = new CarCondition(this, type, value, CarCondition.AND);
                } else {
                    condition = new CarCondition(this, type, value, 1 - pDialogChoice);
                }
                ifList.add(condition);
                showElement(ifList, ADD_IF);
            } else if (type <= HANDLER_GPS) {
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
        final AlertDialog.Builder alterDialog = new AlertDialog.Builder(NewCaseActivity.this);
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



    private boolean RegexFind(String source){
        Pattern temp = Pattern.compile(ILLEGAL_STRING);
        Matcher tempMatcher = temp.matcher(source);
        return tempMatcher.find();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!"".equals(s.toString()))
            btn_commit.setEnabled(true);
    }
}