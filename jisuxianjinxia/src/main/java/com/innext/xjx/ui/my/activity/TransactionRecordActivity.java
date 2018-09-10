package com.innext.xjx.ui.my.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.innext.xjx.R;
import com.innext.xjx.base.BaseActivity;
import com.innext.xjx.ui.main.WebViewActivity;
import com.innext.xjx.ui.my.adapter.TransactionRecordAdapter;
import com.innext.xjx.ui.my.bean.TransactionRecordListBean;
import com.innext.xjx.ui.my.contract.TransactionRecordContract;
import com.innext.xjx.ui.my.presenter.TransactionRecordPresenter;
import com.innext.xjx.widget.recycler.DividerItemDecoration;
import com.innext.xjx.widget.refresh.base.OnLoadMoreListener;
import com.innext.xjx.widget.refresh.base.OnRefreshListener;
import com.innext.xjx.widget.refresh.base.SwipeToLoadLayout;

import java.util.List;

import butterknife.BindView;

/**
 * Created by gym on 2017/2/16 0016.
 * 描述：借款记录
 */

public class TransactionRecordActivity extends BaseActivity<TransactionRecordPresenter> implements TransactionRecordContract.View, OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.swipe_target)
    RecyclerView mRefreshList;
    @BindView(R.id.refresh)
    SwipeToLoadLayout mRefreshLoadLayout;

    private TransactionRecordPresenter recordPresenter;
    private TransactionRecordAdapter mAdapter;
    private int pageNo = 1;
    private int pageSize = 10;
    private boolean isPull = true;
    private List<TransactionRecordListBean> itemBeanList;
    private String url;

    @Override
    public int getLayoutId() {
        return R.layout.activity_transaction_record;
    }

    @Override
    public void initPresenter() {
        recordPresenter = new TransactionRecordPresenter();
        recordPresenter.init(this);
        recordPresenter.recordRequest(pageNo + "", pageSize + "");
    }

    @Override
    public void loadData() {
        initView();
    }

    private void initView() {
        mTitle.setTitle("借款记录");
        mTitle.setRightTitle("还款方式", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (url != null) {
                    Intent intent = new Intent(TransactionRecordActivity.this, WebViewActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
            }
        });
        mRefreshLoadLayout.setOnRefreshListener(this);
        mRefreshLoadLayout.setOnLoadMoreListener(this);
        mRefreshList.setLayoutManager(new LinearLayoutManager(mContext));
        mRefreshList.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new TransactionRecordAdapter();
        mRefreshList.setAdapter(mAdapter);
//        autoLoading(mRefreshLoadLayout, mRefreshList);
    }

    @Override
    public void recordSuccess(List<TransactionRecordListBean> itemBean, String linkUrl) {
        if (linkUrl != null) {
            url = linkUrl;
        }
//        onComplete(mRefreshLoadLayout);
        if (itemBean != null) {
            if (isPull) {
                mAdapter.clearData();
//                mRefreshLoadLayout.setLoadMoreEnabled(true);
                onComplete(mRefreshLoadLayout);
                if (itemBean == null || itemBean.size() == 0) {
                    if (mAdapter.getFootersCount() == 0) {
                        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_pop_norecord_style, null);
                        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        mAdapter.addFooterView(view);
                    }
                } else {
                    if (mAdapter.getFootersCount() > 0)
                        mAdapter.clearFooterView();
                }
            }
            itemBeanList = itemBean;
            mAdapter.addData(itemBean);
        } else {
            mAdapter.clearData();
            if (mAdapter.getFootersCount() == 0) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.layout_pop_norecord_style, null);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mAdapter.addFooterView(view);
            }
        }
    }

    @Override
    public void showLoading(String content) {

    }

    @Override
    public void stopLoading() {
        onComplete(mRefreshLoadLayout);
    }

    @Override
    public void showErrorMsg(String msg, String type) {

    }

    @Override
    public void onLoadMore() {
        if (itemBeanList != null) {
            pageNo++;
            isPull = false;
            recordPresenter.recordRequest(pageNo + "", pageSize + "");
            onComplete(mRefreshLoadLayout);
        } else {
            mRefreshLoadLayout.setLoadingMore(false);
            mRefreshLoadLayout.setLoadingMore(false);
            onComplete(mRefreshLoadLayout);
        }
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        isPull = true;
        recordPresenter.recordRequest(pageNo + "", pageSize + "");
    }

}
