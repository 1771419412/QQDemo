package com.example.yls.qqdemo.view;

/**
 * Created by 雪无痕 on 2017/1/17.
 */

public interface ChatView {
    void onSendMessageSuccess();

    void onSendMessageFailed();

    void onStartSendMessage();

    void onLoadMessageSuccess();

    void onLoadMoreMessageSuccess(int size);

    void onNoMoreDate();
}
