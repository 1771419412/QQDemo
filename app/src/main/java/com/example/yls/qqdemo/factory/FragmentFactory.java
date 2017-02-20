package com.example.yls.qqdemo.factory;

import android.support.v4.app.Fragment;

import com.example.yls.qqdemo.R;
import com.example.yls.qqdemo.ui.fargment.ContactsFragment;
import com.example.yls.qqdemo.ui.fargment.ConversationFragment;
import com.example.yls.qqdemo.ui.fargment.DynamicFragment;

/**
 * Created by 雪无痕 on 2016/12/30.
 */

public class FragmentFactory {
    private static FragmentFactory sFragmentFactory;
    private Fragment mConversationFragment;
    private Fragment mContactFragment;
    private Fragment mDynamicFragment;

    private FragmentFactory() {
    }

    public static FragmentFactory getInstance() {
        if (sFragmentFactory == null) {
            synchronized (FragmentFactory.class) {
                if (sFragmentFactory == null) {
                    sFragmentFactory = new FragmentFactory();
                }
            }

        }
        return sFragmentFactory;
    }

    public Fragment getFragment(int tabId){
        switch (tabId){
            case R.id.tab_conversation:
                return getConversationFragment();
            case R.id.tab_contacts:
                return getContactsFragment();
            case R.id.tab_dynamic:
                return getDynamicFragment();
        }
        return null;

    }


    public Fragment getConversationFragment() {
        if(mConversationFragment==null){
            mConversationFragment=new ConversationFragment();
        }
        return mConversationFragment;
    }

    public Fragment getContactsFragment() {
        if(mContactFragment==null){
            mContactFragment=new ContactsFragment();
        }
        return mContactFragment;
    }

    public Fragment getDynamicFragment() {
        if(mDynamicFragment==null){
            mDynamicFragment=new DynamicFragment();
        }
        return mDynamicFragment;
    }
}