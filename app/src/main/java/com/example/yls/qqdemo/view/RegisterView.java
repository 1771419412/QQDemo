package com.example.yls.qqdemo.view;

/**
 * Created by yls on 2016/12/29.
 */

public interface RegisterView {
    void onRegisterSuccess();

    void onRegisterFailed();

    void onUserNameError();

    void onPasswordError();

    void onConfirmPassword();

    void onStartRegister();

    void onUserNameExist();
}
