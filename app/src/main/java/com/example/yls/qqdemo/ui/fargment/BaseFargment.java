package com.example.yls.qqdemo.ui.fargment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by yls on 2016/12/28.
 */

public abstract class BaseFargment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(getLayoutSon(),null);
        ButterKnife.bind(this,view);
        init();
        return view;

    }

    protected void init() {
    }

    public abstract int getLayoutSon();



    public void goTo(Class activity){
        Intent intent=new Intent(getContext(),activity);
        startActivity(intent);
        getActivity().finish();

    }
    public void goTo(Class activity,boolean isFinish){
        Intent intent=new Intent(getContext(),activity);
        startActivity(intent);
        if(isFinish){
            getActivity().finish();
        }


    }
}