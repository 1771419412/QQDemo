package com.example.yls.qqdemo.presenter;

import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by 雪无痕 on 2017/1/17.
 */

public interface ChatPresenter {

    void sendMessage(String userName, String msg);

    List<EMMessage> getMessageList();

    void loadMessage(String username);

    void loadMoreMessage(String username);

    void markReaded(String userName);
}
