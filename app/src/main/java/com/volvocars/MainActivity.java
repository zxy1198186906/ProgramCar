package com.volvocars;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.volvocars.fragment.MyFragment;
import com.volvocars.fragment.TopFragment;
import com.volvocars.programcar.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_addcase, btn_topcase, btn_mycase;
    private FrameLayout fragLayout;
    private Intent caseIntent;
    public static final int requestNum = 1;
    private List<String> caseNames = new ArrayList<>();

    private TopFragment topFragment;
    private MyFragment myFragment;

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        caseIntent = new Intent(MainActivity.this, NewCaseActivity.class);
        fragLayout = (FrameLayout) findViewById(R.id.layout_notice);
        btn_addcase = (Button) findViewById(R.id.btn_addcase);
        btn_topcase = (Button) findViewById(R.id.btn_topcase);
        btn_mycase = (Button) findViewById(R.id.btn_mycase);
        btn_addcase.setOnClickListener(this);
        btn_topcase.setOnClickListener(this);
        btn_mycase.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_addcase:
                if (caseNames.size() < 6) {
                    startActivity(caseIntent);
                    finish();
                }
                else
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Reach maximum cases!", Toast.LENGTH_LONG);
                        }
                    });
                break;
            case R.id.btn_topcase:
                switchTopFragment();
                btn_topcase.setEnabled(false);
                btn_mycase.setEnabled(true);
                break;
            case R.id.btn_mycase:
                switchMyFragment();
                btn_mycase.setEnabled(false);
                btn_topcase.setEnabled(true);
                break;
        }
    }

    private void switchTopFragment(){
        FragmentManager fragManager = getFragmentManager();
        FragmentTransaction fragTran = fragManager.beginTransaction();
        if (null == topFragment)
            topFragment = new TopFragment();
        fragTran.replace(R.id.layout_notice, topFragment);
        fragTran.commit();
    }

    private void switchMyFragment(){
        FragmentManager fragManager = getFragmentManager();
        FragmentTransaction fragTran = fragManager.beginTransaction();
        if (null == myFragment)
            myFragment = new MyFragment();
        fragTran.replace(R.id.layout_notice, myFragment);
        fragTran.commit();
    }

}
