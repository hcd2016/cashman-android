package com.credit.xiaowei.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.credit.xiaowei.R;
import com.credit.xiaowei.config.ConfigUtil;
import com.credit.xiaowei.config.Constant;
import com.credit.xiaowei.events.BaseEvent;
import com.credit.xiaowei.events.EventController;
import com.credit.xiaowei.ui.login.activity.LoginActivity;
import com.credit.xiaowei.ui.login.activity.RegisterPhoneActivity;
import com.credit.xiaowei.util.LogUtils;
import com.credit.xiaowei.util.SpUtil;
import com.credit.xiaowei.util.StringUtil;
import com.credit.xiaowei.util.ToastUtil;
import com.credit.xiaowei.util.ViewUtil;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.MobclickAgent.UMAnalyticsConfig;
import com.umeng.socialize.PlatformConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class App extends MultiDexApplication{

	public static App mApp;

	@Override
	public void onCreate() {
		super.onCreate();
		mApp = this;
		configUtil = new ConfigUtil();
		//初始化bug检查库
		if (!configUtil.isDebug()){
			CrashReport.initCrashReport(this, "aaf70d3b1e", false);
		}
		ToastUtil.register(this);
		LogUtils.logInit(configUtil.isDebug());
		EventBus.getDefault().register(this);
		initUM();
		initLoadingView(this);
		
		 /*设置market*/
	    String MARKET_NAME = getAppChannel(this);
		LogUtils.loge("当前渠道:"+MARKET_NAME);
		/********
		 * 注册UMENG
		 */
		MobclickAgent.startWithConfigure(new UMAnalyticsConfig(this,"5833b527a325112e2d001faa",MARKET_NAME));
		configUtil.setChannelName(MARKET_NAME);
		//关闭默认统计
		MobclickAgent.openActivityDurationTrack(false);
		//MobclickAgent.setDebugMode(true);
	}


	public static String getAPPName(){
		return    getContext().getResources().getString(R.string.app_name);
	}
	
	public static void initLoadingView(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.layout_loan_loading, null);
		ViewUtil.setLoadingView(context, view);
	}

	public static void loadingDefault(Activity activity){
		ViewUtil.setLoadingView(activity, null);
		ViewUtil.showLoading(activity, "");
	}

	public static void loadingContent(Activity activity, String content){
		ViewUtil.setLoadingView(activity, null);
		ViewUtil.showLoading(activity, content);
	}

	public static void hideLoading(){
		ViewUtil.hideLoading();
	}

	public void initUM(){
        //微信    
        PlatformConfig.setWeixin(Constant.WX_APP_KEY, Constant.WX_APP_SECRET);
        //新浪微博
//        PlatformConfig.setSinaWeibo(Constant.SINA_APP_KEY, Constant.SINA_APP_SECRET);
        //QQ
        PlatformConfig.setQQZone(Constant.QQ_APP_ID, Constant.QQ_APP_KEY);
    }

	/*******
	 * 将事件交给事件派发controller处理
	 * @param event
	 */
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventMainThread(BaseEvent event)
	{
		event.setApplicationContext(getApplicationContext());
		EventController.getInstance().handleMessage(event);
	}
	

	//保存一些常用的配置
	private static ConfigUtil configUtil = null;
	public static ConfigUtil getConfig()
	{
		return configUtil;
	}

	public static void toLogin(Context context){
		String uName = SpUtil.getString(Constant.SHARE_TAG_USERNAME);
		if(!TextUtils.isEmpty(uName) && StringUtil.isMobileNO(uName)){
			Intent intent = new Intent(context,LoginActivity.class);
			intent.putExtra("tag", StringUtil.changeMobile(uName));
			intent.putExtra("phone", uName);
			context.startActivity(intent);
		}else{
			Intent intent = new Intent(context,RegisterPhoneActivity.class);
			context.startActivity(intent);
		}
		return;
	}
	/**
     * 设置channel
     * @return
     */
    public static String getAppChannel(Context context){
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        String ret = "";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.startsWith("META-INF/xjxchannel")) {
                    ret = entryName;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String[] split = ret.split("_");
        if (split != null && split.length >= 2) {
            return ret.substring(split[0].length() + 1);
        } else {
            return configUtil.getChannelName();
        }
    }
	public static Context getContext() {
		return mApp.getApplicationContext();
	}
}
