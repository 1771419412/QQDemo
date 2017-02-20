package com.example.yls.qqdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yls.qqdemo.R;
import com.example.yls.qqdemo.model.ContactListItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 雪无痕 on 2016/12/30.
 */

public class ContactListItemView extends RelativeLayout {
    @BindView(R.id.first_letter)
    TextView mFirstLetter;
    @BindView(R.id.contact)
    TextView mContact;

    public ContactListItemView(Context context) {
        this(context, null);
    }

    public ContactListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_contact_list_item, this);
        ButterKnife.bind(this, this);

    }

    public void bindView(ContactListItem contactListItem) {

        mContact.setText(contactListItem.contact);
        if(contactListItem.showFirstLetter){
            mFirstLetter.setVisibility(VISIBLE);
            mFirstLetter.setText(contactListItem.getFirstLetter());
        }else{
            mFirstLetter.setVisibility(GONE);
        }


    }
}
