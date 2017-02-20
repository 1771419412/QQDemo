package com.example.yls.qqdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yls.qqdemo.R;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 雪无痕 on 2017/1/23.
 */

public class ConversationItemView extends RelativeLayout {
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.last_message)
    TextView mLastMessage;
    @BindView(R.id.timestamp)
    TextView mTimestamp;
    @BindView(R.id.unread_count)
    TextView mUnreadCount;

    public ConversationItemView(Context context) {
        this(context, null);
    }

    public ConversationItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_conversation_item, this);
        ButterKnife.bind(this,this);
    }

    public void bindView(EMConversation emConversation) {
        mUserName.setText(emConversation.getUserName());
        EMMessage lastMessage = emConversation.getLastMessage();
        EMMessageBody body = lastMessage.getBody();
        if(body instanceof EMTextMessageBody){
            mLastMessage.setText(((EMTextMessageBody) body).getMessage());
        }else{
            mLastMessage.setText(R.string.no_text_message );
        }
        long msgTime = lastMessage.getMsgTime();
        String timestampString = DateUtils.getTimestampString(new Date(msgTime));
        mTimestamp.setText(timestampString);

        int unreadMsgCount = emConversation.getUnreadMsgCount();
        if(unreadMsgCount==0){
            mUnreadCount.setVisibility(GONE);
        }else {
            mUnreadCount.setVisibility(VISIBLE);
            mUnreadCount.setText(String.valueOf(unreadMsgCount));

        }


    }
}
