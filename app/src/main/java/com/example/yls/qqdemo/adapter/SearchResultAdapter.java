package com.example.yls.qqdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.yls.qqdemo.model.SearchResultItem;
import com.example.yls.qqdemo.widget.SearchResultItemView;

import java.util.List;

/**
 * Created by 雪无痕 on 2016/12/31.
 */

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultItemViewHolder> {
    private Context mContext;
    private List<SearchResultItem> mSearchResultItems;

    public SearchResultAdapter(Context context, List<SearchResultItem> items) {
        mContext = context;
        mSearchResultItems = items;
    }

    @Override
    public SearchResultItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchResultItemViewHolder(new SearchResultItemView(mContext));
    }

    @Override
    public void onBindViewHolder(SearchResultItemViewHolder holder, int position) {
        holder.mSearchResultItemView.bindView(mSearchResultItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mSearchResultItems.size();
    }

    public class SearchResultItemViewHolder extends RecyclerView.ViewHolder {
        private SearchResultItemView mSearchResultItemView;

        public SearchResultItemViewHolder(SearchResultItemView itemView) {
            super(itemView);
            mSearchResultItemView = itemView;
        }
    }
}
