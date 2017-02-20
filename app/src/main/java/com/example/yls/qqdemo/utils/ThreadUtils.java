package com.example.yls.qqdemo.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by yls on 2016/12/29.
 */

public class ThreadUtils {
    private static Handler mHandler=new Handler(Looper.getMainLooper());
    public static Executor sExecutor= Executors.newSingleThreadExecutor();
    public static void runOnBackgroundThread(Runnable runnable){
        sExecutor.execute(runnable);
    }
    public static void runOnMainThread(Runnable runnable){
        mHandler.post(runnable);
    }
}
