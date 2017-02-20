package com.example.yls.qqdemo.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import butterknife.ButterKnife;

/**
 * Created by yls on 2016/12/28.
 */

public abstract class BaseActivity extends AppCompatActivity{
    private ProgressDialog mProgressDialog;


    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutSon());
        ButterKnife.bind(this);
        init();
    }

    protected void init() {
    }

    public  abstract int getLayoutSon();

    public void goTo(Class activity){
        Intent intent=new Intent(this,activity);
        startActivity(intent);
        finish();

    }
    public void showProgressDialog(String msg){
        if(mProgressDialog==null){
            mProgressDialog=new ProgressDialog(this);
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }
    public void hideProgressDialog(){
        mProgressDialog.hide();

    }
    public void hideKeyboard(){
        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }
}
