package com.example.yls.qqdemo.presenter;

import com.hyphenate.chat.EMConversation;

import java.util.List;

/**
 * Created by 雪无痕 on 2017/1/23.
 */

public interface ConversationPresenter {
    void loadConversations();

    List<EMConversation> getConversations();
}
