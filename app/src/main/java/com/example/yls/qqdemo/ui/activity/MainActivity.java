package com.example.yls.qqdemo.ui.activity;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.yls.qqdemo.R;
import com.example.yls.qqdemo.adapter.EMMessageListenerAdapter;
import com.example.yls.qqdemo.factory.FragmentFactory;
import com.example.yls.qqdemo.utils.ThreadUtils;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {


    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;
    private FragmentManager mFragmentManager;

    @Override
    public int getLayoutSon() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        super.init();
        mFragmentManager=getSupportFragmentManager();

        mBottomBar.setOnTabSelectListener(mOnTabSelectListener);
        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListenerAdapter);

       // updateUnreadCount();
        //注册坚挺状态
        EMClient.getInstance().addConnectionListener(mEMConnectionListener);
    }



    private void updateUnreadCount() {
        BottomBarTab nearby = mBottomBar.getTabWithId(R.id.tab_conversation);
        //获取所有未读消息的总数
        int unreadMsgsCount = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        nearby.setBadgeCount(unreadMsgsCount);
    }

    private OnTabSelectListener mOnTabSelectListener =new OnTabSelectListener() {
        @Override
        public void onTabSelected(@IdRes int tabId) {
            FragmentTransaction fragmentTransaction =mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, FragmentFactory.getInstance().getFragment(tabId));
            fragmentTransaction.commit();
        }
    };

    private EMMessageListenerAdapter mEMMessageListenerAdapter=new EMMessageListenerAdapter(){
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            super.onMessageReceived(list);
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    updateUnreadCount();
                }
            });

        }
    };

  @Override
    protected void onResume() {
        super.onResume();
        updateUnreadCount();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListenerAdapter);
        EMClient.getInstance().removeConnectionListener(mEMConnectionListener);
    }
    private EMConnectionListener mEMConnectionListener=new EMConnectionListener() {
        @Override
        public void onConnected() {

        }

        @Override
        public void onDisconnected(int i) {
            if(i== EMError.USER_LOGIN_ANOTHER_DEVICE){
                //跳转到登录界面
                goTo(LoginActivity.class);
            }
        }
    };
}
