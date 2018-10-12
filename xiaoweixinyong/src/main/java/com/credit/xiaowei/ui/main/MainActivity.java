package com.credit.xiaowei.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.credit.pretend.ptd_util.RetrofitUtil;
import com.credit.xiaowei.R;
import com.credit.xiaowei.app.App;
import com.credit.xiaowei.app.AppManager;
import com.credit.xiaowei.base.BaseActivity;
import com.credit.xiaowei.config.Constant;
import com.credit.xiaowei.dialog.AlertFragmentDialog;
import com.credit.xiaowei.events.ChangeTabMainEvent;
import com.credit.xiaowei.events.FragmentRefreshEvent;
import com.credit.xiaowei.events.RefreshUIEvent;
import com.credit.xiaowei.events.UIBaseEvent;
import com.credit.xiaowei.ui.login.activity.LoginActivity;
import com.credit.xiaowei.ui.login.activity.RegisterPhoneActivity;
import com.credit.xiaowei.util.AppUpdateUtil;
import com.credit.xiaowei.util.LogUtils;
import com.credit.xiaowei.util.SpUtil;
import com.credit.xiaowei.util.StringUtil;
import com.credit.xiaowei.util.ToastUtil;
import com.credit.xiaowei.util.ViewUtil;
import com.credit.xiaowei.xgpush.XGPushHelper;
import com.google.gson.JsonObject;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 首页
 * xiejingwen
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.container)
    FrameLayout mContainer;
    @BindView(R.id.group)
    RadioGroup mGroup;
    private FragmentFactory.FragmentStatus toTabIndex = FragmentFactory.FragmentStatus.None;
    private int oldCheckId = R.id.rb_lend;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        mGroup.setOnCheckedChangeListener(changeListener);
        check(FragmentFactory.FragmentStatus.Lend);
        registerPush();
        checkVersionUpdate();
    }

    public void check(FragmentFactory.FragmentStatus status) {
        mGroup.check(getCheckIdByStatus(status));
    }

    /************
     * 注册信鸽推送
     */
    private void registerPush() {
        XGPushHelper.getInstance().unRegisterPush(App.getContext());
        String account = App.getConfig().getLoginStatus() ? SpUtil.getString(Constant.SHARE_TAG_USERNAME) : "";
        XGPushHelper.getInstance().registerPush(App.getContext(), account);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGroup.setOnCheckedChangeListener(changeListener);

    }

    OnCheckedChangeListener changeListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_lend:
                    toTabIndex = FragmentFactory.FragmentStatus.Lend;
                    changeTab(FragmentFactory.FragmentStatus.Lend);
                    oldCheckId = R.id.rb_lend;
                    break;
                case R.id.rb_account:
                    toTabIndex = FragmentFactory.FragmentStatus.Account;
                    if (App.getConfig().getLoginStatus()) {
                        changeTab(FragmentFactory.FragmentStatus.Account);
                    } else {
                        toLogin();
                        return;
                    }
                    break;
                case R.id.rb_rent_lend:
                    toTabIndex = FragmentFactory.FragmentStatus.RentLend;
                    if (App.getConfig().getLoginStatus()) {
                        changeTab(FragmentFactory.FragmentStatus.RentLend);
                    } else {
                        toLogin();
                        return;
                    }
                    break;
                default:
                    break;

            }
        }
    };

    private void toLogin() {
        mGroup.setOnCheckedChangeListener(null);
        ((RadioButton) findViewById(oldCheckId)).setChecked(true);
        String uName = SpUtil.getString(Constant.SHARE_TAG_USERNAME);
        LogUtils.loge(uName);
        if (!StringUtil.isBlank(uName) && StringUtil.isMobileNO(uName)) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("tag", StringUtil.changeMobile(uName));
            intent.putExtra("phone", uName);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, RegisterPhoneActivity.class);
            startActivity(intent);
        }
    }

    /***********
     * eventBus 监听
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UIBaseEvent event) {
        if (event instanceof RefreshUIEvent) {
            int code = ((RefreshUIEvent) event).getType();
            if (code == UIBaseEvent.EVENT_LOGIN)//登录
            {
                registerPush();
                if (toTabIndex != odlState)//切换
                {
                    changeTab(toTabIndex);
                    ((RadioButton) findViewById(getCheckIdByStatus(toTabIndex))).setChecked(true);
                }
                EventBus.getDefault().post(new FragmentRefreshEvent(code));
            } else if (code == UIBaseEvent.EVENT_LOGOUT) {
                registerPush();
                //默认到首页
                changeTab(FragmentFactory.FragmentStatus.Lend);
                ((RadioButton) findViewById(getCheckIdByStatus(FragmentFactory.FragmentStatus.Lend))).setChecked(true);
                EventBus.getDefault().post(new FragmentRefreshEvent(code));
            } else if (code == UIBaseEvent.EVENT_LOAN_SUCCESS) {
                EventBus.getDefault().post(new FragmentRefreshEvent(code));
            }
        } else if (event instanceof ChangeTabMainEvent) {
            changeTab(((ChangeTabMainEvent) event).getTab());
        }
    }


    /***********
     * 获取所选状态的checkId
     *
     * @return
     */
    public int getCheckIdByStatus(FragmentFactory.FragmentStatus status) {
        int id = R.id.rb_lend;
        switch (status) {
            case Account:
                id = R.id.rb_account;
                break;
            case Lend:
                id = R.id.rb_lend;
                break;
            case RentLend:
                id = R.id.rb_rent_lend;
                break;
            default:
                break;
        }

        return id;
    }

    /***********
     * 切换导航栏
     */
    private FragmentFactory.FragmentStatus odlState = FragmentFactory.FragmentStatus.None;

    public void changeTab(FragmentFactory.FragmentStatus status) {
        if (status == odlState) return;
        FragmentFactory.changeFragment(getSupportFragmentManager(), status, R.id.container);
        odlState = status;
    }


    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.showToast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    public RadioGroup getGroup() {
        return mGroup;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //MobclickAgent.onKillProcess(this);
        EventBus.getDefault().unregister(this);
        //XGPushHelper.getInstance().unRegisterPush(App.getContext());
        AppManager.getInstance().finishAllActivity();
        System.exit(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.OPEN_WIFI_FINISH) {//版本更新去设置打开wifi回来
            checkVersionUpdate();
        } else {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState); //解决fragment重叠问题
    }

    /**
     * 版本更新检查
     */
    private void checkVersionUpdate() {
        Call<JsonObject> call = RetrofitUtil.create().checkUpdateVersion(ViewUtil.getAppVersion(this), "android");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String json = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONObject info = jsonObject.optJSONObject("info");

                    if (info != null) {//有更新
                        final int force = info.optInt("force");//1位强制更新,其他为普通更新
                        final String url = App.getConfig().getBaseUrl()+info.optString("update_url");
                        String description = info.optString("description");
                        final String noticeTitle = App.getAPPName();
                        final String authority = "com.credit.xiaowei.provider.fileprovider";
                        if (force == 1) {//强制更新
                            new AlertFragmentDialog.Builder(MainActivity.this)
                                    .setTitle("发现新版本")
                                    .setContent(description)
                                    .setLeftBtnText("立即更新")
                                    .setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
                                        @Override
                                        public void dialogLeftBtnClick() {
                                            AppUpdateUtil.getInstance().downLoadApk(MainActivity.this, url, "xiaowei_credit", noticeTitle, authority, force);
                                        }
                                    })
                                    .setCancel(false)
                                    .build();
                        } else {//非强制更新
                            long lastTime = SpUtil.getLong(Constant.LAST_UPDATE_SHOW_TIME);
                            long currentTimeMillis = System.currentTimeMillis();
                            if (lastTime == 0 || currentTimeMillis - lastTime > 1000 * 60 * 60 * 24) {//未取消过 或 取消间隔大于1天
                                new AlertFragmentDialog.Builder(MainActivity.this)
                                        .setTitle("发现新版本")
                                        .setContent(description)
                                        .setLeftBtnText("立即更新")
                                        .setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
                                            @Override
                                            public void dialogLeftBtnClick() {
                                                AppUpdateUtil.getInstance().downLoadApk(MainActivity.this, url, "xiaowei_credit", noticeTitle, authority, force);
                                            }
                                        })
                                        .setRightBtnText("暂不更新")
                                        .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                                            @Override
                                            public void dialogRightBtnClick() {
                                                SpUtil.putLong(Constant.LAST_UPDATE_SHOW_TIME, System.currentTimeMillis());//记录上次取消时间
                                            }
                                        }).build();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.toString();
            }
        });
    }
}
