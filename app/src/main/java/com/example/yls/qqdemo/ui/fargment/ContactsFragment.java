package com.example.yls.qqdemo.ui.fargment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yls.qqdemo.R;
import com.example.yls.qqdemo.adapter.ContactListAdapter;
import com.example.yls.qqdemo.app.Constant;
import com.example.yls.qqdemo.presenter.ContactPresenter;
import com.example.yls.qqdemo.presenter.impl.ContactPresenterImpl;
import com.example.yls.qqdemo.ui.activity.AddFriendActivity;
import com.example.yls.qqdemo.ui.activity.ChatActivity;
import com.example.yls.qqdemo.view.ContactView;
import com.example.yls.qqdemo.widget.SlideBar;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by 雪无痕 on 2016/12/30.
 */

public class ContactsFragment extends BaseFargment implements ContactView {
    private static final String TAG = "ContactsFragment";
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.add)
    ImageView mAdd;
    @BindView(R.id.swip_refresh_layout)
    SwipeRefreshLayout mSwipRefreshLayout;
    @BindView(R.id.slide_bar)
    SlideBar mSlideBar;
   /* @BindView(R.id.first_letter)
    TextView mFirstLetter;*/

    private ContactListAdapter mContactListAdapter;
    private ContactPresenter mContactPresenter;



    @Override
    public int getLayoutSon() {
        return R.layout.fragment_contacts;
    }

    @Override
    protected void init() {
        super.init();
        mContactPresenter = new ContactPresenterImpl(this);
        mTitle.setText(R.string.contacts);
        mAdd.setVisibility(View.VISIBLE);

        mSlideBar.setOnSlideChangeListener(mOnSlideChangeListener);

        initRecyclerView();
        mSwipRefreshLayout.setColorSchemeResources(R.color.qq_blue, R.color.colorPrimary);
        mSwipRefreshLayout.setOnRefreshListener(mOnRefreshListener);


        mContactPresenter.loadContacts();

        EMClient.getInstance().contactManager().setContactListener(mEMContactListener);
    }

    private void initRecyclerView() {
        mContactListAdapter = new ContactListAdapter(getContext(), mContactPresenter.getDateList());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mContactListAdapter.setItemClicklistener(mOnItemClickListener);
        mRecyclerView.setAdapter(mContactListAdapter);
    }

    private ContactListAdapter.OnItemClickListener mOnItemClickListener = new ContactListAdapter.OnItemClickListener() {
        @Override
        public void onClick(String userName) {
            //跳转到聊天界面
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra(Constant.Extra.USER_NAME, userName);
            startActivity(intent);
        }

        @Override
        public void onLongClick(String userName) {
            //弹出dialog删除好友
            showDeleteFriendDialog(userName);
        }
    };

    private void showDeleteFriendDialog(final String userName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String msg = String.format(getString(R.string.delete_friend_msg), userName);
        builder.setTitle(getString(R.string.delete_friend))
                .setMessage(msg)
                .setNegativeButton(getString(R.string.cancle), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除好友
                        mContactPresenter.deleteFriend(userName);
                    }
                });
        builder.show();

    }


    @OnClick(R.id.add)
    public void onClick() {
        goTo(AddFriendActivity.class, false);

    }

    @Override
    public void onLoadContactSuccess() {
        mContactListAdapter.notifyDataSetChanged();
        mSwipRefreshLayout.setRefreshing(false);
        //Toast.makeText(getContext(), getString(R.string.load_contacts_success), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLoadContactFailed() {
        Toast.makeText(getContext(), getString(R.string.load_contact_failed), Toast.LENGTH_SHORT).show();
        mSwipRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDeleteFriendSuccess() {
        Toast.makeText(getContext(), getString(R.string.delete_friend_success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteFriendFailed() {
        Toast.makeText(getContext(), getString(R.string.delete_friend_failed), Toast.LENGTH_SHORT).show();
    }

    private EMContactListener mEMContactListener = new EMContactListener() {
        //添加联系人回调
        @Override
        public void onContactAdded(String s) {

            mContactPresenter.refresContacts();
            Log.d(TAG, "onContactAdded: " + s);
        }

        //删除联系人
        @Override
        public void onContactDeleted(String s) {
            mContactPresenter.refresContacts();
            Log.d(TAG, "onContactDeleted: " + s);

        }

        //收到好友请求
        @Override
        public void onContactInvited(String s, String s1) {
            /*try {
                EMClient.getInstance().contactManager().acceptInvitation(s);
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
            try {
                EMClient.getInstance().contactManager().declineInvitation(s1);
            } catch (HyphenateException e) {
                e.printStackTrace();
            }*/
        }

        //好友请求被同意
        @Override
        public void onContactAgreed(String s) {
           /*try {
                EMClient.getInstance().contactManager().acceptInvitation(s);
            } catch (HyphenateException e) {
                e.printStackTrace();
            }*/
            Log.d(TAG, "onContactAgreed: " + s);

        }

        @Override
        public void onContactRefused(String s) {
           /* try {
                EMClient.getInstance().contactManager().declineInvitation(s);
            } catch (HyphenateException e) {
                e.printStackTrace();
            }*/
            Log.d(TAG, "onContactRefused: " + s);

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().contactManager().removeContactListener(mEMContactListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mContactPresenter.refresh();
        }
    };

    private SlideBar.OnSlideChangeListener mOnSlideChangeListener=new SlideBar.OnSlideChangeListener() {
        @Override
        public void onSlideChange(String firstLetter) {
            /*mFirstLetter.setVisibility(View.VISIBLE);
            mFirstLetter.setText(firstLetter);*/
        }


    };
   /* public void onSlideFinish(){
        mFirstLetter.setVisibility(View.GONE);
    }*/

}
