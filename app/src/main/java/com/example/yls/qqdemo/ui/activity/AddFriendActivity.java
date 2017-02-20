package com.example.yls.qqdemo.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yls.qqdemo.R;
import com.example.yls.qqdemo.adapter.SearchResultAdapter;
import com.example.yls.qqdemo.presenter.AddFriendPresenter;
import com.example.yls.qqdemo.presenter.impl.AddFriendPresenterImpl;
import com.example.yls.qqdemo.view.AddFriendView;

import butterknife.BindView;
import butterknife.OnClick;



/**
 * Created by 雪无痕 on 2016/12/30.
 */
public class AddFriendActivity extends BaseActivity implements AddFriendView {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.search)
    Button mSearch;
    @BindView(R.id.keyword)
    EditText mKeyword;
    @BindView(R.id.empty)
    TextView mEmpty;

    private AddFriendPresenter mAddFriendPresenter;
    private SearchResultAdapter mSearchResultAdapter;

    @Override
    public int getLayoutSon() {
        return R.layout.activity_add_friend;
    }

    @Override
    protected void init() {
        super.init();
        mAddFriendPresenter = new AddFriendPresenterImpl(this);
        mTitle.setText(R.string.add_friend);
        mKeyword.setOnEditorActionListener(mOnEditorActionListener);


        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultAdapter=new SearchResultAdapter(this,mAddFriendPresenter.getSearchResult());
        mRecyclerView.setAdapter(mSearchResultAdapter);
    }


    @OnClick(R.id.search)
    public void onClick() {
        search();
    }

    private void search() {
        showProgressDialog(getString(R.string.searchimg));
        hideKeyboard();
        String keyword = mKeyword.getText().toString().trim();
        mAddFriendPresenter.searchFriend(keyword);
    }

    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search();
                return true;
            }


            return false;
        }
    };

    @Override
    public void onSearchSuccess() {
        hideProgressDialog();
        mEmpty.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        Toast.makeText(this, getString(R.string.search_success), Toast.LENGTH_SHORT).show();
        mSearchResultAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSearchFailed() {
        hideProgressDialog();
        mEmpty.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        Toast.makeText(this, getString(R.string.search_failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchEmpty() {
        hideProgressDialog();
        mRecyclerView.setVisibility(View.GONE);
        mSearch.setVisibility(View.VISIBLE);

    }

    @Override
    public void onAddFriendSuccess() {
        Toast.makeText(this, getString(R.string.send_add_friend_request_success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddFriendFailed() {
        Toast.makeText(this, getString(R.string.send_add_friend_request_failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
