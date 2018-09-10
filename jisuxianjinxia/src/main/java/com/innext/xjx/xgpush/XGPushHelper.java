package com.innext.xjx.xgpush;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import com.innext.xjx.util.LogUtils;
import com.orhanobut.logger.Logger;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;

public class XGPushHelper {

	
	private static volatile XGPushHelper helper;
	
	private XGPushHelper(){};
	
	public static XGPushHelper getInstance()
	{
		if(helper==null)
			helper = new XGPushHelper();
		return helper;
	}


	//启动并注册APP，同时绑定账号
	public void registerPush(Context context, String account)
	{
		LogUtils.loge("注册:"+account);
		if(TextUtils.isEmpty(account))
		{
			registerPush(context);
			return;
		}
		
		XGPushManager.registerPush(context, account, new XGIOperateCallback() {
			
			@Override
			public void onSuccess(Object arg0, int arg1) {

				LogUtils.loge("注册成功，设备token为：" + arg0);
			}
			
			@Override
			public void onFail(Object arg0, int arg1, String arg2) {
				// TODO Auto-generated method stub
				LogUtils.loge("注册失败，错误码：" + arg1 + ",错误信息：" + arg2);
			}
		});
		
		// 2.36（不包括）之前的版本需要调用以下2行代码
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
		{
			Intent service = new Intent(context, XGPushService.class);
			context.startService(service);
		}
	}
	//无账户注册
	private void registerPush(Context context)
	{
		XGPushManager.registerPush(context, new XGIOperateCallback() {
			
			@Override
			public void onSuccess(Object arg0, int arg1) {
				Logger.e("注册成功，设备token为：" + arg0);
			}
			
			@Override
			public void onFail(Object arg0, int arg1, String arg2) {
				LogUtils.loge("注册失败，错误码：" + arg1 + ",错误信息：" + arg2);
			}
		});
		
		// 2.36（不包括）之前的版本需要调用以下2行代码
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
		{
			Intent service = new Intent(context, XGPushService.class);
			context.startService(service);
		}
	}
	
	//取消注册
	public void unRegisterPush(Context context)
	{
		XGPushManager.unregisterPush(context, new XGIOperateCallback() {
			
			@Override
			public void onSuccess(Object arg0, int arg1) {
				LogUtils.loge("推送取消注册成功");
			}
			
			@Override
			public void onFail(Object arg0, int arg1, String arg2) {
				LogUtils.loge("推送取消注册失败");
			}
		});
	}
	
}
