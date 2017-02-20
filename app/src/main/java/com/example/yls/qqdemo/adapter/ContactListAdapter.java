package com.example.yls.qqdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.yls.qqdemo.model.ContactListItem;
import com.example.yls.qqdemo.widget.ContactListItemView;

import java.util.List;

/**
 * Created by 雪无痕 on 2016/12/30.
 */

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContanctListItemViewHolder> {

    private Context mContext;
    private List<ContactListItem> mContactListItems;
    private OnItemClickListener mOnItemClickListener;

    public ContactListAdapter(Context context, List<ContactListItem> listItems) {
        mContext = context;
        mContactListItems = listItems;
    }

    @Override
    public ContanctListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContanctListItemViewHolder(new ContactListItemView(mContext));
    }

    @Override
    public void onBindViewHolder(ContanctListItemViewHolder holder, final int position) {

        holder.mContactListItemView.bindView(mContactListItems.get(position));
        //设置点击和长按事件
        holder.mContactListItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener!=null){
                    mOnItemClickListener.onClick(mContactListItems.get(position).getContact());
                }

            }
        });
        holder.mContactListItemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mOnItemClickListener!=null){
                    mOnItemClickListener.onLongClick(mContactListItems.get(position).getContact());
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mContactListItems.size();
    }

    public class ContanctListItemViewHolder extends RecyclerView.ViewHolder {
        public ContactListItemView mContactListItemView;

        public ContanctListItemViewHolder(ContactListItemView itemView) {
            super(itemView);
            mContactListItemView = itemView;
        }
    }
    public interface OnItemClickListener{
        void onClick(String userName);
        void onLongClick(String userName);
    }
    public void setItemClicklistener(OnItemClickListener l){
        mOnItemClickListener=l;
    }
}
