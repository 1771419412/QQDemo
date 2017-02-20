package com.example.yls.qqdemo.ui.fargment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.yls.qqdemo.R;
import com.example.yls.qqdemo.adapter.ConversationListAdapter;
import com.example.yls.qqdemo.adapter.EMMessageListenerAdapter;
import com.example.yls.qqdemo.presenter.ConversationPresenter;
import com.example.yls.qqdemo.presenter.impl.ConversationPresenterImpl;
import com.example.yls.qqdemo.view.ConversationView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.List;

import butterknife.BindView;

/**
 * Created by 雪无痕 on 2016/12/30.
 */

public class ConversationFragment extends BaseFargment implements ConversationView {
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private ConversationPresenter mConversationPresenter;
    private ConversationListAdapter mConversationListAdapter;

    @Override
    public int getLayoutSon() {
        return R.layout.fragment_conversation;
    }

    @Override
    protected void init() {
        super.init();
        mConversationPresenter = new ConversationPresenterImpl(this);
        mTitle.setText(R.string.conversation);
        initRecyclerView();
        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListener);
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mConversationListAdapter = new ConversationListAdapter(getContext(), mConversationPresenter.getConversations());
        mRecyclerView.setAdapter(mConversationListAdapter);
    }

    @Override
    public void onLoadConversationSuccess() {
        //Toast.makeText(getContext(), getString(R.string.load_conversation_success), Toast.LENGTH_SHORT).show();
        mConversationListAdapter.notifyDataSetChanged();
    }

    private EMMessageListenerAdapter mEMMessageListener = new EMMessageListenerAdapter() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            //重新加载会话数据
            mConversationPresenter.loadConversations();
        }

    };

    @Override
    public void onResume() {
        super.onResume();
        mConversationPresenter.loadConversations();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListener);

    }
}