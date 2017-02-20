package com.example.yls.qqdemo.presenter.impl;

import com.example.yls.qqdemo.database.DatabaseManager;
import com.example.yls.qqdemo.event.AddFriendEvent;
import com.example.yls.qqdemo.model.SearchResultItem;
import com.example.yls.qqdemo.presenter.AddFriendPresenter;
import com.example.yls.qqdemo.utils.ThreadUtils;
import com.example.yls.qqdemo.view.AddFriendView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 雪无痕 on 2016/12/31.
 */

public class AddFriendPresenterImpl implements AddFriendPresenter{

    public AddFriendView mAddFriendView;
    private List<SearchResultItem> mSearchResultItems;
    public AddFriendPresenterImpl(AddFriendView addFriendView){
        mAddFriendView=addFriendView;
        mSearchResultItems=new ArrayList<SearchResultItem>();
        EventBus.getDefault().register(this);
    }

    @Override
    public void searchFriend(String keyword) {
        BmobQuery<BmobUser> query=new BmobQuery<BmobUser>();
        // TODO: 2016/12/31
        query.addWhereNotEqualTo("contact", EMClient.getInstance().getCurrentUser());
        query.findObjects(new FindListener<BmobUser>() {
            @Override
            public void done(List<BmobUser> list, BmobException e) {
                if(e==null){
                //搜索成功
                    if(list.size()==0){
                        mAddFriendView.onSearchEmpty();
                        return;
                    }else{
                        List<String> contacts = DatabaseManager.getInstance().queryContact();
                        for (int i = 0; i <list.size() ; i++) {
                            //将user转换成SearchResultitem
                            SearchResultItem item=new SearchResultItem();
                            item.userName=list.get(i).getUsername();
                            item.timestamp=list.get(i).getCreatedAt();



                            item.added =contacts.contains(item.userName);



                            mSearchResultItems.add(item);
                        }


                        mAddFriendView.onSearchSuccess();
                    }

                }else {
                    //搜索失败
                    mAddFriendView.onSearchFailed();
                   // Log.d("aaa", "done: "+e.getMessage());
                }
            }
        });

    }

    @Override
    public List<SearchResultItem> getSearchResult() {
        return mSearchResultItems;
    }
    @Subscribe(threadMode=ThreadMode.BACKGROUND)
    public void handleAddFriendEvent(AddFriendEvent event){
        try {
            EMClient.getInstance().contactManager().addContact(event.userName, event.reason);
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    mAddFriendView.onAddFriendSuccess();
                }
            });
        } catch (HyphenateException e) {
            e.printStackTrace();
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    mAddFriendView.onAddFriendFailed();
                }
            });
        }

    }
    @Override
    public void destory(){
        EventBus.getDefault().unregister(this);
    }
}
