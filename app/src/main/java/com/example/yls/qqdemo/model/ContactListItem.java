package com.example.yls.qqdemo.model;

/**
 * Created by 雪无痕 on 2016/12/30.
 */

public class ContactListItem {
    public String getFirstLetter() {
        return String.valueOf(contact.charAt(0)).toUpperCase();
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String firstLetter;//首字符
    public String url;//图片的URL
    public String contact;//联系人的名字
    public boolean showFirstLetter=true;




}
