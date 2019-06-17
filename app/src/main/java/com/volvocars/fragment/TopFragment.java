package com.volvocars.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.volvocars.model.CarNotice;
import com.volvocars.programcar.R;

import java.util.ArrayList;
import java.util.List;

public class TopFragment extends Fragment {

    List<CarNotice> toplist = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View mView = inflater.inflate(R.layout.fragment_top, container, false);
        return mView;
    }
}
