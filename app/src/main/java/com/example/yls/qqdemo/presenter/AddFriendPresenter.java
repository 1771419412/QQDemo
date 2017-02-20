package com.example.yls.qqdemo.presenter;

import com.example.yls.qqdemo.model.SearchResultItem;

import java.util.List;

/**
 * Created by 雪无痕 on 2016/12/31.
 */

public interface AddFriendPresenter {
    void searchFriend(String keyword);

    List<SearchResultItem> getSearchResult();
    void destory();
}
