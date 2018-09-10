package com.innext.xjx.ui.my.activity;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.innext.xjx.R;
import com.innext.xjx.app.App;
import com.innext.xjx.base.BaseActivity;
import com.innext.xjx.config.Constant;
import com.innext.xjx.dialog.AlertFragmentDialog;
import com.innext.xjx.events.LogoutEvent;
import com.innext.xjx.events.RefreshUIEvent;
import com.innext.xjx.events.UIBaseEvent;
import com.innext.xjx.http.HttpManager;
import com.innext.xjx.ui.authentication.activity.PerfectInformationActivity;
import com.innext.xjx.ui.login.activity.UpdataPwdActivity;
import com.innext.xjx.ui.login.contract.LoginOutContract;
import com.innext.xjx.ui.login.presenter.LoginOutPresenter;
import com.innext.xjx.ui.main.WebViewActivity;
import com.innext.xjx.ui.my.bean.MoreContentBean;
import com.innext.xjx.util.SpUtil;
import com.innext.xjx.util.ToastUtil;
import com.innext.xjx.util.Tool;
import com.innext.xjx.util.ViewUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.innext.xjx.R.id.layout_modification_trade_password;


/**
 * Created by Administrator on 2017/2/14 0014.
 */

public class MoreActivity extends BaseActivity<LoginOutPresenter> implements LoginOutContract.View {
    @BindView(R.id.tv_version)
    TextView mTvVersion;
    @BindView(R.id.layout_feedback)
    TextView mLayoutFeedback;
    @BindView(R.id.layout_modification_login_password)
    TextView mLayoutModificationLoginPassword;
    @BindView(layout_modification_trade_password)
    TextView mLayoutModificationTradePassword;
    @BindView(R.id.tv_exit)
    TextView mTvExit;
    @BindView(R.id.layout_about_my)
    RelativeLayout mLayoutAboutMy;
    MoreContentBean bean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_more;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        mTitle.setTitle("设置");
        bean = getIntent().getParcelableExtra("bean");
        mTvVersion.setText("V" + ViewUtil.getAppVersion(this));

    }

    @OnClick({R.id.layout_about_my, R.id.layout_feedback, R.id.tv_exit, R.id.layout_modification_login_password, R.id.layout_modification_trade_password})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick())
            return;
        switch (view.getId()) {
            case R.id.layout_about_my:
                String aboutUsUrl = App.getConfig().ABOUT_US;
                Intent intent = new Intent(mActivity, WebViewActivity.class);
                intent.putExtra("title", "关于我们");
                intent.putExtra("url", HttpManager.getUrl(aboutUsUrl));
                startActivity(intent);
                break;
            case R.id.layout_feedback:
                Intent intentFeed = new Intent(mActivity, FeedBackActivity.class);
                if(bean!=null&&bean.getService()!=null){
                    intentFeed.putExtra("holiday",bean.getService().getHoliday());
                    intentFeed.putExtra("peacetime",bean.getService().getPeacetime());
                    intentFeed.putExtra("qq_group",bean.getService().getQq_group());
                    intentFeed.putExtra("service_phone",bean.getService().getService_phone());
                    intentFeed.putStringArrayListExtra("services_qq",(ArrayList<String>) bean.getService().getServices_qq());
                }
                startActivity(intentFeed);
                break;
            case R.id.tv_exit:
                logoutConfirm();
                break;
            case R.id.layout_modification_login_password:
                startActivity(new Intent(mActivity, UpdataPwdActivity.class)
                        .putExtra("phone", SpUtil.getString(Constant.SHARE_TAG_USERNAME)));
                break;
            case R.id.layout_modification_trade_password:
                if (bean != null && bean.getVerify_info() != null && bean.getVerify_info().getReal_verify_status().equals("1")) {
                    if (bean.getVerify_info().getReal_pay_pwd_status().equals("1")) {
                        startActivity(new Intent(mActivity, UpdataTradePwdActivity.class));
                    } else {
                        startActivity(new Intent(mActivity, SetTradePwdActivity.class));
                    }

                } else {
                    new AlertFragmentDialog.Builder(this)
                            .setContent("亲，请先填写个人信息哦~").setRightBtnText("确定")
                            .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                                @Override
                                public void dialogRightBtnClick() {
                                    startActivity(new Intent(mActivity, PerfectInformationActivity.class));
                                }
                            }).build();
                }
                break;
        }
    }

    /**
     * 退出确认
     */
    private void logoutConfirm() {
        new AlertFragmentDialog.Builder(this)
                .setCancel(false)
                .setTitle("温馨提示")
                .setContent("您确定要退出登录吗")
                .setLeftBtnText("取消").setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
            @Override
            public void dialogLeftBtnClick() {

            }
        }).setRightBtnText("确定").setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
            @Override
            public void dialogRightBtnClick() {
                mPresenter.loginOut();
            }
        }).build();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /***********
     * eventBus 传入是否设置交易密码
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RefreshUIEvent event) {
        if (UIBaseEvent.EVENT_SET_PAYPWD == event.getType()) {
            bean.getVerify_info().setReal_pay_pwd_status("1");
        }else if (UIBaseEvent.EVENT_REALNAME_AUTHENTICATION_SUCCESS == event.getType()){
            bean.getVerify_info().setReal_verify_status("1");
        }
    }

    @Override
    public void loginOutSuccess() {
        EventBus.getDefault().post(new LogoutEvent(getApplicationContext(),1));
    }

    @Override
    public void showLoading(String content) {
        App.loadingContent(this, content);
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg,  String type) {
        ToastUtil.showToast(msg);
    }
}
