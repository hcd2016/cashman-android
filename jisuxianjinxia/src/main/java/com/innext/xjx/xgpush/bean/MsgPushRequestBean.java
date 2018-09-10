package com.innext.xjx.xgpush.bean;




public class MsgPushRequestBean
{
	
	private String app_version;	//App当前版本号
	
	private String app_market;	//App市场来源
	
	private int task_id;		//推送消息ID(从推送参数中可获取)
	
	public String getApp_version() {
		return app_version;
	}
	public void setApp_version(String app_version) {
		this.app_version = app_version;
	}
	public String getApp_market() {
		return app_market;
	}
	public void setApp_market(String app_market) {
		this.app_market = app_market;
	}
	public int getTask_id() {
		return task_id;
	}
	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}
	
}
