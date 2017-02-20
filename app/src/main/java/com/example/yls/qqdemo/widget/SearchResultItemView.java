package com.example.yls.qqdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yls.qqdemo.R;
import com.example.yls.qqdemo.event.AddFriendEvent;
import com.example.yls.qqdemo.model.SearchResultItem;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 雪无痕 on 2016/12/31.
 */

public class SearchResultItemView extends RelativeLayout {
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.timestamp)
    TextView mTimestamp;
    @BindView(R.id.add_friend)
    Button mAddFriend;

    public SearchResultItemView(Context context) {
        this(context, null);
    }

    public SearchResultItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_search_result, this);
        ButterKnife.bind(this,this);
    }

    public void bindView(SearchResultItem searchResultItem) {
        mUserName.setText(searchResultItem.userName);
        mTimestamp.setText(searchResultItem.timestamp);

        if(searchResultItem.added){
            mAddFriend.setEnabled(false);
            mAddFriend.setText(R.string.already_added);
        }else {
            mAddFriend.setEnabled(true);
            mAddFriend.setText(R.string.add_friend);

        }



    }

    @OnClick(R.id.add_friend)
    public void onClick() {
        EventBus.getDefault().post(new AddFriendEvent(mUserName.getText().toString().trim(),"因为我最帅，所以请加我"));
    }
}
