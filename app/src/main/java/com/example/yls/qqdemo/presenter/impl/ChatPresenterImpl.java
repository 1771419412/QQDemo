package com.example.yls.qqdemo.presenter.impl;

import com.example.yls.qqdemo.presenter.ChatPresenter;
import com.example.yls.qqdemo.utils.ThreadUtils;
import com.example.yls.qqdemo.view.ChatView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 雪无痕 on 2017/1/17.
 */

public class ChatPresenterImpl implements ChatPresenter {
    private ChatView mChatView;
    private List<EMMessage> mEMMessages;
    private static final int DEFAULT_PAGE_SIZE = 30;
    private boolean canLoadMore = true;

    public ChatPresenterImpl(ChatView chatView) {
        mChatView = chatView;
        mEMMessages = new ArrayList<EMMessage>();

    }

    @Override
    public void sendMessage(final String userName, final String msg) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
                EMMessage message = EMMessage.createTxtSendMessage(msg, userName);
                message.setMessageStatusCallback(mEMCallBack);
                mEMMessages.add(message);
                //当发送一条消息时，通知view层刷新列表，显示一条正在发送的消息
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mChatView.onStartSendMessage();
                    }
                });
//发送消息
                EMClient.getInstance().chatManager().sendMessage(message);
            }
        });
    }

    @Override
    public List<EMMessage> getMessageList() {
        return mEMMessages;
    }

    @Override
    //加载聊天记录
    public void loadMessage(final String username) {

        ThreadUtils.runOnBackgroundThread(new Runnable() {

            @Override
            public void run() {

                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
                if (conversation != null) {
                    //获取此会话的所有消息
                    List<EMMessage> messages = conversation.getAllMessages();
                    mEMMessages.addAll(messages);
                    //指定会话消息未读数清零
                    conversation.markAllMessagesAsRead();


                }
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mChatView.onLoadMessageSuccess();
                    }
                });





            }

        });


    }

    @Override
    //加载更多
    public void loadMoreMessage(final String username) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                if (canLoadMore) {
                    EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);

                    //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
//获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
                    //获取第一条数据的id
                    String msgId = mEMMessages.get(0).getMsgId();
                    final List<EMMessage> messages = conversation.loadMoreMsgFromDB(msgId, DEFAULT_PAGE_SIZE);
                    //将更多数据加载到 集合
                    mEMMessages.addAll(0, messages);

                    if(messages.size()<DEFAULT_PAGE_SIZE){
                        canLoadMore=false;
                    }

                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mChatView.onLoadMoreMessageSuccess(messages.size());
                        }
                    });
                } else {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mChatView.onNoMoreDate();
                        }
                    });
                }
            }

        });
    }

    @Override
    public void markReaded(String userName) {
        //将收到的新消息标记为已读
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(userName);
        conversation.markAllMessagesAsRead();
    }

    private EMCallBack mEMCallBack = new EMCallBack() {
        @Override
        public void onSuccess() {
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    mChatView.onSendMessageSuccess();
                }
            });

        }

        @Override
        public void onError(int i, String s) {
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    mChatView.onSendMessageFailed();
                }
            });
        }

        @Override
        public void onProgress(int i, String s) {

        }
    };

}
