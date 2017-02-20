package com.example.yls.qqdemo.presenter.impl;

import com.example.yls.qqdemo.presenter.SplashPresenter;
import com.example.yls.qqdemo.view.SplashView;
import com.hyphenate.chat.EMClient;

/**
 * Created by yls on 2016/12/28.
 */

public class SplashPresentImpl implements SplashPresenter{
    private SplashView mSplashView;
    public  SplashPresentImpl(SplashView view){
        mSplashView=view;
    }
   @Override
    public void checkLoginStatus() {
        if(isLoginIn()){
            mSplashView.onLoginIn();
        }else{
            mSplashView.noNotLogin();
        }

   }






    private boolean isLoginIn() {
        return EMClient.getInstance().isLoggedInBefore()&&EMClient.getInstance().isConnected();
    }
}
