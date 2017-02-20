package com.example.yls.qqdemo.presenter;

import com.example.yls.qqdemo.model.ContactListItem;

import java.util.List;

/**
 * Created by 雪无痕 on 2016/12/30.
 */

public interface ContactPresenter {
    void loadContacts();

    List<ContactListItem> getDateList();

    void refresContacts();

    void deleteFriend(String userName);

    void refresh();

}
