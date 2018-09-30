package com.credit.xiaowei.ui.authentication.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.credit.xiaowei.R;
import com.credit.xiaowei.app.App;
import com.credit.xiaowei.base.BaseActivity;
import com.credit.xiaowei.config.Constant;
import com.credit.xiaowei.dialog.AlertFragmentDialog;
import com.credit.xiaowei.events.AuthenticationRefreshEvent;
import com.credit.xiaowei.ui.authentication.adapter.PerfectInformationAdapter;
import com.credit.xiaowei.ui.authentication.bean.AuthenticationinformationBean;
import com.credit.xiaowei.ui.authentication.bean.PertfecInformationRequestBean;
import com.credit.xiaowei.ui.authentication.contract.PertfecInformationContract;
import com.credit.xiaowei.ui.authentication.presenter.PerfectInformationPresenter;
import com.credit.xiaowei.ui.main.WebViewActivity;
import com.credit.xiaowei.util.StatusViewUtil;
import com.credit.xiaowei.util.Tool;
import com.credit.xiaowei.widget.loading.LoadingLayout;
import com.credit.xiaowei.widget.recycler.BaseRecyclerAdapter;
import com.credit.xiaowei.widget.recycler.DividerItemDecoration;
import com.credit.xiaowei.widget.refresh.base.OnRefreshListener;
import com.credit.xiaowei.widget.refresh.base.SwipeToLoadLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

import static com.credit.xiaowei.http.HttpManager.getUrl;
import static com.credit.xiaowei.util.ToastUtil.showToast;


/**
 * 认证中心
 */
public class PerfectInformationActivity extends BaseActivity<PerfectInformationPresenter> implements PertfecInformationContract.View, BaseRecyclerAdapter.OnItemClick {
    @BindView(R.id.loading_layout)
    LoadingLayout mLoadingLayout;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.refresh)
    SwipeToLoadLayout mRefresh;
    private View footView;
    private int contactStatus;
    private int realVerifyStatus;

    private PerfectInformationAdapter mAdapter;
    private DividerItemDecoration itemDecoration;
    private List<AuthenticationinformationBean> items;

    @Override
    public int getLayoutId() {
        return R.layout.activity_perfect_information;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);

    }

    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        initView();
        iniLister();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void iniLister() {
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                //请求接口
                mPresenter.getPertfecInformation();
            }
        });
        mPresenter.getPertfecInformation();
        mRefresh.setLoadMoreEnabled(false);

    }

    /**
     * 初始化titile
     */
    private void initView() {
        mTitle.setTitle(R.string.perfect_information);
        mAdapter = new PerfectInformationAdapter(this);
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(mContext));
        itemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST);
        mSwipeTarget.addItemDecoration(itemDecoration);
        mSwipeTarget.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshPerfectInfo(AuthenticationRefreshEvent event) {
        mPresenter.getPertfecInformation();
    }

    @Override
    public void PertfecInformationSccess(PertfecInformationRequestBean bean) {
        mLoadingLayout.setStatus(LoadingLayout.Success);
        contactStatus = bean.getItem().getContacts_status();
        realVerifyStatus = bean.getItem().getReal_verify_status();
        items = bean.getItem().getList();
        StatusViewUtil.hidePopWin();
        if (items.size() > 0) {
            mRefresh.setVisibility(View.VISIBLE);
            mAdapter.clearData();
            mAdapter.addData(items);
            if (footView == null) {
                footView = LayoutInflater.from(mContext).inflate(R.layout.encrypt_footer_item, null);
                mAdapter.addFooterView(footView, Tool.dip2px(mContext, 60));
                itemDecoration.setFootViewCount(mAdapter.getFootersCount());
                mSwipeTarget.addItemDecoration(itemDecoration);
            }
        } else {
            StatusViewUtil.showDefaultPopWin(this, new StatusViewUtil.IOnTouchRefresh() {
                @Override
                public void refresh() {
                    mPresenter.getPertfecInformation();
                }
            }, StatusViewUtil.TAG_POP_STYLE_NORECORD, "暂无数据");

            mAdapter.clearData();
            mAdapter.addData(items);
        }
    }

    @Override
    public void showLoading(String content) {
        if (items == null) {
            mLoadingLayout.setStatus(LoadingLayout.Loading);
        } else {
            App.loadingContent(this, content);
        }
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
        mRefresh.setRefreshing(false);
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        showToast(msg);
        if ("网络不可用".equals(msg)) {
            mLoadingLayout.setStatus(LoadingLayout.No_Network);
        } else {
            mLoadingLayout.setErrorText(msg)
                    .setStatus(LoadingLayout.Error);
        }
        mLoadingLayout.setOnReloadListener(new LoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                mPresenter.getPertfecInformation();
            }
        });
    }


    @Override
    public void onItemClick(View view, int position) {
        goShowAuthenticationView(position);
    }

    /**
     * 去显示认证视图
     */
    private void goShowAuthenticationView(int position) {
        if (Tool.isFastDoubleClick()) return;
        AuthenticationinformationBean item = mAdapter.getData().get(position);
        int type = item.getTag();//得到所以列表的type
        Intent intent = new Intent(this, WebViewActivity.class);
        if (Constant.TAG_QUOTA_PERSONAL == type) { // 个人信息
            intent.setClass(this, PersonalInformationActivity.class);
        } else if (Constant.TAG_QUOTA_CONTACT == type) {//紧急联系人
            intent.setClass(this, AuthEmergencyContactActivity.class);
        } else if (Constant.TAG_QUOTA_PHONE == type) {//手机运营商
            if (realVerifyStatus == 1) { //是否实名认证
                if (mAdapter.getData().get(position).getStatus() == 1) { //运营商是否已认证
                    intent.putExtra("url", getUrl(item.getUrl()) + "&recode=2");
                } else {
                    if (contactStatus == 1) {//紧急联系人是否已认证
                        intent.putExtra("url", item.getUrl());
                    } else {
                        showDialog("亲，请先填写紧急联系人哦~");
                        return;
                    }
                }
            } else {
                showDialog("亲，请先填写个人信息哦~");
                return;
            }
        } else if (Constant.TAG_QUOTA_BANK == type) {//银行卡信息
            if (realVerifyStatus == 1) {//是否实名认证
                intent.putExtra("url", item.getUrl());
            } else {
                showDialog("亲，请先填写个人信息哦~");
                return;
            }
        } else if (Constant.TAG_QUOTA_ZMXY == type) {//芝麻信用
            if (realVerifyStatus == 1) {
                intent.putExtra("url", item.getUrl());
            } else {
                showDialog("亲，请先填写个人信息哦~");
                return;
            }
        } else if (Constant.TAG_QUOTA_WORK == type) {//工作信息
            intent.setClass(this, LendWorkDetailsActivity.class);
        } else if (Constant.TAG_QUOTA_ALIPAY == type) {
            intent.putExtra("url", item.getUrl());
            //支付宝认证
        } else if (Constant.TAG_QUOTA_MORE == type) { //更多
            intent.putExtra("url", item.getUrl());
        }
        startActivity(intent);
    }

    private void showDialog(String content) {
        new AlertFragmentDialog.Builder(this)
                .setContent(content)
                .setRightBtnText("确定")
                .build();
    }
}
