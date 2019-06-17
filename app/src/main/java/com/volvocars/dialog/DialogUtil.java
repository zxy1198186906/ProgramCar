package com.volvocars.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.volvocars.condition.IConditions;
import com.volvocars.programcar.R;
import com.volvocars.result.IResults;

import static com.volvocars.model.CaseFactory.*;


public class DialogUtil extends RelativeLayout implements View.OnClickListener{
    private Dialog mDialog;
    private Dialog mNextDialog;
    private View mView;
    private View mNextView;

    private Context mContext;
    private onValueChangedListener mListener;

    private LinearLayout layout_time, layout_temper, layout_net;
    private LinearLayout layout_clock, layout_music, layout_gps;

    private LinearLayout layout_high_param, layout_final_param, layout_control_param;

    private ImageView img_category;
    private TextView tv_balance;
    private TextView tv_category, tv_param, tv_signal;
    private EditText edit_control, edit_now;
    private TextView tv_low, tv_high;
    private Button btn_minus, btn_add, btn_confirm;

    private static int controlFlag;

    public static final int DIALOG_IF_CATEGORY = 0;
    public static final int DIALOG_THEN_CATEGORY = 1;
    private static final int DIALOG_PARAMS = 2;
    private static final int DIALOG_NO_PARAMS = 3;

    private static final int MINUS_NUMBER = 20;
    private static final int ADD_NUMBER = 21;

    public DialogUtil(Context context, onValueChangedListener listener) {
        super(context);
        mContext = context;
        mListener = listener;
    }


    public void showBottomDialog(int type) {
        mDialog = new Dialog(mContext, R.style.DialogBottom);
        mNextDialog = new Dialog(mContext, R.style.DialogBottom);
        mNextView = LayoutInflater.from(mContext).inflate(R.layout.dialog_params, null);
        switch (type){
            case DIALOG_IF_CATEGORY:
                mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_if_category, null);
                mDialog.setContentView(mView);
                mNextDialog.setContentView(mNextView);
                break;
            case DIALOG_THEN_CATEGORY:
                mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_then_category, null);
                mDialog.setContentView(mView);
                mNextDialog.setContentView(mNextView);
                break;
        }
        setWindow(mDialog, type);
        mDialog.show();
    }


    private void setWindow(Dialog dialog, int type){
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wLayoutParam = window.getAttributes();
        wLayoutParam.gravity = Gravity.BOTTOM;
        wLayoutParam.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wLayoutParam);
        switch (type){
            case DIALOG_IF_CATEGORY:
                initIfCategoryView(window);
                break;
            case DIALOG_THEN_CATEGORY:
                initThenCategoryView(window);
                break;
            case DIALOG_PARAMS:
                initParamsView(window);
                break;
        }
    }

    private void initIfCategoryView(Window window) {
        layout_time = (LinearLayout) window.findViewById(R.id.layout_time_if_category);
        layout_temper = (LinearLayout) window.findViewById(R.id.layout_temper_if_category);
        layout_net = (LinearLayout) window.findViewById(R.id.layout_net_if_category);
        layout_time.setOnClickListener(this);
        layout_temper.setOnClickListener(this);
        layout_net.setOnClickListener(this);
    }

    private void initThenCategoryView(Window window) {
        layout_clock = (LinearLayout) window.findViewById(R.id.layout_clock_then_category);
        layout_music = (LinearLayout) window.findViewById(R.id.layout_music_then_category);
        layout_gps = (LinearLayout) window.findViewById(R.id.layout_gps_then_category);
        layout_clock.setOnClickListener(this);
        layout_music.setOnClickListener(this);
        layout_gps.setOnClickListener(this);
    }

    private void initParamsView(Window window){
        layout_high_param = (LinearLayout) window.findViewById(R.id.layout_higher_params);
        layout_final_param = (LinearLayout) window.findViewById(R.id.layout_final_params);
        layout_control_param = (LinearLayout) window.findViewById(R.id.layout_specific_consequence);

        img_category = (ImageView) window.findViewById(R.id.img_consequence);
        tv_category = (TextView) window.findViewById(R.id.tv_consequence);
        tv_param = (TextView) window.findViewById(R.id.tv_specific_intro);
        tv_balance = (TextView) window.findViewById(R.id.tv_specific_intro_change);
        edit_control = (EditText) window.findViewById(R.id.edit_specific_control);
        tv_signal = (TextView) window.findViewById(R.id.tv_specific_signal);
        edit_now = (EditText) window.findViewById(R.id.edit_now);
        tv_low = (TextView) window.findViewById(R.id.tv_low);
        tv_high = (TextView) window.findViewById(R.id.tv_high);
        btn_add = (Button) window.findViewById(R.id.btn_increase);
        btn_minus = (Button) window.findViewById(R.id.btn_decrease);
        btn_confirm = (Button) window.findViewById(R.id.btn_confirm);

        tv_balance.setOnClickListener(this);
        tv_low.setOnClickListener(this);
        tv_high.setOnClickListener(this);
        edit_control.setOnClickListener(this);
        btn_minus.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
    }

    public void closeBottomDialog(Dialog dialog) {
        if (null != dialog) {
            dialog.dismiss();
            dialog = null;
        }
    }

    private void updateView(int categoryType){
        switch (categoryType){
            case HANDLER_TIME:
                setWindow(mNextDialog, DIALOG_PARAMS);
                img_category.setImageResource(R.mipmap.time);
                tv_category.setText("Time");
                tv_param.setText("If the time is ");
                tv_signal.setText(" O' Clock ");
                edit_control.setText("0");
                edit_now.setText("0");
                tv_low.setText("earlier");
                tv_high.setText("later");
                controlFlag = HANDLER_TIME;
                break;
            case HANDLER_TEMPERATURE:
                setWindow(mNextDialog, DIALOG_PARAMS);
                img_category.setImageResource(R.mipmap.temper);
                tv_category.setText("Temperature");
                tv_param.setText("If the temperature is ");
                edit_control.setText("24");
                edit_now.setText("24");
                tv_signal.setText(" ℃ ");
                tv_low.setText("lower");
                tv_high.setText("higher");
                controlFlag = HANDLER_TEMPERATURE;
                break;
            case HANDLER_NET:
                setWindow(mNextDialog, DIALOG_PARAMS);
                img_category.setImageResource(R.mipmap.wifi);
                tv_category.setText("Network");
                tv_param.setText("If the network is ");
                tv_low.setText("on");
                tv_high.setText("off");
                layout_control_param.setVisibility(GONE);
                controlFlag = HANDLER_NET;
                break;
            case HANDLER_CLOCK:
                setWindow(mNextDialog, DIALOG_PARAMS);
                img_category.setImageResource(R.mipmap.clock);
                tv_category.setText("Clock");
                tv_param.setText("Then the clock is ");
                tv_low.setText("on");
                tv_high.setText("off");
                layout_control_param.setVisibility(GONE);
                controlFlag = HANDLER_CLOCK;
                break;
            case HANDLER_MUSIC:
                setWindow(mNextDialog, DIALOG_PARAMS);
                img_category.setImageResource(R.mipmap.music);
                tv_category.setText("Music");
                tv_param.setText("Then the music is ");
                tv_low.setText("on");
                tv_high.setText("off");
                layout_control_param.setVisibility(GONE);
                controlFlag = HANDLER_MUSIC;
                break;
            case HANDLER_GPS:
                setWindow(mNextDialog, DIALOG_PARAMS);
                img_category.setImageResource(R.mipmap.location);
                tv_category.setText("GPS");
                tv_param.setText("Then the gps is ");
                tv_low.setText("on");
                tv_high.setText("off");
                layout_control_param.setVisibility(GONE);
                controlFlag = HANDLER_GPS;
                break;
        }
    }

    private void updateContent(int categoryType, int addType){
        switch (categoryType){
            case HANDLER_TIME:
                if (MINUS_NUMBER == addType) {
                    int i = Integer.parseInt(edit_now.getText().toString());
                    i--;
                    if (i<0)
                        i = 23;
                    edit_now.setText(""+i);
                    edit_control.setText(""+i);
                }else if (ADD_NUMBER == addType){
                    int i = Integer.parseInt(edit_now.getText().toString());
                    i++;
                    if (i>=24)
                        i = 0;
                    edit_now.setText(""+i);
                    edit_control.setText(""+i);
                }
                break;
            case HANDLER_TEMPERATURE:
                if (MINUS_NUMBER == addType) {
                    int i = Integer.parseInt(edit_now.getText().toString());
                    i--;
                    if (i<15)
                        i = 15;
                    edit_now.setText(""+i);
                    edit_control.setText(""+i);
                }else if (ADD_NUMBER == addType){
                    int i = Integer.parseInt(edit_now.getText().toString());
                    i++;
                    if (i>35)
                        i = 35;
                    edit_now.setText(""+i);
                    edit_control.setText(""+i);
                }
                break;
            case HANDLER_NET:
                setWindow(mNextDialog, DIALOG_NO_PARAMS);
                break;
            case HANDLER_CLOCK:
                setWindow(mNextDialog, DIALOG_NO_PARAMS);
                break;
            case HANDLER_MUSIC:
                setWindow(mNextDialog, DIALOG_NO_PARAMS);
                break;
            case HANDLER_GPS:
                setWindow(mNextDialog, DIALOG_NO_PARAMS);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_time_if_category:
                closeBottomDialog(mDialog);
                updateView(HANDLER_TIME);
                mNextDialog.show();
                break;
            case R.id.layout_temper_if_category:
                closeBottomDialog(mDialog);
                updateView(HANDLER_TEMPERATURE);
                mNextDialog.show();
                break;
            case R.id.layout_net_if_category:
                closeBottomDialog(mDialog);
                updateView(HANDLER_NET);
                mNextDialog.show();
                break;
            case R.id.layout_clock_then_category:
                closeBottomDialog(mDialog);
                updateView(HANDLER_CLOCK);
                mNextDialog.show();
                break;
            case R.id.layout_music_then_category:
                closeBottomDialog(mDialog);
                updateView(HANDLER_MUSIC);
                mNextDialog.show();
                break;
            case R.id.layout_gps_then_category:
                closeBottomDialog(mDialog);
                updateView(HANDLER_GPS);
                mNextDialog.show();
                break;
            case R.id.tv_specific_intro_change:
                layout_high_param.setVisibility(VISIBLE);
                break;
            case R.id.tv_low:
                tv_balance.setText(tv_low.getText());
                tv_balance.setActivated(true);
                edit_control.setEnabled(true);
                btn_confirm.setEnabled(true);
                break;
            case R.id.tv_high:
                tv_balance.setText(tv_high.getText());
                tv_balance.setActivated(true);
                edit_control.setEnabled(true);
                btn_confirm.setEnabled(true);
                break;
            case R.id.edit_specific_control:
                layout_high_param.setVisibility(GONE);
                layout_final_param.setVisibility(VISIBLE);
                break;
            case R.id.btn_decrease:
                updateContent(controlFlag, MINUS_NUMBER);
                break;
            case R.id.btn_increase:
                updateContent(controlFlag, ADD_NUMBER);
                break;
            case R.id.btn_confirm:
                if (controlFlag < HANDLER_NET)
                    mListener.onValueChanged(tv_param.getText().toString()+tv_balance.getText().toString()+" than "
                                +edit_control.getText().toString()+tv_signal.getText().toString(), controlFlag);
                else
                    mListener.onValueChanged(tv_param.getText().toString()+tv_balance.getText().toString(), controlFlag);
                closeBottomDialog(mNextDialog);
                break;
        }
    }
}