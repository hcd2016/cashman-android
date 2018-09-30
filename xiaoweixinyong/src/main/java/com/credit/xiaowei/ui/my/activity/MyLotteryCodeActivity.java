package com.credit.xiaowei.ui.my.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.credit.xiaowei.R;
import com.credit.xiaowei.app.App;
import com.credit.xiaowei.base.BaseActivity;
import com.credit.xiaowei.config.Constant;
import com.credit.xiaowei.ui.my.adapter.MyLotteryAdapter;
import com.credit.xiaowei.ui.my.bean.Lottery;
import com.credit.xiaowei.ui.my.contract.LotteryContract;
import com.credit.xiaowei.ui.my.presenter.LotteryPresenter;
import com.credit.xiaowei.util.SpUtil;
import com.credit.xiaowei.widget.recycler.DividerItemDecoration;
import com.credit.xiaowei.widget.refresh.base.OnLoadMoreListener;
import com.credit.xiaowei.widget.refresh.base.OnRefreshListener;
import com.credit.xiaowei.widget.refresh.base.SwipeToLoadLayout;

import java.util.List;

import butterknife.BindView;

import static com.credit.xiaowei.util.ToastUtil.showToast;

/**
 * Created by gym on 2017/2/15 0015.
 * 描述：抽奖码
 */

public class MyLotteryCodeActivity extends BaseActivity<LotteryPresenter> implements LotteryContract.View, OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.swipe_target)
    RecyclerView mRefreshList;
    @BindView(R.id.refresh)
    SwipeToLoadLayout mRefreshLoadLayout;

    private MyLotteryAdapter mMyLotteryAdapter;
    private LotteryPresenter mLotteryPresenter;
    private Lottery mLottery;
    private boolean isPull = true;
    private int pageNo = 1;
    private int pageSize = 10;
    private List<Lottery.ItemBean> itemBeanList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_lottery_code;
    }

    @Override
    public void initPresenter() {
        mLotteryPresenter = new LotteryPresenter();
        mLotteryPresenter.init(this);
        mLotteryPresenter.lotteryRequest(SpUtil.getString(Constant.SHARE_TAG_USERNAME), pageNo + "", pageSize + "");
    }

    @Override
    public void loadData() {
        initView();
    }

    private void initView() {
        mTitle.setTitle("我的抽奖码");
        mRefreshLoadLayout.setOnRefreshListener(this);
        mRefreshLoadLayout.setOnLoadMoreListener(this);
        mRefreshList.setLayoutManager(new LinearLayoutManager(mContext));
        mRefreshList.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mMyLotteryAdapter = new MyLotteryAdapter();
        mRefreshList.setAdapter(mMyLotteryAdapter);
    }

    @Override
    public void lotterySuccess(List<Lottery.ItemBean> lottery) {
        if (lottery != null) {
            if (isPull) {
                mMyLotteryAdapter.clearData();
                if (lottery == null || lottery.size() == 0) {
                    if (mMyLotteryAdapter.getFootersCount() == 0) {
                        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_pop_norecord_style, null);
                        mMyLotteryAdapter.addFooterView(view);
                        mRefreshLoadLayout.setLoadMoreEnabled(false);
                    }
                } else {
                    if (mMyLotteryAdapter.getFootersCount() > 0){
                        mMyLotteryAdapter.clearFooterView();
                        mRefreshLoadLayout.setLoadMoreEnabled(true);
                    }
                }
            }
            itemBeanList = lottery;
            mMyLotteryAdapter.addData(lottery);
        } else {
            mMyLotteryAdapter.clearData();
            if (mMyLotteryAdapter.getFootersCount() == 0) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.layout_pop_norecord_style, null);
                mMyLotteryAdapter.addFooterView(view);
                mRefreshLoadLayout.setLoadMoreEnabled(false);
            }
        }
    }

    @Override
    public void showLoading(String title) {
        App.loadingContent(this, title);
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
        onComplete(mRefreshLoadLayout);
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        showToast(msg);
    }

    @Override
    public void onLoadMore() {
        if (itemBeanList != null) {
            pageNo++;
            isPull = false;
            mLotteryPresenter.lotteryRequest(SpUtil.getString(Constant.SHARE_TAG_USERNAME), pageNo + "", pageSize + "");
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
        mLotteryPresenter.lotteryRequest(SpUtil.getString(Constant.SHARE_TAG_USERNAME), pageNo + "", pageSize + "");
    }
}
