package com.innext.xjx.events;

import android.content.Context;

import com.innext.xjx.ui.my.bean.UserInfoBean;

public class LoginNoRefreshUIEvent extends BaseEvent {

	private UserInfoBean bean;
	private Context context;
	public LoginNoRefreshUIEvent(Context context, UserInfoBean bean)
	{
		this.context = context;
		this.bean = bean;
	}
	public UserInfoBean getBean() {
		return bean;
	}
	public void setBean(UserInfoBean bean) {
		this.bean = bean;
	}
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	
}
