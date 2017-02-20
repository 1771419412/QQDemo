package com.example.yls.qqdemo.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yls.qqdemo.R;
import com.example.yls.qqdemo.adapter.MessageListAdapter;
import com.example.yls.qqdemo.app.Constant;
import com.example.yls.qqdemo.presenter.ChatPresenter;
import com.example.yls.qqdemo.presenter.impl.ChatPresenterImpl;
import com.example.yls.qqdemo.utils.ThreadUtils;
import com.example.yls.qqdemo.view.ChatView;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 雪无痕 on 2017/1/17.
 */
public class ChatActivity extends BaseActivity implements ChatView{
    @BindView(R.id.title)
    TextView mtitle;
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.message)
    EditText mMessage;
    @BindView(R.id.send)
    Button mSend;
    private ChatPresenter mChatPresenter;
    private String mUserName;
    private MessageListAdapter mMessageListAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    public int getLayoutSon() {
        return R.layout.activity_chat;

    }

    @Override
    protected void init() {
        super.init();
        mChatPresenter=new ChatPresenterImpl(this);
        mUserName = getIntent().getStringExtra(Constant.Extra.USER_NAME);
        String title=String.format(getString(R.string.chat_title), mUserName);
        mtitle.setText(title);
        mBack.setVisibility(View.VISIBLE);
        mMessage.addTextChangedListener(mTextWatcher);
        mMessage.setOnEditorActionListener(mOnEditorActionListener);
        
        initRecyclerView();
        //加载聊天记录
        mChatPresenter.loadMessage(mUserName);

        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListener);

    }

    private void initRecyclerView() {
        mMessageListAdapter=new MessageListAdapter(this,mChatPresenter.getMessageList());
        mLinearLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mMessageListAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);

    }

    @OnClick({R.id.back,R.id.send})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.send:
                sendMessage();
                break;
        }
    }

    private void sendMessage() {
        hideKeyboard();
        String message=mMessage.getText().toString().trim();
        mChatPresenter.sendMessage(mUserName,message);
    }

    private TextWatcher mTextWatcher =new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mSend.setEnabled(s.length()>0);
        }
    };
    private TextView.OnEditorActionListener mOnEditorActionListener=new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(actionId== EditorInfo.IME_ACTION_SEND){
                sendMessage();
                return true;
            }

            return false;
        }
    };

    @Override
    public void onSendMessageSuccess() {
        Toast.makeText(this,getString(R.string.send_message_success), Toast.LENGTH_SHORT).show();
        //刷新列表
        mMessageListAdapter.notifyDataSetChanged();
        //清空编辑框
        mMessage.getEditableText().clear();
    }

    @Override
    public void onSendMessageFailed() {
        Toast.makeText(this,getString(R.string.send_message_failed), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStartSendMessage() {
        mMessageListAdapter.notifyDataSetChanged();
        //滚东到地步
        mRecyclerView.smoothScrollToPosition(mChatPresenter.getMessageList().size()-1);
    }

    @Override
    //加载聊天记录
    public void onLoadMessageSuccess() {
        mMessageListAdapter.notifyDataSetChanged();
        srcollToBottom();
    }

    @Override
    public void onLoadMoreMessageSuccess(int size) {
        //Toast.makeText(this, getString(R.string.more_message_success), Toast.LENGTH_SHORT).show();
        mMessageListAdapter.notifyDataSetChanged();
        //滚动到指定位置
        mRecyclerView.scrollToPosition(size);
    }

    @Override
    public void onNoMoreDate() {
        //Toast.makeText(this, getString(R.string.no_more_date), Toast.LENGTH_SHORT).show();
    }

    private void srcollToBottom(){
        mRecyclerView.scrollToPosition(mChatPresenter.getMessageList().size()-1);
    }

    private EMMessageListener mEMMessageListener=new EMMessageListener() {
        @Override
        public void onMessageReceived(final List<EMMessage> list) {
            //收到消息
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {


                        mMessageListAdapter.addMessage(list.get(0));
                        mChatPresenter.markReaded(mUserName);
                        smoothScrollToBottom();
                    }



                private void smoothScrollToBottom() {
                    mRecyclerView.smoothScrollToPosition(mChatPresenter.getMessageList().size()-1);
                }
            });


        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {
            //收到透传消息

        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> list) {
            //收到已读回执

        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> list) {
            //收到已送达回执

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {
            //消息状态变动

        }
    };
    private RecyclerView.OnScrollListener mOnScrollListener=new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if(newState==RecyclerView.SCROLL_STATE_IDLE){
                //在空闲状态判断是否加载更多
                if(mLinearLayoutManager.findFirstVisibleItemPosition()==0){
                    mChatPresenter.loadMoreMessage(mUserName);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };
}
