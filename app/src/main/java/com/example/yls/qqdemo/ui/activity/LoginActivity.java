package com.example.yls.qqdemo.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yls.qqdemo.R;
import com.example.yls.qqdemo.utils.ThreadUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



/**
 * Created by yls on 2016/12/28.
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.user_name)
    EditText mUserName;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.login)
    Button mLogin;
    @BindView(R.id.new_user)
    TextView mNewUser;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 0;
    @Override
    public int getLayoutSon() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        super.init();
        ButterKnife.bind(this);
        mPassword.setOnEditorActionListener(mOnEditorActionListener);
    }




    @OnClick({R.id.login, R.id.new_user})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                //判断用户是否有写磁盘的权限
                if (hasWriteExternalStoragePermission()) {
                  login();
                } else {
                    //申请权限
                    applyWriteExternalStoragePermission();
                }
                break;
            case R.id.new_user:
                goTo(RegisterActivity.class);
                break;
        }
    }
private  TextView.OnEditorActionListener mOnEditorActionListener=new TextView.OnEditorActionListener() {
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId== EditorInfo.IME_ACTION_GO){
            login();
            return true;
        }


        return false;
    }
};

    private void applyWriteExternalStoragePermission() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 检查是否是写磁盘的权限
     * @return
     */
    private boolean hasWriteExternalStoragePermission() {

        int result = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PermissionChecker.PERMISSION_GRANTED;

        //return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void login() {

        showProgressDialog(getString(R.string.loginimg));
        hideKeyboard();

        String userName=mUserName.getText().toString().trim();
        String password=mPassword.getText().toString().trim();

        EMClient.getInstance().login(userName,password,new EMCallBack() {//回调
            @Override
            public void onSuccess() {



                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("main", "登录聊天服务器成功！");
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                        goTo(MainActivity.class);
                    }
                });

            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登录聊天服务器失败！");
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                    }
                });

            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    login();
                } else {
                    Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }









}
