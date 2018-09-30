package com.credit.xiaowei.ui.repayment.fragment;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.credit.xiaowei.R;
import com.credit.xiaowei.app.App;
import com.credit.xiaowei.base.BaseFragment;
import com.credit.xiaowei.events.FragmentRefreshEvent;
import com.credit.xiaowei.events.UIBaseEvent;
import com.credit.xiaowei.ui.main.FragmentFactory;
import com.credit.xiaowei.ui.main.MainActivity;
import com.credit.xiaowei.ui.main.WebViewActivity;
import com.credit.xiaowei.ui.repayment.adapter.RepayTypeItemAdapter;
import com.credit.xiaowei.ui.repayment.adapter.RepaymentAdapter;
import com.credit.xiaowei.ui.repayment.bean.RepaymentItemBean;
import com.credit.xiaowei.ui.repayment.contract.RepaymentContract;
import com.credit.xiaowei.ui.repayment.presenter.RePaymentPresenter;
import com.credit.xiaowei.util.ToastUtil;
import com.credit.xiaowei.widget.loading.LoadingLayout;
import com.credit.xiaowei.widget.recycler.BaseRecyclerAdapter;
import com.credit.xiaowei.widget.recycler.DividerItemDecoration;
import com.credit.xiaowei.widget.refresh.base.OnRefreshListener;
import com.credit.xiaowei.widget.refresh.base.SwipeToLoadLayout;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author：wangzuzhen on 2016/9/21 0021 12:15
 * 还款
 */
public class RepaymentFragment extends BaseFragment<RePaymentPresenter> implements RepaymentContract.View {
    public static RepaymentFragment sFragment;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.refresh)
    SwipeToLoadLayout mRefresh;
    @BindView(R.id.btn_confirm)
    TextView mBtnConfirm;
    @BindView(R.id.ll_no_data)
    LinearLayout mLlNoData;
    @BindView(R.id.tv_repay_title)
    TextView mTvRepayTitle;
    @BindView(R.id.layout_nodata)
    LinearLayout mLayoutNodata;
    @BindView(R.id.iv_emotion)
    ImageView mIvEmotion;
    @BindView(R.id.tv_message)
    TextView mTvMessage;
    @BindView(R.id.type_list)
    RecyclerView mTypeList;
    @BindView(R.id.loading_layout)
    LoadingLayout mLoadingLayout;

    private TextView tv_repayment_status;
    private RepaymentAdapter mAdapter;
    private RepayTypeItemAdapter mTypeAdapter;
    private MainActivity mainActivity;

    private RepaymentItemBean result;

    public static RepaymentFragment getInstance() {
        if (sFragment == null)
            sFragment = new RepaymentFragment();
        return sFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_repayment_detail_main;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        mainActivity = (MainActivity) mActivity;
        initTitle();
        initView();
        initLister();
    }

    private void initTitle() {
        mTitle.setTitle(false, "还款");
        mTitle.setRightTitle(R.mipmap.help, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, WebViewActivity.class);
                intent.putExtra("url", App.getConfig().HELP);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(mContext));
        mSwipeTarget.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new RepaymentAdapter();
        mSwipeTarget.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", mAdapter.getData().get(position).getUrl());
                startActivity(intent);
            }
        });

        mTypeList.setLayoutManager(new LinearLayoutManager(mContext));
        mTypeList.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mTypeAdapter = new RepayTypeItemAdapter(this);
        mTypeList.setAdapter(mTypeAdapter);
        mTypeAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mActivity, WebViewActivity.class);
                intent.putExtra("url", mTypeAdapter.getData().get(position).getLink_url());
                startActivity(intent);
            }
        });
    }

    private void initLister() {
        mRefresh.setLoadMoreEnabled(false);
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getMyLoan();
            }
        });
        if (App.getConfig().getLoginStatus()) {
            mPresenter.getMyLoan();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(FragmentRefreshEvent event) {
        if (UIBaseEvent.EVENT_BORROW_MONEY_SUCCESS == event.getType() ||
                UIBaseEvent.EVENT_LOGIN == event.getType()) {
            mPresenter.getMyLoan();
        }
    }

    private void showNoData(final RepaymentItemBean result) {
        mLayoutNodata.setVisibility(View.VISIBLE);
        mRefresh.setVisibility(View.GONE);
        mIvEmotion.setImageResource(R.mipmap.icon_norecord);
        mTvMessage.setText("您还没有需要还款的记录");
        mView.findViewById(R.id.iv_emotion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getMyLoan();
            }
        });

        if (result.getPay_type() != null && result.getPay_type().size() > 0) {
            mTvRepayTitle.setText(result.getPay_title());
            mTypeAdapter.clearData();
            mTypeAdapter.addData(result.getPay_type());
        }
    }


    @OnClick(R.id.btn_confirm)
    public void onClick() {
        mainActivity.check(FragmentFactory.FragmentStatus.None);
        mainActivity.changeTab(FragmentFactory.FragmentStatus.Lend);
    }

    @Override
    public void repaymentSuccess(RepaymentItemBean result) {
        this.result = result;
        mLoadingLayout.setStatus(LoadingLayout.Success);
        if (result.getList().size() > 0) {
            mLayoutNodata.setVisibility(View.GONE);
            mRefresh.setVisibility(View.VISIBLE);
            if (tv_repayment_status == null) {
                View headView = LayoutInflater.from(mActivity).inflate(R.layout.repayment_item_list, null);
                mAdapter.addHeaderView(headView);
                tv_repayment_status = (TextView) headView.findViewById(R.id.tv_repayment_status);
            } else {
                tv_repayment_status.setVisibility(View.VISIBLE);
            }
            tv_repayment_status.setText("全部待还 " + result.getCount() + " 笔");
            StringInterceptionChangeRed(tv_repayment_status, tv_repayment_status.getText().toString(), result.getCount(), result.getCount());
            mAdapter.clearData();
            mAdapter.addData(result.getList());
        } else {
            if (tv_repayment_status != null) {
                tv_repayment_status.setVisibility(View.GONE);
            }
            showNoData(result);
        }
    }

    /**
     * 字符串截取变红
     *
     * @param string3 你要变色的字符，可为null
     * @param string2 你要变色的字符
     * @param string  整个字符串
     * @param numtext
     */
    public void StringInterceptionChangeRed(TextView numtext,
                                            String string, String string2, String string3) {
        int fstart = string.indexOf(string2);
        int fend = fstart + string2.length();
        SpannableStringBuilder style = new SpannableStringBuilder(string);
        if (!"".equals(string3) && string3 != null) {
            int bstart = string.indexOf(string3);
            int bend = bstart + string3.length();
            style.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mActivity, R.color.theme_color)), bstart, bend,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        style.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mActivity, R.color.theme_color)), fstart, fend,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        numtext.setText(style);
    }

    @Override
    public void showLoading(String content) {
        if (result==null) {
            mLoadingLayout.setStatus(LoadingLayout.Loading);
        }else{
            App.loadingContent(mActivity, content);
        }
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
        mRefresh.setRefreshing(false);
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        ToastUtil.showToast(msg);
        if (tv_repayment_status != null) {
            tv_repayment_status.setVisibility(View.GONE);
        }
        loadingStatusView(msg);
    }

    private void loadingStatusView(String message) {
        if ("网络不可用".equals(message)){
            mLoadingLayout.setStatus(LoadingLayout.No_Network);
        }else{
            mLoadingLayout.setErrorText(message)
                    .setStatus(LoadingLayout.Error);
        }
        mLoadingLayout.setOnReloadListener(new LoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                mPresenter.getMyLoan();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("还款"); //统计页面，"还款"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("还款");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sFragment = null;
        EventBus.getDefault().unregister(this);
    }
}
