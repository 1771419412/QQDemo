package com.example.yls.qqdemo.app;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;

import com.example.yls.qqdemo.R;
import com.example.yls.qqdemo.adapter.EMMessageListenerAdapter;
import com.example.yls.qqdemo.database.DaoSession;
import com.example.yls.qqdemo.database.DatabaseManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BuildConfig;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;

/**
 * Created by yls on 2016/12/29.
 */

public class QQApplication extends Application{
    private SoundPool mSoundPool;
    private DaoSession mDaoSession;
    private int mYulu;
    private int mDuan;


    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(getApplicationContext(),"80279bf0031817c86ca57de5257bc361");
        initEaseMob();
        initSoundPool();
        DatabaseManager.getInstance().initDatabase(getApplicationContext());
        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListenerAdapter);
    }

    private void initSoundPool() {
        mSoundPool=new SoundPool(2,AudioManager.STREAM_MUSIC,0);
        mYulu = mSoundPool.load(getApplicationContext(), R.raw.yulu, 1);
        mDuan = mSoundPool.load(getApplicationContext(), R.raw.duan, 1);

    }

    private void initEaseMob() {

        int pid=android.os.Process.myPid();
        String proccessAppName=getAppName(pid);
        if(proccessAppName==null || !proccessAppName.equalsIgnoreCase(getPackageName())){
            return;
        }


        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(true);

//初始化
        EMClient.getInstance().init(getApplicationContext(), options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        if(BuildConfig.DEBUG){
            EMClient.getInstance().setDebugMode(true);
        }

    }
    private String getAppName(int pID){
       String processName=null;
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List l=am.getRunningAppProcesses();
        Iterator i=l.iterator();
        this.getPackageManager();
        while (i.hasNext()){
            ActivityManager.RunningAppProcessInfo info= (ActivityManager.RunningAppProcessInfo) i.next();
            try {
                if(info.pid==pID){
                    processName=info.processName;
                    return  processName;
                }
            }catch (Exception e){

            }
        }
        return processName;
    }
    private EMMessageListenerAdapter mEMMessageListenerAdapter=new EMMessageListenerAdapter(){
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            //判断app如果在后台，弹出notification
            if(isForeground()){
                //播放段音频
                mSoundPool.play(mDuan,1,1,0,0,1);
            }else {
                mSoundPool.play(mYulu,1,1,0,0,1);
                //播放长音频
                showNotification(list.get(0));
            }
        }
    };

    private void showNotification(EMMessage emMessage) {
        Notification.Builder builder=new Notification.Builder(getApplicationContext());

        String content="";
        EMMessageBody body = emMessage.getBody();
        if(body instanceof EMTextMessageBody){
            content=((EMTextMessageBody) body).getMessage();
        }else {
            content=getString(R.string.no_text_message);
        }

        Notification notification = builder.setContentTitle(getString(R.string.receive_new_message))
                .setContentText(content)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.avatar1))
                .setSmallIcon(R.mipmap.ic_contact_selected_2)
                .getNotification();

        NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0,notification);
    }

    public boolean isForeground() {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        for (int i = 0; i <runningAppProcesses.size() ; i++) {
            ActivityManager.RunningAppProcessInfo info = runningAppProcesses.get(i);
            if(info.processName.equals(getPackageName()) && info.importance==IMPORTANCE_FOREGROUND){
                    return true;
            }

        }
        
        return false;
    }
}
