package com.example.yls.qqdemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.yls.qqdemo.app.Constant;
import com.example.yls.qqdemo.ui.activity.ChatActivity;
import com.example.yls.qqdemo.widget.ConversationItemView;
import com.hyphenate.chat.EMConversation;

import java.util.List;

/**
 * Created by 雪无痕 on 2017/1/23.
 */

public class ConversationListAdapter extends RecyclerView.Adapter<ConversationListAdapter.ConvrsationItemViewHolder>{
    private Context mContext;
    private List<EMConversation> mEMConversations;
    public ConversationListAdapter(Context context,List<EMConversation> emConversations){
        mContext=context;
        mEMConversations=emConversations;
    }

    @Override
    public ConvrsationItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ConvrsationItemViewHolder(new ConversationItemView(mContext));
    }

    @Override
    public void onBindViewHolder(ConvrsationItemViewHolder holder, final int position) {
        holder.mConversationItemView.bindView(mEMConversations.get(position));
        holder.mConversationItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到聊天界面
                Intent intent=new Intent(mContext, ChatActivity.class);
                intent.putExtra(Constant.Extra.USER_NAME,mEMConversations.get(position).getUserName());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEMConversations.size();
    }
    public class ConvrsationItemViewHolder extends RecyclerView.ViewHolder{
        private ConversationItemView mConversationItemView;
        public ConvrsationItemViewHolder(ConversationItemView itemView) {
            super(itemView);
            mConversationItemView=itemView;
        }
    }
}
