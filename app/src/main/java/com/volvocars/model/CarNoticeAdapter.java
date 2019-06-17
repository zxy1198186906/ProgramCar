package com.volvocars.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.volvocars.programcar.R;
import java.util.List;

import static com.volvocars.model.CaseFactory.*;

public class CarNoticeAdapter extends ArrayAdapter<CarNotice> {

    private int resourceId;
    private onButtonClickedListener mClickListener;
    private onButtonStateListener mStateListner;

    public CarNoticeAdapter(@NonNull Context context, int resource, @NonNull List objects,
                            onButtonClickedListener listener1, onButtonStateListener listener2) {
        super(context, resource, objects);
        resourceId = resource;
        mClickListener = listener1;
        mStateListner = listener2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final CarNotice notice = (CarNotice) getItem(position);
        int[] noticeImage = notice.getNoticeImage();
        View mView;
        final ViewHolder mHolder;

        if (null == convertView){
            mView = LayoutInflater.from(getContext()).inflate(resourceId, null);
            mHolder = new ViewHolder();
            mHolder.noticeName = (TextView) mView.findViewById(R.id.tv_notice);
            mHolder.imageNotice1 = (ImageView) mView.findViewById(R.id.img_notice1);
            mHolder.imageNotice2 = (ImageView) mView.findViewById(R.id.img_notice2);
            mHolder.imageNotice3 = (ImageView) mView.findViewById(R.id.img_notice3);
            mHolder.imageNotice4 = (ImageView) mView.findViewById(R.id.img_notice4);
            mHolder.imageNotice5 = (ImageView) mView.findViewById(R.id.img_notice5);
            mHolder.imageNotice6 = (ImageView) mView.findViewById(R.id.img_notice6);

            mHolder.btn_save = (Button) mView.findViewById(R.id.btn_save);
            mHolder.btn_edit = (Button) mView.findViewById(R.id.btn_edit);
            mHolder.btn_delete = (Button) mView.findViewById(R.id.btn_delete);

            mView.setTag(mHolder);
        }else{
            mView = convertView;
            mHolder = (ViewHolder) mView.getTag();
        }

        mHolder.noticeName.setText(notice.getNoticeName());
        mHolder.setImageNotice(mHolder.imageNotice1, noticeImage[0]);
        mHolder.setImageNotice(mHolder.imageNotice2, noticeImage[1]);
        mHolder.setImageNotice(mHolder.imageNotice3, noticeImage[2]);
        mHolder.setImageNotice(mHolder.imageNotice4, noticeImage[3]);
        mHolder.setImageNotice(mHolder.imageNotice5, noticeImage[4]);
        mHolder.setImageNotice(mHolder.imageNotice6, noticeImage[5]);

        mHolder.isRun = mStateListner.onStateChecked(position, notice.getNoticeName());
        if (mHolder.isRun){
            mHolder.btn_save.setActivated(true);
            mHolder.btn_save.setText("Stop");

        }else {
            mHolder.btn_save.setActivated(false);
            mHolder.btn_save.setText("Run");
        }


        mHolder.btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mHolder.isRun){
                    Log.d("Adapter", ""+mStateListner.onStateChecked(position, notice.getNoticeName()));
                    mHolder.btn_save.setActivated(false);
                    mHolder.btn_save.setText("Run");
                    mClickListener.onStopClicked(position, notice.getNoticeName());
                    mHolder.isRun = false;
                } else{
                    mHolder.btn_save.setActivated(true);
                    mHolder.btn_save.setText("Stop");
                    mClickListener.onRunClicked(position, notice.getNoticeName());
                    mHolder.isRun = true;
                }
            }
        });

        mHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mHolder.isRun)
                    mClickListener.onEditClicked(position, notice.getNoticeName());
                Log.d("Adapter", "edit item ID: "+position);
            }
        });
        mHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onDeleteClicked(position, notice.getNoticeName());
                Log.d("Adapter", "delete item ID: "+position);
            }
        });
        return mView;
    }
}

class ViewHolder{
    TextView noticeName;
    ImageView imageNotice1, imageNotice2, imageNotice3, imageNotice4, imageNotice5, imageNotice6;
    Button btn_save, btn_edit, btn_delete;
    boolean isRun;

    void setImageNotice(ImageView view, int imageType){
        switch (imageType){
            case HANDLER_TIME:
                view.setImageResource(R.mipmap.time);
                break;
            case HANDLER_TEMPERATURE:
                view.setImageResource(R.mipmap.temper);
                break;
            case HANDLER_NET:
                view.setImageResource(R.mipmap.wifi);
                break;
            case HANDLER_CLOCK:
                view.setImageResource(R.mipmap.clock);
                break;
            case HANDLER_MUSIC:
                view.setImageResource(R.mipmap.music);
                break;
            case HANDLER_GPS:
                view.setImageResource(R.mipmap.location);
                break;
            default:
                view.setImageResource(R.color.papayawhip);
                break;
        }
    }

}
