package com.credit.xiaowei.ui.main;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import com.credit.pretend.activity.PretendMainActivity;
import com.credit.pretend.ptd_util.RetrofitUtil;
import com.credit.xiaowei.R;
import com.credit.xiaowei.app.App;
import com.credit.xiaowei.base.BaseActivity;
import com.credit.xiaowei.base.PermissionsListener;
import com.credit.xiaowei.config.ConfigUtil;
import com.credit.xiaowei.config.Constant;
import com.credit.xiaowei.dialog.AlertFragmentDialog;
import com.credit.xiaowei.events.LoginNoRefreshUIEvent;
import com.credit.xiaowei.ui.login.contract.LoginOutContract;
import com.credit.xiaowei.ui.login.presenter.LoginOutPresenter;
import com.credit.xiaowei.ui.my.contract.DeviceReportContract;
import com.credit.xiaowei.ui.my.presenter.DeviceReportPresenter;
import com.credit.xiaowei.util.SpUtil;
import com.credit.xiaowei.util.ViewUtil;
import com.google.gson.JsonObject;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 启动页
 * xiejingwen
 */
public class SplashActivity extends BaseActivity implements LoginOutContract.View
        , DeviceReportContract.View {
    private boolean isRequesting;//为了避免在onResume中多次请求权限
    private String[] permissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_CONTACTS};
    private LoginOutPresenter mLoginOutPresenter;
    private DeviceReportPresenter mDeviceReportPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initPresenter() {
        mDeviceReportPresenter = new DeviceReportPresenter();
        mDeviceReportPresenter.init(this);
        mLoginOutPresenter = new LoginOutPresenter();
        mLoginOutPresenter.init(this);
    }

    @Override
    public void loadData() {
        //新的应用重复启动解决方法
        if (!isTaskRoot()) {
            //判断该Activity是不是任务空间的源Activity,"非"也就是说是被系统重新实例化出来的
            //如果你就放在Launcher Activity中的话，这里可以直接return了
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        XGPushClickedResult click = XGPushManager.onActivityStarted(this);
        if (click != null) {
            //从推送通知栏打开-Service打开Activity会重新执行Laucher流程
            //查看是不是全新打开的面板
            if (isTaskRoot()) {
                return;
            }
            //如果有面板存在则关闭当前的面板
            finish();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isRequesting) {
            requestPermissions(permissions, mListener);
            isRequesting = true;
        }
    }

    private PermissionsListener mListener = new PermissionsListener() {
        @Override
        public void onGranted() {
            if (ConfigUtil.isOpenPretend) {//是否跳转伪页面.
                String appMetaData = ViewUtil.getAppMetaData(SplashActivity.this);
                Log.i("appMetaData",appMetaData);
                Call<JsonObject> call = RetrofitUtil.create().getIsOpenPretend(ViewUtil.getAppVersion(SplashActivity.this), ViewUtil.getAppMetaData(SplashActivity.this),"android");
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try {
                            JSONObject object = new JSONObject(response.body().toString());
                            String message = object.optString("message");
                            if ("Y".equals(message)) {//开启伪装
                                startActivity(PretendMainActivity.class);
                                finish();
                            } else {
                                normalProcess();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            normalProcess();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        normalProcess();
                    }
                });
            } else {
                normalProcess();
            }
        }

        //走正常流程
        public void normalProcess() {
            isRequesting = false;
            if (SpUtil.getInt(Constant.IS_FIRST_LOGIN, Constant.HAS_ALREADY_LOGIN) == Constant.HAS_ALREADY_LOGIN) {
                mLoginOutPresenter.loginOut();
                updateDeviceReport();
                startActivity(GuideActivity.class);
                finish();
            } else {
                startActivity(MainActivity.class);
                finish();
            }
            EventBus.getDefault().post(new LoginNoRefreshUIEvent(getApplicationContext(), App.getConfig().getUserInfo()));
        }

        @Override
        public void onDenied(List<String> deniedPermissions, boolean isNeverAsk) {
            if (!isNeverAsk) {//请求权限没有全被勾选不再提示然后拒绝
                new AlertFragmentDialog.Builder(mActivity)
                        .setLeftBtnText("退出").setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
                    @Override
                    public void dialogLeftBtnClick() {
                        finish();
                    }
                }).setContent("为了能正常使用\"" + App.getAPPName() + "\"，请授予所需权限")
                        .setRightBtnText("授权").setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                    @Override
                    public void dialogRightBtnClick() {
                        requestPermissions(permissions, mListener);
                    }
                }).build();
            } else {//全被勾选不再提示
                new AlertFragmentDialog.Builder(mActivity)
                        .setLeftBtnText("退出").setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
                    @Override
                    public void dialogLeftBtnClick() {
                        finish();
                    }
                }).setContent("\"" + App.getAPPName() + "\"缺少必要权限\n请手动授予\"" + App.getAPPName() + "\"访问您的权限")
                        .setRightBtnText("授权").setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                    @Override
                    public void dialogRightBtnClick() {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                        isRequesting = false;
                    }
                }).build();
            }
        }
    };

    private void updateDeviceReport() {
        if (App.getConfig().getUserInfo() != null) {
            mDeviceReportPresenter.deviceReport(ViewUtil.getDeviceId(this),
                    ViewUtil.getInstalledTime(this),
                    App.getConfig().getUserInfo().getUid() + "",
                    App.getConfig().getUserInfo().getUsername(),
                    ViewUtil.getNetworkType(this),
                    ViewUtil.getDeviceName(),
                    App.getConfig().getChannelName());
        }
    }


    @Override
    public void loginOutSuccess() {

    }

    @Override
    public void showLoading(String content) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorMsg(String msg, String type) {

    }

    @Override
    public void deviceReportSuccess() {

    }
}
