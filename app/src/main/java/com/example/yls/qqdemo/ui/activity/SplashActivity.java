package com.example.yls.qqdemo.ui.activity;

import android.content.Intent;
import android.os.Handler;

import com.example.yls.qqdemo.R;
import com.example.yls.qqdemo.presenter.SplashPresenter;
import com.example.yls.qqdemo.presenter.impl.SplashPresentImpl;
import com.example.yls.qqdemo.view.SplashView;


/**
 * Created by yls on 2016/12/28.
 */

public class SplashActivity extends BaseActivity implements SplashView{
    private Handler mHandler=new Handler();
    private static final int DELAY=2000;
    private SplashPresenter mSplashPresenter;
    @Override
    public int getLayoutSon() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init() {
        super.init();
        mSplashPresenter=new SplashPresentImpl(this);
        mSplashPresenter.checkLoginStatus();

    }



    private void navigateTologin() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },DELAY);
    }

    private void navigateToMain() {
        /*Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();*/
        goTo(MainActivity.class);
    }


    @Override
    public void onLoginIn() {
       navigateToMain();

    }

    @Override
    public void noNotLogin() {
        navigateTologin();
        //goTo(LoginActivity.class);
    }
}
