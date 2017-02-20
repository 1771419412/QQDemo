package com.example.yls.qqdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.yls.qqdemo.widget.ReceiveMessageItemView;
import com.example.yls.qqdemo.widget.SendMessageItemView;
import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by 雪无痕 on 2017/1/17.
 */

public class MessageListAdapter extends RecyclerView.Adapter{
    public static final int ITEM_TYPE_SEND=0;
    public static final int ITEN_TYPE_RECEIVE=1;
    private Context mContext;
    private List<EMMessage> mEMMessages;

    public MessageListAdapter(Context context, List<EMMessage> messages){
        mContext=context;
        mEMMessages=messages;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==ITEM_TYPE_SEND){
            return new SendMessageListItemViewHolder(new SendMessageItemView(mContext));
        }else {
            return new ReceiveMessageListItemViewHolder(new ReceiveMessageItemView(mContext));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof SendMessageListItemViewHolder){
            ((SendMessageListItemViewHolder) holder).mSendMessageItemView.bindView(mEMMessages.get(position));
        }else if(holder instanceof ReceiveMessageListItemViewHolder){
            ((ReceiveMessageListItemViewHolder) holder).mReceiveMessageItemView.bindView(mEMMessages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mEMMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage emMessage=mEMMessages.get(position);
        return emMessage.direct()==EMMessage.Direct.SEND ? ITEM_TYPE_SEND :ITEN_TYPE_RECEIVE;
    }

    public void addMessage(EMMessage emMessage) {
        mEMMessages.add(emMessage);
        notifyDataSetChanged();
    }




    public class SendMessageListItemViewHolder extends RecyclerView.ViewHolder{
        private SendMessageItemView mSendMessageItemView;

        public SendMessageListItemViewHolder(SendMessageItemView itemView) {
            super(itemView);
            mSendMessageItemView=itemView;
        }
    }

    public class ReceiveMessageListItemViewHolder extends RecyclerView.ViewHolder{
        private ReceiveMessageItemView mReceiveMessageItemView;


        public ReceiveMessageListItemViewHolder(ReceiveMessageItemView itemView) {
            super(itemView);
            mReceiveMessageItemView=itemView;
        }
    }
}
