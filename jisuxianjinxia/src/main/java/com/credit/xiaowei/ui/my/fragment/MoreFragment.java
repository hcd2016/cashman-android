package com.credit.xiaowei.ui.my.fragment;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

import com.credit.xiaowei.R;
import com.credit.xiaowei.app.App;
import com.credit.xiaowei.base.BaseFragment;
import com.credit.xiaowei.config.Constant;
import com.credit.xiaowei.dialog.AlertFragmentDialog;
import com.credit.xiaowei.events.FragmentRefreshEvent;
import com.credit.xiaowei.events.UIBaseEvent;
import com.credit.xiaowei.ui.authentication.activity.PerfectInformationActivity;
import com.credit.xiaowei.ui.main.MainActivity;
import com.credit.xiaowei.ui.main.WebViewActivity;
import com.credit.xiaowei.ui.my.activity.MoreActivity;
import com.credit.xiaowei.ui.my.activity.MyLotteryCodeActivity;
import com.credit.xiaowei.ui.my.activity.TransactionRecordActivity;
import com.credit.xiaowei.ui.my.bean.MoreContentBean;
import com.credit.xiaowei.ui.my.contract.MyContract;
import com.credit.xiaowei.ui.my.presenter.MyPresenter;
import com.credit.xiaowei.util.SpUtil;
import com.credit.xiaowei.util.StringUtil;
import com.credit.xiaowei.util.ToastUtil;
import com.credit.xiaowei.util.Tool;
import com.credit.xiaowei.widget.loading.LoadingLayout;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 作者：黑哥 on 2016/9/20 13:26
 * <p>
 * 更多页面
 */
public class MoreFragment extends BaseFragment<MyPresenter> implements View.OnClickListener, MyContract.View
,SwipeRefreshLayout.OnRefreshListener{
    public static MoreFragment fragment;

    @BindView(R.id.loading_layout)
    LoadingLayout mLoadingLayout;
    @BindView(R.id.tc_progress)
    TextView mTcProgress;
    @BindView(R.id.tv_remaining_borrow)
    TextView mTvRemainingBorrow;
    @BindView(R.id.tv_my_bank)
    TextView mTvMyBank;
    @BindView(R.id.tv_lottery)
    TextView mTvLottery;
    @BindView(R.id.tv_invit_code)
    TextView mTvInvitCode;
    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.tv_qq_group)
    TextView mTvQqGroup;
    private MainActivity mActivity;
    private MoreContentBean mMoreContentBean;


    public static MoreFragment getInstance() {
        if (fragment == null)
            fragment = new MoreFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_more_main;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        mActivity = (MainActivity) getActivity();
        EventBus.getDefault().register(this);
        mRefresh.setColorSchemeColors(ContextCompat.getColor(mContext,R.color.theme_color));
        mRefresh.setOnRefreshListener(this);
        if (App.getConfig().getLoginStatus()) {
            mPresenter.getInfo();
        }

        mTitle.setTitle(false, StringUtil.changeMobile(SpUtil.getString(Constant.SHARE_TAG_USERNAME)));
        mTitle.setRightTitle(R.mipmap.shezhi, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMoreContentBean==null){
                    mPresenter.getInfo();
                }else{
                    Intent intent = new Intent(mActivity, MoreActivity.class);
                    intent.putExtra("bean", mMoreContentBean);
                    startActivity(intent);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FragmentRefreshEvent event) {
        if (UIBaseEvent.EVENT_BANK_CARD_SUCCESS==event.getType()||//银行卡绑定或修改成功
            UIBaseEvent.EVENT_LOGIN==event.getType() || //登录成功
            UIBaseEvent.EVENT_REPAY_SUCCESS==event.getType()||  //还款成功
            UIBaseEvent.EVENT_REALNAME_AUTHENTICATION_SUCCESS==event.getType()||    //实名认证成功
            UIBaseEvent.EVENT_LOAN_SUCCESS==event.getType()||   //借款申请成功
            UIBaseEvent.EVENT_LOAN_FAILED ==event.getType()){   //借款申请失败
            if (UIBaseEvent.EVENT_LOGIN==event.getType()){
                mTitle.setTitle(false, StringUtil.changeMobile(SpUtil.getString(Constant.SHARE_TAG_USERNAME)));
            }
            if (App.getConfig().getLoginStatus()) {
                mPresenter.getInfo();
            }
        }
    }

    /**
     * 设置数据
     */
    private void setData(MoreContentBean mMoreContentBean) {
        if (mMoreContentBean.getCard_info() != null) {
            if (!StringUtil.isBlank(mMoreContentBean.getCard_info().getBank_name()) && !StringUtil.isBlank(mMoreContentBean.getCard_info().getCard_no_end())) {
                mTvMyBank.setText(mMoreContentBean.getCard_info().getBank_name() + "(" + mMoreContentBean.getCard_info().getCard_no_end() + ")");
            } else {
                mTvMyBank.setText("");
            }
        }
        mTvInvitCode.setText(mMoreContentBean.getInvite_code());
        setCreditLimitAnimation(mTcProgress, Integer.valueOf(mMoreContentBean.getCredit_info().getCard_amount()) / 100);
        mTvRemainingBorrow.setText("剩余可借：" + Integer.valueOf(mMoreContentBean.getCredit_info().getCard_unused_amount()) / 100 + "元");
        if (null!=mMoreContentBean.getService()){
            mTvQqGroup.setText(mMoreContentBean.getService().getQq_group());
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            ToastUtil.showToast("分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtil.showToast("分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("我的"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragment = null;
    }

    private void setCreditLimitAnimation(final TextView view, final int maxMoney) {
        ValueAnimator animator = ValueAnimator.ofInt(0, maxMoney)
                .setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setText(String.valueOf(animation.getAnimatedValue()));
            }
        });
        animator.start();
    }

    @OnClick({R.id.layout_perfect, R.id.layout_lend_record, R.id.layout_bank,
            R.id.layout_invitation_code, R.id.layout_invitation, R.id.layout_message,
            R.id.layout_help, R.id.layout_qq, R.id.tv_lottery,R.id.iv_query})
    public void onClick(View view) {
        /**
         * 防止多次点击进入重复界面
         * 针对于startActivity启动
         * */
        if (Tool.isFastDoubleClick())
            return;

        switch (view.getId()) {
            case R.id.layout_perfect:
                startActivity(PerfectInformationActivity.class);
                break;
            case R.id.layout_lend_record:
                startActivity(TransactionRecordActivity.class);
                break;
            case R.id.layout_bank:
                if (mMoreContentBean != null) {
                    try {
                        if (mMoreContentBean.getVerify_info().getReal_verify_status().equals("1")) {
                            //if (mMoreContentBean.getVerify_info().getReal_bind_bank_card_status().equals("1")) {
                                Intent intent = new Intent(mActivity, WebViewActivity.class);
                                intent.putExtra("url", mMoreContentBean.getCard_url());
                                startActivity(intent);
                            /*} else {
                                startActivity(AddBankCardActivity.class);
                            }*/
                        } else {
                            new AlertFragmentDialog.Builder(mActivity)
                                    .setContent("亲，请先填写个人信息哦~")
                                    .setRightBtnText("确定")
                                    .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                                        @Override
                                        public void dialogRightBtnClick() {
                                            startActivity(PerfectInformationActivity.class);
                                        }
                                    }).build();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    mPresenter.getInfo();
                }
                break;
            case R.id.tv_lottery:
                startActivity(MyLotteryCodeActivity.class);
                break;
            case R.id.layout_invitation_code:
                Bundle invitation = new Bundle();
                invitation.putString("url", App.getConfig().INVITATION_CODE);
                startActivity(WebViewActivity.class, invitation);
                break;
            case R.id.layout_invitation:
                if (mMoreContentBean != null) {
                    new ShareAction(mActivity).setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                            .withTitle(mMoreContentBean.getShare_title())
                            .withText(mMoreContentBean.getShare_body())
                            .withTargetUrl(mMoreContentBean.getShare_url())
                            .withMedia(new UMImage(mActivity, mMoreContentBean.getShare_logo()))
                            .setCallback(umShareListener).open();
                }
                break;
            case R.id.layout_message:
                Bundle message = new Bundle();
                message.putString("url", App.getConfig().ACTIVITY_CENTER);
                startActivity(WebViewActivity.class, message);
                break;
            case R.id.layout_help:
                Bundle help = new Bundle();
                help.putString("url", App.getConfig().HELP);
                startActivity(WebViewActivity.class, help);
                break;
            case R.id.iv_query:
                new AlertFragmentDialog.Builder(mActivity)
                        .setContent("资料完整度及真实性会影响您的信用额度,目前只可整百倍数借款。")
                        .setRightBtnText("去提额")
                        .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                            @Override
                            public void dialogRightBtnClick() {
                                startActivity(PerfectInformationActivity.class);
                            }
                        })
                        .setCancel(true).build();
                break;
        }
    }

    @Override
    public void userInfoSuccess(MoreContentBean result) {
        mLoadingLayout.setStatus(LoadingLayout.Success);
        mMoreContentBean = result;
        if (mMoreContentBean != null) {
            setData(mMoreContentBean);
        }
    }

    @Override
    public void showLoading(String content) {
        if (mMoreContentBean == null){
            mLoadingLayout.setStatus(LoadingLayout.Loading);
        }
    }

    @Override
    public void stopLoading() {
        mRefresh.setRefreshing(false);
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        ToastUtil.showToast(msg);
        if ("网络不可用".equals(msg)){
            mLoadingLayout.setStatus(LoadingLayout.No_Network);
        }else{
            mLoadingLayout.setErrorText(msg)
                .setStatus(LoadingLayout.Error);
        }
        mLoadingLayout.setOnReloadListener(new LoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                mPresenter.getInfo();
            }
        });
    }

    @Override
    public void onRefresh() {
        mPresenter.getInfo();
    }
}
