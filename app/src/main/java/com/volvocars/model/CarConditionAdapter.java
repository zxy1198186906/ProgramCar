package com.volvocars.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.volvocars.programcar.R;
import java.util.List;
import static com.volvocars.model.CaseFactory.*;

public class CarConditionAdapter extends ArrayAdapter<CarCondition> {

    private int resourceId;

    public CarConditionAdapter(@NonNull Context context, int resource, List Objects) {
        super(context, resource, Objects);
        resourceId = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        CarCondition condition  = (CarCondition) getItem(position);
        int noticeType = condition.getmImageId();
        int noticeRelation = condition.getmImageRelation();
        View mView;
        ViewHolderCondtion mHolder;
        if (null == convertView){
            mView = LayoutInflater.from(getContext()).inflate(resourceId, null);
            mHolder = new ViewHolderCondtion();
            mHolder.conditionImage = (ImageView) mView.findViewById(R.id.item_image);
            mHolder.conditionDescript = (TextView) mView.findViewById(R.id.item_descript);
            mHolder.conditionRelation = (ImageView) mView.findViewById(R.id.item_relation);
            mView.setTag(mHolder);
        }else{
            mView = convertView;
            mHolder = (ViewHolderCondtion) mView.getTag();
        }
        switch (noticeType){
            case HANDLER_TIME:
                mHolder.conditionImage.setImageResource(R.mipmap.time);
                break;
            case HANDLER_TEMPERATURE:
                mHolder.conditionImage.setImageResource(R.mipmap.temper);
                break;
            case HANDLER_NET:
                mHolder.conditionImage.setImageResource(R.mipmap.wifi);
                break;
            case HANDLER_CLOCK:
                mHolder.conditionImage.setImageResource(R.mipmap.clock);
                break;
            case HANDLER_MUSIC:
                mHolder.conditionImage.setImageResource(R.mipmap.music);
                break;
            case HANDLER_GPS:
                mHolder.conditionImage.setImageResource(R.mipmap.location);
                break;
        }
        mHolder.conditionDescript.setText(condition.getDescript());
        switch (noticeRelation){
            case CarCondition.AND:
                mHolder.conditionRelation.setImageResource(R.color.tomato);
                break;
            case CarCondition.OR:
                mHolder.conditionRelation.setImageResource(R.color.limegreen);
                break;
            case CarCondition.NO:
                mHolder.conditionRelation.setImageResource(R.color.ivory);
                break;
        }
        return mView;
    }
}

class ViewHolderCondtion{
    ImageView conditionImage;
    TextView conditionDescript;
    ImageView conditionRelation;
}


