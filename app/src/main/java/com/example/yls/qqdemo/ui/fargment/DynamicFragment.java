package com.example.yls.qqdemo.ui.fargment;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yls.qqdemo.R;
import com.example.yls.qqdemo.ui.activity.LoginActivity;
import com.example.yls.qqdemo.utils.ThreadUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.OnClick;



/**
 * Created by 雪无痕 on 2016/12/30.
 */

public class DynamicFragment extends BaseFargment {
    @BindView(R.id.logout)
    Button mLogout;
    @BindView(R.id.title)
    TextView mtitle;


    @Override
    public int getLayoutSon() {
        return R.layout.fragment_dynamic;
    }

    @Override
    protected void init() {
        super.init();
        mtitle.setText(R.string.dynamic);
        String logout=String.format(getString(R.string.logout), EMClient.getInstance().getCurrentUser());

        mLogout.setText(logout);

    }



    @OnClick(R.id.logout)
    public void onClick() {

       EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        goTo(LoginActivity.class);
                    }
                });

            }

            @Override
            public void onProgress(int progress, String status) {


            }

            @Override
            public void onError(int code, String message) {
               ThreadUtils.runOnMainThread(new Runnable() {
                   @Override
                   public void run() {
                       Toast.makeText(getContext(), getString(R.string.logout_failed), Toast.LENGTH_SHORT).show();
                   }
               });

            }
        });

    }
}
