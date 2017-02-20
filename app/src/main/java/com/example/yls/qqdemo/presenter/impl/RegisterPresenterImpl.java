package com.example.yls.qqdemo.presenter.impl;

import com.example.yls.qqdemo.app.Constant;
import com.example.yls.qqdemo.presenter.RegisterPresenter;
import com.example.yls.qqdemo.utils.StringUtils;
import com.example.yls.qqdemo.utils.ThreadUtils;
import com.example.yls.qqdemo.view.RegisterView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by yls on 2016/12/29.
 */

public class RegisterPresenterImpl implements RegisterPresenter {
    private RegisterView mRegisterView;

    public RegisterPresenterImpl(RegisterView view) {
        mRegisterView = view;
    }

    @Override
    public void resgiter(String userName, String password, String confirmPassword) {
        if (StringUtils.isValidUserName(userName)) {
            if (StringUtils.isValidPassword(password)) {
                if (password.equals(confirmPassword)) {
                    mRegisterView.onStartRegister();
                    resgiterBmob(userName, password);

                } else {
                    mRegisterView.onConfirmPassword();
                }
            } else {
                mRegisterView.onPasswordError();
            }
        } else {
            mRegisterView.onUserNameError();
        }

    }



    public void resgiterBmob(final String userName, final String password) {
        BmobUser bmobUser = new BmobUser();
        bmobUser.setUsername(userName);
        bmobUser.setPassword(password);
        bmobUser.signUp(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (e == null) {
                    //注册成功
                    //注册环信
                    //mRegisterView.onRegisterSuccess();
                    resgiterEaseMob(userName,password);
                } else {
                    //注册失败
                    //通知view层注册失败
                    //如果是用户名已经存在，通知view层提示用户
                    if(e.getErrorCode()== Constant.ErrorCode.USER_ALREDY_EXIST){
                        mRegisterView.onUserNameExist();
                    }else {
                        mRegisterView.onRegisterFailed();

                    }
                }
            }
        });
    }
    private void resgiterEaseMob(final String userName, final String password) {
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                //注册失败会抛出HyphenateException
                try {
                    EMClient.getInstance().createAccount(userName, password);//同步方法
                    mRegisterView.onRegisterSuccess();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }

            }
        }).start();*/
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(userName, password);//同步方法
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mRegisterView.onRegisterSuccess();
                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mRegisterView.onRegisterFailed();
                        }
                    });
                }
            }
        });
    }
}
