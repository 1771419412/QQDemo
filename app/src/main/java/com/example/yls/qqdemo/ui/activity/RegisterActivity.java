package com.example.yls.qqdemo.ui.activity;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yls.qqdemo.R;
import com.example.yls.qqdemo.presenter.RegisterPresenter;
import com.example.yls.qqdemo.presenter.impl.RegisterPresenterImpl;
import com.example.yls.qqdemo.view.RegisterView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yls on 2016/12/29.
 */
public class RegisterActivity extends BaseActivity implements RegisterView {
    @BindView(R.id.user_name)
    EditText mUserName;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.confirm_password)
    EditText mConfirmPassword;
    @BindView(R.id.register)
    Button mRegister;
    private RegisterPresenter mRegisterPresenter;

    @Override
    public int getLayoutSon() {
        return R.layout.activity_register;
    }

    @Override
    protected void init() {
        super.init();
        mRegisterPresenter = new RegisterPresenterImpl(this);

        //设置软键盘的监听事件
        mConfirmPassword.setOnEditorActionListener(mOnEditorActionListener);

    }

    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                register();
                return true;

            }
            return false;
        }
    };


    @OnClick(R.id.register)
    public void onClick() {
        register();
    }

    private void register() {
        String userName = mUserName.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String confirmPassword = mConfirmPassword.getText().toString().trim();
        hideKeyboard();
        mRegisterPresenter.resgiter(userName, password, confirmPassword);


    }

    @Override
    public void onRegisterSuccess() {
        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
        hideProgressDialog();
        goTo(LoginActivity.class);
    }

    @Override
    public void onRegisterFailed() {
        Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserNameError() {
        mUserName.setError(getString(R.string.user_name_error));
    }

    @Override
    public void onPasswordError() {
        mPassword.setError(getString(R.string.password_error));
    }

    @Override
    public void onConfirmPassword() {
        mConfirmPassword.setError(getString(R.string.confrim_password_error));
    }

    @Override
    public void onStartRegister() {
        showProgressDialog(getString(R.string.registerimg));
    }

    @Override
    public void onUserNameExist() {
        hideProgressDialog();
        Toast.makeText(this, getString(R.string.user_name_exist), Toast.LENGTH_SHORT).show();
    }
}
