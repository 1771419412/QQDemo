package com.example.yls.qqdemo.presenter.impl;

import com.example.yls.qqdemo.database.Contact;
import com.example.yls.qqdemo.database.DatabaseManager;
import com.example.yls.qqdemo.model.ContactListItem;
import com.example.yls.qqdemo.presenter.ContactPresenter;
import com.example.yls.qqdemo.utils.ThreadUtils;
import com.example.yls.qqdemo.view.ContactView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by 雪无痕 on 2016/12/30.
 */

public class ContactPresenterImpl implements ContactPresenter {

    private ContactView mContactView;
    private List<ContactListItem> mContactListItems;

    public ContactPresenterImpl(ContactView contactView) {
        mContactView = contactView;
        mContactListItems=new ArrayList<ContactListItem>();
    }

    @Override
    public void loadContacts()  {

        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {


                try {
                    DatabaseManager.getInstance().deleteContacts();
                    //同步方法，在子线程做
                    List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    for(int i=0;i<usernames.size();i++){
                       ContactListItem item=new ContactListItem();
                        item.contact=usernames.get(i);




                        //将联系人保存到数据库
                        Contact contact=new Contact();
                        contact.setUserName(usernames.get(i));
                        DatabaseManager.getInstance().saveContact(contact);

                        mContactListItems.add(item);

                    }
                    Collections.sort(mContactListItems, new Comparator<ContactListItem>() {
                        @Override
                        public int compare(ContactListItem o1, ContactListItem o2) {
                            return o1.contact.charAt(0)-o2.contact.charAt(0);
                        }
                    });
                    //先判断后一个item跟前面一个item的首字符是否一致，如果是一致，则不显示首字符
                    for (int i = 0; i <mContactListItems.size() ; i++) {
                        ContactListItem item=mContactListItems.get(i);
                        if(i>0 && item.getFirstLetter().equals(mContactListItems.get(i-1).getFirstLetter())){
                            item.showFirstLetter=false;
                        }

                    }


                    ThreadUtils.runOnMainThread(new Runnable() {
                     @Override
                     public void run() {
                         mContactView.onLoadContactSuccess();
                     }
                 });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onLoadContactFailed();
                        }
                    });
                }


            }
        });

    }

    @Override
    public List<ContactListItem> getDateList() {
        return mContactListItems;
    }

    @Override
    public void refresContacts() {
        mContactListItems.clear();
        loadContacts();

    }

    @Override
    public void deleteFriend(final String userName) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(userName);
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onDeleteFriendSuccess();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onDeleteFriendFailed();
                        }
                    });
                }
            }
        });


    }

    @Override
    public void refresh() {
        //清空老数据
        mContactListItems.clear();
        loadContacts();
    }
}
