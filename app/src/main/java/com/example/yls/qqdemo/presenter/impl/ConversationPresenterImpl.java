package com.example.yls.qqdemo.presenter.impl;

import com.example.yls.qqdemo.presenter.ConversationPresenter;
import com.example.yls.qqdemo.utils.ThreadUtils;
import com.example.yls.qqdemo.view.ConversationView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by 雪无痕 on 2017/1/23.
 */

public class ConversationPresenterImpl implements ConversationPresenter{

    private ConversationView mConversationView;
    private List<EMConversation> mEMConversations;
    public ConversationPresenterImpl(ConversationView view){
        mEMConversations=new ArrayList<EMConversation>();
        mConversationView=view;
    }

    @Override
    public void loadConversations() {


        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {

                mEMConversations.clear();

                Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
                mEMConversations.addAll(conversations.values());
                Collections.sort(mEMConversations, new Comparator<EMConversation>() {
                    @Override
                    public int compare(EMConversation o1, EMConversation o2) {
                       //降序排列,根据最后一跳消息的时间错
                        return (int)(o2.getLastMessage().getMsgTime()-o1.getLastMessage().getMsgTime());
                    }
                });



                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mConversationView.onLoadConversationSuccess();
                    }
                });
            }
        });
    }

    @Override
    public List<EMConversation> getConversations() {
        return mEMConversations;
    }
}
