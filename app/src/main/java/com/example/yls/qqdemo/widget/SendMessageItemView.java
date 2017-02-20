package com.example.yls.qqdemo.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yls.qqdemo.R;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 雪无痕 on 2017/1/17.
 */

public class SendMessageItemView extends RelativeLayout {
    @BindView(R.id.timestamp)
    TextView mTimestamp;
    @BindView(R.id.content)
    TextView mContent;
    @BindView(R.id.send_message_progress)
    ImageView mSendMessageProgress;

    public SendMessageItemView(Context context) {
        this(context, null);
    }

    public SendMessageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_send_message, this);
        ButterKnife.bind(this, this);
    }

    public void bindView(EMMessage emMessage) {
        long msgTime = emMessage.getMsgTime();
        mTimestamp.setText(DateUtils.getTimestampString(new Date(msgTime)));
        EMMessageBody body = emMessage.getBody();
        if (body instanceof EMTextMessageBody) {
            mContent.setText(((EMTextMessageBody) body).getMessage());
        } else {
            mContent.setText(R.string.no_text_message);
        }

        switch (emMessage.status()) {
            case INPROGRESS:
                mSendMessageProgress.setVisibility(VISIBLE);
                mSendMessageProgress.setImageResource(R.drawable.send_message_progress);
                AnimationDrawable drawable = (AnimationDrawable) mSendMessageProgress.getDrawable();
                drawable.start();
                break;
            case SUCCESS:
                mSendMessageProgress.setVisibility(GONE);
                break;
            case FAIL:
                mSendMessageProgress.setImageResource(R.mipmap.msg_error);
                break;
        }
    }
}
